## Future Release Plan 
For major changes or significant dependency upgrades, we will publish a GitHub Release.

Therefore, we recommend clicking watch -> custom -> releases on GitHub to receive release notifications as soon as possible.  

We plan to upgrade to Java 25 and Spring Boot 4 in the future (community contributions are welcome).

Currently, using the latest minor version of the previous major version has the following benefits:
1. Stability 
2. Compatibility 
3. Better AI Vibe Coding support, as LLMs are trained on past knowledge and may not be familiar with the latest features and syntax.  


## Suggested Code Contribution Areas 
1. Upgrade to Java 25 and Spring Boot 4.
2. Add Redis caching for higher performance.   
   For example, we noted in comments that [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) can be optimized with caching. We have not added it because not all systems need that level of performance, and Redis is not a core feature of this framework. Contributions are welcome, and we are happy to review and merge them into a beta branch.  


## Code Contribution 

We welcome all forms of contributions, including code, documentation, tests, and other improvements.  

For example, updating deprecated methods, correcting typos, or improving documentation in Chinese or English are all welcome.  

Please ensure your pull request is well-tested and includes both Chinese and English comments (or English only).  

If you have specific ideas to discuss, feel free to email dtd-ai-code@hkpc.org, describing your ideas and planned contributions. We will create a WeChat or Discord group for contributors to facilitate communication and collaboration.    


## [Code Convention](./code_convention.md)
