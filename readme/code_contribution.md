## Future Release Plan 未来发布计划
For major changes or significant dependency upgrades, we will publish a GitHub Release.  
后续有大的改动，或依赖有大版本更新时，我们会发布GitHub Release。  

Therefore, we recommend clicking watch -> custom -> releases on GitHub to receive release notifications as soon as possible.  
所以建议GitHub上面点watch -> custom -> releases，这样就能第一时间收到发布releases的通知。  

We plan to upgrade to Java 25 and Spring Boot 4 in the future (community contributions are welcome).  
我们计划后续会更新为Java 25 以及 Spring Boot 4 (我们也欢迎社区贡献代码)。  

Currently, using the latest minor version of the previous major version has the following benefits:  
暂时使用上一个大版本的最新小版本的好处是：
1. Stability 稳定性
2. Compatibility 兼容性
3. Better AI Vibe Coding support, as LLMs are trained on past knowledge and may not be familiar with the latest features and syntax.  
   更好的AI Vibe Coding支持，因为LLM是用过往的知识训练的，其本身不熟悉最新版本的特性和语法。

## Suggested Code Contribution Areas 建议贡献领域
1. Upgrade to Java 25 and Spring Boot 4.
2. Add Redis caching for higher performance.   
   For example, we noted in comments that [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) can be optimized with caching. We have not added it because not all systems need that level of performance, and Redis is not a core feature of this framework. Contributions are welcome, and we are happy to review and merge them into a beta branch.  
   例如，我们在注释中提到 [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) 可以通过缓存优化。我们尚未添加该功能，因为并非所有系统都需要如此高的性能，而且 Redis 不是该框架的核心特性。欢迎贡献实现，我们也很乐意审核并合并到 beta 分支。

## Code Contribution 贡献代码

We welcome all forms of contributions, including code, documentation, tests, and other improvements.  
我们欢迎任何形式的贡献，无论是代码、文档、测试还是其他方面的改进。

For example, updating deprecated methods, correcting typos, or improving documentation in Chinese or English are all welcome.  
比如更新下某个已经被标记为弃用的方法，错别字修正，中英文档完善等都是欢迎的。

Please ensure your pull request is well-tested and includes both Chinese and English comments (or English only).  
请确保您pull request的代码是仔细测试过的,以及有中英文或英文注释。

If you have specific ideas to discuss, feel free to email dtd-ai-code@hkpc.org, describing your ideas and planned contributions. We will create a WeChat or Discord group for contributors to facilitate communication and collaboration.    
如果您有具体想法想和我们沟通，也欢迎email dtd-ai-code@hkpc.org, 说明您的想法和计划贡献的内容，我们会创建个贡献者微信群或Discord群，方便交流与协作。


## [Code Convention 代码规范](./code_convention.md)
