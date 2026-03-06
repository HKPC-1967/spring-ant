## 流水線（Pipeline）式模型程式碼生成  
這是一種從 PostgreSQL 資料庫表結構到 MyBatis Generator 程式碼，再到 Controller 的 HTTP POST DTO，最後到 SpringDoc 註解的流水線式模型程式碼生成方法。  
它可以幫助開發者更快開發、統一維護並減少重複工作。  

### 設計理念  
- 相比 Hibernate（全 ORM 框架），我們選擇 MyBatis（半 ORM 框架），因為它在靈活性、可控性和原生 SQL 友好性方面更好，新人也更快上手。  
- Pipeline 思想在軟件工程中很常見：一個任務的輸出成為下一個任務的輸入，從而形成流水線。  
- 該方法並非全自動，而是有意設計成半自動：機器處理大部分重複工作，人工保留靈活決策空間。  
- 一個關鍵理念是以資料庫表註釋作為單一事實來源，在一個地方維護元數據，再傳播到 Model、Mapper、DTO 和 API 文檔。  

### 程式碼實現細節  
[MybatisGenerator.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/generator/MybatisGenerator.java)  
[generator-configuration.xml](../src/main/resources/generator-configuration.xml)  
[CustomCommentGenerator.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/generator/CustomCommentGenerator.java)  
[OpenApiConfig.java](./../src/main/java/org/hkpc/dtd/common/config/OpenApiConfig.java)  

### Pipeline 的 3 個核心階段  
1. PostgreSQL 資料庫表結構設計  
   所有表註釋、字段註釋和數據類型都在 PostgreSQL 表結構中定義，字段註釋以資料庫為準並統一維護。  

2. 由 MyBatis Generator 生成 Java 程式碼  
   MyBatisGenerator.java 可根據資料庫表結構自動生成 Model（實體類）、Mapper（接口）和 Mapper XML（SQL 映射文件）。  
   通過 CustomCommentGenerator.java 的配置，生成的 Model 和 Mapper 會帶有表與字段註釋，並符合 SpringDoc 的 `@Schema` 規範。  

3. 複製 MyBatis Model 為 DTO  
   可以將 MyBatis 的 model 複製為 DTO（HTTP POST 入參）；由於字段已包含 `@Schema` 元數據，生成的 OpenAPI（Swagger）文檔會直接帶出字段註釋。  

### Pipeline 的 3 個可選階段  
1. 將測試過的 JSON 放入 Swagger 描述  
   你可以將測試過的回應 JSON 貼到 Controller 的 `Swagger(@Operation) description` 中，隨後會顯示在 Swagger UI。  

2. 將 JSON 轉換為 TypeScript 對象  
   你也可以使用 JSON 轉 TypeScript 工具將回應 JSON 轉換為前端 TypeScript 對象。  
   - https://transform.tools/json-to-typescript  
   - https://quicktype.io/typescript  
   - AI Copilot 工具（如 GitHub Copilot）  

3. 將 OpenAPI JSON 匯入 API 工具  
   你可以將匯出的 OpenAPI JSON 配置文件匯入 Postman 或 Apifox 等工具。  
   - 匯入到 Postman  
     ![import_to_postman.png](document_images/pipelined_model_code_generation/import_to_postman.png)  
   - 匯入到 Apifox  
     ![import_to_apifox.png](document_images/pipelined_model_code_generation/import_to_apifox.png)  

