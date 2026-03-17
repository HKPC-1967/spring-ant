Language : [English](README.md) | [繁體中文](README.zh-HK.md)

# Sprint Ant Family
Sprint Ant Family 由三个开源仓库组成，旨在实现 Web 应用的快速开发。  
- **Sprint Ant Backend**：Spring Boot后端（本仓库）。
- **[Sprint Ant Frontend](https://github.com/HKPC-1967/spring-ant-frontend)**：基于 **Ant Design Pro** 的 React 框架，用于快速构建一个**后台管理系统**。
- **[Sprint Ant Frontend API Core](https://github.com/HKPC-1967/spring-ant-frontend-api-core)**： 当你不使用 **Ant Design Pro** 作为 UI 框架时，可以将核心 TypeScript 代码（大约 200 行）复制到你现有的前端项目中，以快速集成 **Spring Ant Backend** API。


## 功能亮点
- 基于 JWT Token 的登录认证 （默认用户名：`admin` 或 `user`，密码：`ant.design`）  
- 多语言支持  
![login_Page.png](readme/document_images/readme/Login_Page.jpg)
- 基于角色的访问控制 (RBAC)  
  如下图红框所示：左侧管理员admin可访问 "admin page"、"admin sub-page"、"admin button"，右侧普通用户user则无权限。后端用Spring Security实现RABC。
- 错误码与展示类型  
  如下图蓝框所示：后端可按业务需要返回不同`错误码`与`展示类型`；同时前端对网络层与 HTTP 层错误进行统一处理。  
- 可配置布局：深色模式、主题色、导航模式（侧边、顶部、混合）等。 
 
![RBAC_Message.jpg](readme/document_images/readme/RBAC_Message.jpg)
- Loading 状态管理（局部loading动画和全局遮罩层，避免请求进行中用户继续操作）（如下图红框）。
- 分页（如下图黄框）
 
![Loading_Pagination.png](readme/document_images/readme/Loading_Pagination.png)

---

# Sprint Ant Backend
一个用于快速开发的 Java Spring Boot 后端框架，核心特性包括：JWT 认证、RBAC（Spring Security）、AOP 切面（统一 API 格式、日志与错误处理）、分页，以及基于 PostgreSQL -> MyBatis Generator -> Swagger（SpringDoc）的流水线式模型代码生成。

## [数据库初始化](./readme/database_initialization.zh-CN.md)

## 构建项目
* Gradle（用于构建 .jar 文件；你可以根据操作系统将 "/" 改为 "\\"）   
`./gradlew clean`  
`./gradlew build -x test`
* Docker（用于构建 Docker 镜像）   
`docker build -t spring_ant_backend:0.0.1 .`

## DEV 环境（[application-dev.yml](src/main/resources/application-dev.yml)）下的 5 种运行方式

* 使用 IDEA Ultimate Edition 运行（推荐用于本地开发与调试）</br>
Edit Configuration -> Active profile: `dev`

* 使用 IDEA Community Edition 运行（推荐用于本地开发与调试）</br>
Edit Configuration -> Environment variables: `SPRING_PROFILES_ACTIVE=dev`

* 使用 Gradle 运行 </br>
`./gradlew bootRun -Dspring.profiles.active=dev`

* 在 Linux 上以后台进程运行 "java spring_ant_backend-0.0.1-SNAPSHOT.jar" </br>
`nohup java -jar  .\spring_ant_backend-0.0.1-SNAPSHOT.jar   --spring.profiles.active=dev &`

* 使用 Docker 运行（推荐用于生产环境）</br>
`docker run -d --add-host host.docker.internal:host-gateway -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev --name spring_ant_backend spring_ant_backend:0.0.1`   



## OpenAPI（Swagger）地址
用户名和密码在 [application-dev.yml](src/main/resources/application-dev.yml) 的 `swagger-auth` 下配置。
- Web UI：  
http://localhost:8080/api/swagger-ui/index.html
- OpenAPI JSON：  
http://localhost:8080/api/api-docs


## [框架设计](./readme/framework_design.zh-CN.md)


## [后续发布计划、代码贡献与代码规范](./readme/code_contribution.zh-CN.md)

## 为什么我们创建这个开源项目
这个项目由香港生产力促进局（HKPC）的 Nick、Jacob 和 Ken 开源。最初的想法来源于我们发现 Ant Design Pro 是一个非常优秀的 React UI 解决方案，但并没有一个现成直接可用的后端框架与之配套。我们希望将这个项目开源能够帮助到有需要的其他人。例如，中小企业（SMEs）可以利用这个项目，快速搭建一个后台管理系统。即使你不懂 Java 和 Spring Boot 也不用担心 —— 你只需要先把它跑起来，详细的文档和代码注释会帮助你快速上手。

如果这个项目在任何方面对你有帮助，欢迎告诉我们；如果你有任何问题，也欢迎在 **[GitHub Discussions 页面](https://github.com/HKPC-1967/spring-ant/discussions)** 中提问。你的支持与反馈对我们非常宝贵。

