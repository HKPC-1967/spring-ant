# AGENTS.md

## Big picture
- Spring Boot 3 backend (Java 21) with `/api` context path; entrypoint is `src/main/java/org/hkpc/dtd/Application.java`.
- Controllers live under `src/main/java/org/hkpc/dtd/business/*/controller` and are wrapped by an aspect (`common/core/aspect/MainAspect.java`) that logs and enforces the unified response envelope from `ResponseStructureUtil`.
- Database access uses MyBatis (mapper XML in `src/main/resources/mapper/*.xml`, mapper interfaces in `component/postgres/dao`), PageHelper for pagination, and Druid as the datasource.
- JWT auth + RBAC: `JwtAuthenticationFilter` validates tokens, sets Spring Security roles, and `SpringSecurityConfig` applies role checks for specific routes.

## Response + error handling conventions
- Successful controller responses are wrapped by `ResponseStructureUtil.getSuccessResponse(...)` unless the URL is in `MainAspect.NO_CHANGE_RESULT_URLS`.
- Errors should use `CodeException` + `ErrorCodeEnum`; `GlobalExceptionHandler` maps common validation/format errors to consistent error codes.
- Logging for alarms uses markers from `CommonConst` (`MARK_ALARM_SYSTEM`, `MARK_ALARM_CUSTOM`).

## Security + auth flow
- JWT access token header is `Authorization: Bearer <token>`; refresh token header is `refreshToken` (see `CommonConst`).
- Routes excluded from JWT checks are in `CommonConst.NO_CHECK_JWT_TOKEN_URLS` and `OpenApiConfig` excludes login/refresh from Swagger header injection.
- JWT signing key is stored encrypted in DB (see `DbConfigService.KeyEnum.JWT_KEY`), not in YAML.
- Swagger UI is protected by basic auth (`project-config.spring-security.swagger-auth.*` in `application-dev.yml`).

## MyBatis code generation pipeline
- DB table/column comments are the source of truth and are propagated via MyBatis Generator + custom comments.
- Generator entrypoint: `component/postgres/mybatis/generator/MybatisGenerator.java` with config in `src/main/resources/generator-configuration.xml`.
- `CustomCommentGenerator` adds `@Schema` annotations to generated models; the workflow copies model classes into DTOs for Swagger docs.
- Background doc: `readme/pipelined_model_code_generation.md`.

## Key configs
- `src/main/resources/application.yml`: `/api` context path, Swagger paths, MyBatis type aliases, PageHelper settings.
- `src/main/resources/application-dev.yml`: local DB connection, AES key for DB config, CORS origins, Swagger basic auth.

## Local workflows (from README)
- Build: `./gradlew clean` then `./gradlew build -x test`.
- Run (dev profile): `./gradlew bootRun -Dspring.profiles.active=dev` or set `SPRING_PROFILES_ACTIVE=dev` in IDE.
- Database init: `readme/database_initialization.md` + `readme/sql/tables.sql`.

## Where to look first
- Authentication: `common/core/security/filter/JwtAuthenticationFilter.java`, `common/core/jwt/JwtValidateComponent.java`.
- Unified API format + logging: `common/core/aspect/MainAspect.java`, `common/core/aspect/utils/ResponseStructureUtil.java`.
- RBAC policy: `common/core/security/SpringSecurityConfig.java`.

