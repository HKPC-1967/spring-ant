package org.hkpc.dtd.common.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.hkpc.dtd.common.consts.CommonConst;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
* OpenApi is named "Swagger" before, now it become "Spring Document"
*/
@Configuration
public class OpenApiConfig implements WebMvcConfigurer {
     /**
     * you can set the test token to a valid JWT token, so that when you try the API in Swagger UI, you can directly use the test token. </br>
     * But remember to change it back before commit, as the test token may be valid and can be used by others if you commit it to git.
     */
    public static final String TEST_JWT_TOKEN ="Set this test token in OpenApiConfig.java";

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Platform API")
                        .description("Platform API")
                        .version("1.0"));
    }

    @Bean
    public OperationCustomizer customizeGlobalHeaders() {
        return (operation, handlerMethod) -> {
            String methodName = handlerMethod.getMethod().getName();

            // all requests need JWT access token except login and refreshToken
            if (methodName.equals("login") || methodName.equals("refreshToken") || methodName.equals("errorMessageDemo")) {
                return operation;
            }

            operation.addParametersItem(new Parameter()
                    .name(CommonConst.HEADER_JWT_ACCESS_TOKEN)
                    .in(ParameterIn.HEADER.toString())
                    .schema(new Schema<>().type("string").example(TEST_JWT_TOKEN))
                    .required(true));
            return operation;
        };
    }
}
