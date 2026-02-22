package org.hkpc.dtd.common.core.security.filter;

import com.alibaba.fastjson2.JSON;
import lombok.NonNull;
import org.hkpc.dtd.business.user.service.UserAccountService;
import org.hkpc.dtd.common.consts.CommonConst;
import org.hkpc.dtd.common.core.jwt.JwtValidateComponent;
import org.hkpc.dtd.common.core.jwt.model.UserJwtSubjectContextHolder;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.security.enums.TokenTypeEnum;
import org.hkpc.dtd.common.core.jwt.model.UserJwtSubject;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.hkpc.dtd.common.core.aspect.utils.ResponseStructureUtil;
import org.hkpc.dtd.common.utils.CompareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Will not check JWT token for this refresh token URL, but will check the refresh token in the header and validate it. </br>
     * This is because when the access token expires, the front end will call this URL to get a new access token using the refresh token. If we check JWT token for this URL, it will cause a dead loop, as the access token is expired and the front end cannot get a new access token.</br>
     * No need to add server.servlet.context-path (defined in application.yml, e.g., /api) in front of the URL, because we get the urlPath by httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length()), which has removed the context path. So just use /user/login/refreshToken instead of /api/user/login/refreshToken.
    */
    private static final String LOGIN_REFRESH_TOKEN_URL = "/user/login/refreshToken";

    @Value("${project-config.ENABLE_REFRESH_TOKEN}")
    private Boolean isEnableRefreshToken;

    private final JwtValidateComponent jwtValidateComponent;
    private final UserJwtSubjectContextHolder userJwtSubjectContextHolder;
    private final UserAccountService userAccountService;

    @Autowired
    public JwtAuthenticationFilter(JwtValidateComponent jwtValidateComponent, UserJwtSubjectContextHolder userJwtSubjectContextHolder, UserAccountService userAccountService) {
        this.jwtValidateComponent = jwtValidateComponent;
        this.userJwtSubjectContextHolder = userJwtSubjectContextHolder;
        this.userAccountService = userAccountService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // this value is defined by server.servlet.context-path in application.yml, e.g., /api
        String contextPath = httpServletRequest.getContextPath();
        String requestURI = httpServletRequest.getRequestURI();
        String urlPath = requestURI.substring(contextPath.length());
//        String method = httpServletRequest.getMethod();

        // only allow Login and Swagger Document without a token
        if (CompareUtil.anyMatch(CommonConst.NO_CHECK_JWT_TOKEN_URLS, urlPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // get the jwtToken and tokenType (accessToken or refreshToken) buy urlPath and header
        String jwtToken = request.getHeader(CommonConst.HEADER_JWT_ACCESS_TOKEN);
        int tokenType = TokenTypeEnum.ACCESS_TOKEN.getValue();
        if (LOGIN_REFRESH_TOKEN_URL.equals(urlPath)) {
            if (!isEnableRefreshToken) {
                response.getWriter().write(JSON.toJSONString(ResponseStructureUtil.getResponse(ErrorCodeEnum.INVALID_JWT_TYPE)));
                return;
            }
            jwtToken = request.getHeader(CommonConst.HEADER_JWT_REFRESH_TOKEN);
            tokenType = TokenTypeEnum.REFRESH_TOKEN.getValue();
        }

        // check the token format
        if (jwtToken == null || !jwtToken.startsWith(CommonConst.JWT_TOKEN_PREFIX)) {
            // if the token is not present, return an error response
            logger.error("Illegal JWT token format [{}][{}]", urlPath, jwtToken);
            response.getWriter().write(JSON.toJSONString(ResponseStructureUtil.getResponse(ErrorCodeEnum.INVALID_JWT_TOKEN)));
            return;
        }
        jwtToken = jwtToken.replace(CommonConst.JWT_TOKEN_PREFIX, "");


        try {
            UserJwtSubject userJwtSubject = jwtValidateComponent.ValidateJwtAndGetSubject(jwtToken);

            // check the tokenType
            if (tokenType != userJwtSubject.tokenType()) {
                logger.error("Illegal JWT type [{}][{}]", urlPath, jwtToken);
                response.getWriter().write(JSON.toJSONString(ResponseStructureUtil.getResponse(ErrorCodeEnum.INVALID_JWT_TYPE)));
                return;
            }

            userJwtSubjectContextHolder.setUserJwtSubject(userJwtSubject);

            // This extra validation is for higher security (but it may not be necessary for your system), but less performant. 
            // You can remove it if you don't need it. You can also optimise the performance with the Redis cache.
            userAccountService.validateJwtSubjectWithDB(userJwtSubject);

            setSpringSecurityContext(userJwtSubject, request);
        } catch (CodeException e) {
            logger.info("token invalid[{}],[{}]", urlPath, jwtToken);
            // catch ErrorCodeEnum.INVALID_JWT_TOKEN and return to front end
            response.getWriter().write(JSON.toJSONString(ResponseStructureUtil.getResponse(e.getCodeEnum())));
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * tell Spring Security the user's role for role-based access control (RBAC)
     */
    private void setSpringSecurityContext(UserJwtSubject userJwtSubject, HttpServletRequest request) {
        Set<GrantedAuthority> grantedAuthorities = userJwtSubject.roleIds().stream()
                //"ROLE_" is defined in spring security
                .map(roleId -> new SimpleGrantedAuthority("ROLE_" + roleId))
                .collect(Collectors.toSet());
        // no need to pass a password here, because we are handling authentication through JWT tokens and not using Spring Security's built-in authentication mechanisms that require a password.
        UserDetails userDetails = new User(userJwtSubject.username(), "", grantedAuthorities);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // setAuthentication to spring security to apply role-based access control (RBAC) based on the user's roles and authorities.
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
