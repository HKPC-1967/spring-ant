package org.hkpc.dtd.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
* OpenApi is named "Swagger" before, now it become "Spring Document"
*/
@Configuration
public class OpenApiConfig implements WebMvcConfigurer {
    // This is an OpenAPI name, not an HTTP header or a role. It is used in two places:
    // 1. addSecuritySchemes(...) defines the Bearer scheme with this name.
    // 2. SecurityRequirement.addList(...) selects that named scheme for an operation.
    // Both places must use the same name so OpenAPI can connect them.
    private static final String BEARER_AUTH_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                // Define the authentication scheme named "bearerAuth". Swagger UI uses
                // this definition to show Authorize and add the "Bearer " prefix to the JWT.
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info().title("Platform API")
                        .description("Platform API")
                        .version("1.0"));
    }

    @Bean
    public OperationCustomizer customizeOperationSecurity() {
        return (operation, handlerMethod) -> {
            String methodName = handlerMethod.getMethod().getName();

            // These operations are intentionally callable without a JWT, so their
            // OpenAPI definitions must not reference the Bearer authentication scheme.
            if (methodName.equals("login") || methodName.equals("refreshToken") || methodName.equals("errorMessageDemo")) {
                return operation;
            }

            // Select the "bearerAuth" scheme defined by addSecuritySchemes(...) above
            // and mark this operation as requiring it.
            // Swagger UI will then send the authorized value as:
            // Authorization: Bearer <JWT>
            operation.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME));
            return operation;
        };
    }
}
