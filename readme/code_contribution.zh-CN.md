## 未来发布计划
后续有大的改动，或依赖有大版本更新时，我们会发布 GitHub Release。

所以建议 GitHub 上面点 watch -> custom -> releases，这样就能第一时间收到发布 releases 的通知。

本仓库已基于 **Java 25** 与 **Spring Boot 4**（版本见 `build.gradle`）。日常的小版本依赖升级与框架改进通过 Release 与 Pull Request 交付。


## 建议贡献领域
1. **CI/CD** — 例如 GitHub Actions 执行 `compileJava`、`test`，以及可选的 Docker 镜像构建。
2. **可观测性** — Spring Boot Actuator（容器健康/就绪探针）、结构化日志或指标接入。
3. **Redis 缓存**以获得更高性能。  
   例如，我们在注释中提到 [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) 可以通过缓存优化。我们尚未添加该功能，因为并非所有系统都需要如此高的性能，而且 Redis 不是该框架的核心特性。欢迎贡献实现，我们也很乐意审核并合并到 beta 分支。
4. **测试** — 使用 MockMvc / `spring-security-test` 覆盖登录、刷新令牌、RBAC 与统一 API 响应格式。
5. **配置规范** — 无密钥的示例 YAML、生产环境凭据外置及相关文档。


## 贡献代码

我们欢迎任何形式的贡献，无论是代码、文档、测试还是其他方面的改进。

比如更新某个已经被标记为弃用的方法、错别字修正、中英文档完善等都是欢迎的。

请确保您 pull request 的代码是仔细测试过的，以及有中英文或英文注释。

如果您有具体想法想和我们沟通，也欢迎 email dtd-ai-code@hkpc.org，说明您的想法和计划贡献的内容，我们会创建个贡献者微信群或 Discord 群，方便交流与协作。


## [代码规范](./code_convention.zh-CN.md)
