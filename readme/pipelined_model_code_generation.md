## Pipelined Model Code Generation | 流水线式模型代码生成  
This is a pipelined model code generation approach from PostgreSQL table schema to MyBatis Generator output, then to Controller HTTP POST DTO, and finally to SpringDoc annotations.  
这是一种从 PostgreSQL 数据库表结构到 MyBatis Generator 代码，再到 Controller 的 HTTP POST DTO，最后到 SpringDoc 注解的流水线式模型代码生成方法。  
It helps developers ship faster, maintain consistency in one place, and reduce repeated work.  
它可以帮助开发者更快开发、统一维护并减少重复工作。  

### Design Philosophy | 设计理念  
- Compared with Hibernate (a full ORM framework), we choose MyBatis (a semi-ORM framework) for better flexibility, stronger control, and more native SQL friendliness for newcomers.  
  相比 Hibernate（全 ORM 框架），我们选择 MyBatis（半 ORM 框架），因为它在灵活性、可控性和原生 SQL 友好性方面更好，新人也更快上手。  
- The pipeline idea is common in software engineering: the output of one task becomes the input of the next task to form a workflow chain.  
  Pipeline 思想在软件工程中很常见：一个任务的输出成为下一个任务的输入，从而形成流水线。  
- This method is not fully automatic; it is semi-automatic by design, where machines handle most repetitive work while humans keep room for flexible decisions.  
  该方法并非全自动，而是有意设计成半自动：机器处理大部分重复工作，人工保留灵活决策空间。  
- A key principle is that database table comments are the single source of truth, so metadata is maintained in one place and propagated through Model, Mapper, DTO, and API docs.  
  一个关键理念是以数据库表注释作为单一事实来源，在一个地方维护元数据，再传播到 Model、Mapper、DTO 和 API 文档。  

### Code Implementation Details | 代码实现细节  
[MybatisGenerator.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/generator/MybatisGenerator.java)  
[generator-configuration.xml](../src/main/resources/generator-configuration.xml)  
[CustomCommentGenerator.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/generator/CustomCommentGenerator.java)  
[OpenApiConfig.java](./../src/main/java/org/hkpc/dtd/common/config/OpenApiConfig.java)  

### Three Core Stages in Pipeline | Pipeline 的 3 个核心阶段  
1. PostgreSQL Table Schema Design | PostgreSQL 数据库表结构设计  
   Table comments, column comments, and data types are all defined in PostgreSQL schema, and column comments are maintained with the database as the canonical source.  
   所有表注释、字段注释和数据类型都在 PostgreSQL 表结构中定义，字段注释以数据库为准并统一维护。  

2. Generate Java Code by MyBatis Generator | 由 MyBatis Generator 生成 Java 代码  
   MyBatisGenerator.java can generate Model classes, Mapper interfaces, and Mapper XML files automatically from database schema.  
   MyBatisGenerator.java 可根据数据库表结构自动生成 Model（实体类）、Mapper（接口）和 Mapper XML（SQL 映射文件）。  
   With CustomCommentGenerator.java configured, generated Models and Mappers include table and column comments compatible with SpringDoc @Schema.  
   通过 CustomCommentGenerator.java 的配置，生成的 Model 和 Mapper 会带有表与字段注释，并符合 SpringDoc 的 @Schema 规范。  

3. Copy MyBatis Model to DTO | 复制 MyBatis Model 为 DTO  
   MyBatis model can be copied to DTO (for HTTP POST request body), and because fields already contain @Schema metadata, generated OpenAPI (Swagger) docs include field comments directly.  
   可以将 MyBatis 的 model 复制为 DTO（HTTP POST 入参）；由于字段已包含 @Schema 元数据，生成的 OpenAPI（Swagger）文档会直接带出字段注释。  

### Three Optional Stages in Pipeline | Pipeline 的 3 个可选阶段  
1. Add Tested JSON to Swagger Description | 将测试过的 JSON 放入 Swagger 描述  
   You can paste tested response JSON into Controller "Swagger(@Operation) description", and it will be shown in Swagger UI.  
   你可以将测试过的响应 JSON 粘贴到 Controller 的 "Swagger(@Operation) description" 中，随后会显示在 Swagger UI。  

2. Convert JSON to TypeScript Object | 将 JSON 转换为 TypeScript 对象  
   You may use JSON-to-TypeScript tools to transform response JSON into frontend TypeScript objects.  
   你也可以使用 JSON 转 TypeScript 工具将响应 JSON 转换为前端 TypeScript 对象。  
   - https://transform.tools/json-to-typescript  
   - https://quicktype.io/typescript  
   - AI Copilot tools (e.g., GitHub Copilot)  

3. Import OpenAPI JSON into API Tools | 将 OpenAPI JSON 导入 API 工具  
   You can import the exported OpenAPI JSON configuration into tools such as Postman or Apifox.  
   你可以将导出的 OpenAPI JSON 配置文件导入 Postman 或 Apifox 等工具。  
   - Import to Postman | 导入到 Postman  
     ![import_to_postman.png](document_images/pipelined_model_code_generation/import_to_postman.png)  
   - Import to Apifox | 导入到 Apifox  
     ![import_to_apifox.png](document_images/pipelined_model_code_generation/import_to_apifox.png)  
