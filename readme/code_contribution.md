## Future Release Plan
For major changes or significant dependency upgrades, we will publish a GitHub Release.

Therefore, we recommend clicking watch -> custom -> releases on GitHub to receive release notifications as soon as possible.

This repository targets **Java 25** and **Spring Boot 4** (see pinned versions in `build.gradle`). Minor dependency bumps and framework improvements are delivered through releases and pull requests.


## Suggested Code Contribution Areas
1. **CI/CD** — GitHub Actions (or similar) running `compileJava` and `test`, plus optional Docker image builds.
2. **Observability** — Spring Boot Actuator (health/readiness for containers), structured logging, or metrics hooks.
3. **Redis caching** for higher performance.  
   For example, we noted in comments that [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) can be optimized with caching. We have not added it because not all systems need that level of performance, and Redis is not a core feature of this framework. Contributions are welcome, and we are happy to review and merge them into a beta branch.
4. **Tests** — MockMvc / `spring-security-test` coverage for login, refresh token, RBAC, and the unified API response envelope.
5. **Configuration hygiene** — example YAML without secrets, externalized credentials for production, and documentation updates.


## Code Contribution

We welcome all forms of contributions, including code, documentation, tests, and other improvements.

For example, updating deprecated methods, correcting typos, or improving documentation in Chinese or English are all welcome.

Please ensure your pull request is well-tested and includes both Chinese and English comments (or English only).

If you have specific ideas to discuss, feel free to email dtd-ai-code@hkpc.org, describing your ideas and planned contributions. We will create a WeChat or Discord group for contributors to facilitate communication and collaboration.


## [Code Convention](./code_convention.md)
