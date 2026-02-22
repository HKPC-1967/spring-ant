package org.hkpc.dtd.common.core.security;

import org.hkpc.dtd.common.core.aspect.MainAspect;
import org.hkpc.dtd.common.core.security.filter.JwtAuthenticationFilter;
import org.hkpc.dtd.common.core.security.enums.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String SWAGGER_ROLE = "SWAGGER";

    @Value("${project-config.spring-security.cors-allowed-origins}")
    private List<String> corsAllowedOrigins;

    @PostConstruct
    public void init() {
        logger.info("Spring Security CORS Allowed Origins: {},size:{}",corsAllowedOrigins, corsAllowedOrigins.size());
    }

    /**
     * String encodedPassword = passwordEncoder.encode(rawPassword);  // encrypt password
     * if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {  // compare password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasRole(SWAGGER_ROLE)
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.disable())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                /**
                 * 1. CORS (Cross-Origin Resource Sharing) 跨域资源共享
                 * Spring Security 引入了自己的过滤器链，默认情况下会拦截所有请求，包括跨域请求和预检请求（OPTIONS 方法）
                 * Spring Security CORS Document:
                 * https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
                 */
//                .cors(Customizer.withDefaults()) // Enable CORS with default configuration.
//                .cors(CorsConfigurer::disable) // Warning: CORS is a browser-based security feature. By disabling CORS in Spring Security with .cors(CorsConfigurer::disable), you are not removing CORS protection from your browser. Instead, you are removing CORS support from Spring Security, and users will not be able to interact with your Spring backend from a cross-origin browser application. To fix CORS errors in your application, you must enable CORS support, and provide an appropriate configuration source.
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    // change to specific domain in production, e.g., https://www.example.com, do not use "*" in production, otherwise it will cause security issues.
                    corsConfiguration.setAllowedOrigins(corsAllowedOrigins);
                    /**
                     * For a unified log format and simplicity, only POST and GET (use "?a=6&b=6", not "/a/6/b/6") methods are tested, ensuring guaranteed support. {@link MainAspect#around}
                     * 中文注释：为了切面的日志统一格式和简化，只测试了 POST 和 GET（使用 "?a=6&b=6"，而不是 "/a/6/b/6"）保证支持，建议优先用POST。{@link MainAspect#around}
                     */
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    return corsConfiguration;
                }))

                /**
                 * 2. jwtAuthenticationFilter can ensure that the JWT token is validated and the user is authenticated with our own login logic {@link JwtAuthenticationFilter#doFilterInternal},
                 * then use Spring Security for authorization logic (RBAC).
                 * 中文注释：jwtAuthenticationFilter 可以确保 JWT 令牌被验证，并且是通过我们自己的登录认证逻辑（{@link JwtAuthenticationFilter#doFilterInternal}），
                 * 然后使用 Spring Security 进行鉴权逻辑（RBAC）。
                 */
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                /**
                 * 3. RBAC (Role Based Access Control) 基于角色的访问控制
                 * the authorizeHttpRequests will be checked first, then process {@link MainAspect#around}
                 */
                .authorizeHttpRequests(authorize -> authorize
                        // Role Based Access Control (RBAC)
                        .requestMatchers("/demo/rule/insert").hasRole(RoleEnum.ADMIN.getRoleId())
//                        .requestMatchers("/demo/message/errorMessageDemo").hasRole(RoleEnum.ADMIN.getRoleId())
                        //.requestMatchers("/demo/rule/**").hasRole(RoleEnum.ADMIN.getRoleId())
                        //.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // Other requests are configured as needed
                        // We don't need this as we implemented the JWT authentication (login) ourselves. We mainly need Spring Security for RBAC.
                        //.anyRequest().authenticated()
                        // Be careful when using .anyRequest().permitAll(), as it will allow all requests, which may cause security issues for your system.
                        // We use .permitAll() here because we have implemented JWT authentication (login) ourselves, and we only have the USER role apart from the ADMIN role.
                        .anyRequest().permitAll()
                )

                // As we are using JWT which is stateless, we can disable session management
                .sessionManagement(session -> session.disable())
                // When using JWT tokens for authentication, it is common to disable CSRF protection because JWT tokens are not vulnerable to CSRF attacks. CSRF protection is primarily needed for stateful sessions where cookies are used for authentication.
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            @Value("${project-config.spring-security.swagger-auth.username}") String username,
            @Value("${project-config.spring-security.swagger-auth.password}") String password,
            PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles(SWAGGER_ROLE)
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
