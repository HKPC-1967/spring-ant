Language : [简体中文](README.zh-CN.md) | [繁體中文](README.zh-HK.md)

# Sprint Ant Family
Sprint Ant Family consists of three open-source repositories designed for rapid web application development.  
- **Sprint Ant Backend**: the Spring Boot Backend (this repository).
- **[Sprint Ant Frontend](https://github.com/HKPC-1967/spring-ant-frontend)**: the React Frontend based on **Ant Design Pro**, for quickly building an **admin-panel web application**, see Feature Highlights screenshot below.
- **[Sprint Ant Frontend API Core](https://github.com/HKPC-1967/spring-ant-frontend-api-core)**: if you are not using **Ant Design Pro** as your UI framework, then you can copy the 200 lines of core TypeScript code into your existing Frontend to integrate with the **Spring Ant Backend** API. 


## Feature Highlights
- Login with JWT tokens (default username: `admin` or `user`, password: `ant.design`)  
- Multi-language support  
![login_Page.jpg](readme/document_images/readme/Login_Page.jpg)
- Role-Based Access Control (Spring Security RBAC for backend APIs)  
  As shown in red below, the admin on the left has access to "admin page", "admin sub-page", and "admin button", while the regular user on the right does not.
- Error Codes and Display Types  
  As shown in blue below, the backend can return different error codes and display types based on business needs. Network and HTTP-level errors are also handled uniformly.  
- Configurable Layout: Dark Mode, Theme Color, Navigation (side, top, mix), etc. 
![RBAC_Message.jpg](readme/document_images/readme/RBAC_Message.jpg)
- Loading spinners (red below)
- Loading overlay to prevent user interaction while requests are in progress
- Pagination (yellow below)
![Loading_Pagination.png](readme/document_images/readme/Loading_Pagination.png)

---

# Sprint Ant Backend
A Java Spring Boot backend framework for rapid development, with core features such as JWT authentication, RBAC (Spring Security), Aspect (unified API format, logging, and error handling), pagination, and pipelined model code generation from PostgreSQL -> MyBatis Generator -> Swagger (SpringDoc).

## [Database initialization](./readme/database_initialization.md)

## Build the project
* Gradle (to build a .jar file; you may change "/" to "\\" to suit your OS)   
`./gradlew clean`  
`./gradlew build -x test`
* Docker (to build docker image)   
`docker build -t spring_ant_backend:0.0.1 .`

## 5 ways to run the project in the DEV environment (application-dev.yml)

* Run with IDEA Ultimate Edition (recommended for local development and debugging)</br>
Edit Configuration -> Active profile: `dev`

* Run with IDEA Community Edition (recommended for local development and debugging)</br>
Edit Configuration -> Environment variables: `SPRING_PROFILES_ACTIVE=dev`

* Run with Gradle </br>
`./gradlew bootRun -Dspring.profiles.active=dev`

* Run "java spring_ant_backend-0.0.1-SNAPSHOT.jar" as a background process on Linux </br>
`nohup java -jar  .\spring_ant_backend-0.0.1-SNAPSHOT.jar   --spring.profiles.active=dev &`

* Run with Docker (Recommended for Production)</br>
`docker run -d --add-host host.docker.internal:host-gateway -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev --name spring_ant_backend spring_ant_backend:0.0.1`   



## OpenAPI (Swagger) URLs
The username and password are configured under `swagger-auth` in [application-dev.yml](src/main/resources/application-dev.yml).
- Web UI:  
http://localhost:8080/api/swagger-ui/index.html
- OpenAPI JSON configuration file:  
http://localhost:8080/api/api-docs


## [Framework Design](./readme/framework_design.md)


## [Future Release Plan, Code Contribution, and Code Convention](./readme/code_contribution.md)
