## 未来发布计划
后续有大的改动，或依赖有大版本更新时，我们会发布GitHub Release。  

所以建议GitHub上面点watch -> custom -> releases，这样就能第一时间收到发布releases的通知。  

我们计划后续会更新为Java 25 以及 Spring Boot 4 (我们也欢迎社区贡献代码)。  

暂时使用上一个大版本的最新小版本的好处是：
1. 稳定性
2. 兼容性
3. 更好的AI Vibe Coding支持，因为LLM是用过往的知识训练的，其本身不熟悉最新版本的特性和语法。

## 建议贡献领域
1. 升级到 Java 25 和 Spring Boot 4.
2. 增加Redis缓存以获得更高性能。  
   例如，我们在注释中提到 [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) 可以通过缓存优化。我们尚未添加该功能，因为并非所有系统都需要如此高的性能，而且 Redis 不是该框架的核心特性。欢迎贡献实现，我们也很乐意审核并合并到 beta 分支。

## 贡献代码

我们欢迎任何形式的贡献，无论是代码、文档、测试还是其他方面的改进。

比如更新下某个已经被标记为弃用的方法，错别字修正，中英文档完善等都是欢迎的。

请确保您pull request的代码是仔细测试过的,以及有中英文或英文注释。

如果您有具体想法想和我们沟通，也欢迎email dtd-ai-code@hkpc.org, 说明您的想法和计划贡献的内容，我们会创建个贡献者微信群或Discord群，方便交流与协作。


## [代码规范](./code_convention.zh-CN.md)
