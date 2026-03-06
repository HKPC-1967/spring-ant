## Pipelined Model Code Generation  
This is a pipelined model code generation approach from PostgreSQL table schema to MyBatis Generator output, then to Controller HTTP POST DTO, and finally to SpringDoc annotations.  
It helps developers ship faster, maintain consistency in one place, and reduce repeated work.  

### Design Philosophy  
- Compared with Hibernate (a full ORM framework), we choose MyBatis (a semi-ORM framework) for better flexibility, stronger control, and more native SQL friendliness for newcomers.  
- The pipeline idea is common in software engineering: the output of one task becomes the input of the next task to form a workflow chain.  
- This method is not fully automatic; it is semi-automatic by design, where machines handle most repetitive work while humans keep room for flexible decisions.  
- A key principle is that database table comments are the single source of truth, so metadata is maintained in one place and propagated through Model, Mapper, DTO, and API docs.  

### Code Implementation Details   
[MybatisGenerator.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/generator/MybatisGenerator.java)  
[generator-configuration.xml](../src/main/resources/generator-configuration.xml)  
[CustomCommentGenerator.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/generator/CustomCommentGenerator.java)  
[OpenApiConfig.java](./../src/main/java/org/hkpc/dtd/common/config/OpenApiConfig.java)  

### Three Core Stages in Pipeline   
1. PostgreSQL Table Schema Design   
   Table comments, column comments, and data types are all defined in PostgreSQL schema, and column comments are maintained with the database as the canonical source.  

2. Generate Java Code by MyBatis Generator   
   MyBatisGenerator.java can generate Model classes, Mapper interfaces, and Mapper XML files automatically from database schema.  
   With CustomCommentGenerator.java configured, generated Models and Mappers include table and column comments compatible with SpringDoc @Schema.  

3. Copy MyBatis Model to DTO   
   MyBatis model can be copied to DTO (for HTTP POST request body), and because fields already contain @Schema metadata, generated OpenAPI (Swagger) docs include field comments directly.  

### Three Optional Stages in Pipeline   
1. Add Tested JSON to Swagger Description   
   You can paste tested response JSON into Controller "Swagger(@Operation) description", and it will be shown in Swagger UI.  

2. Convert JSON to TypeScript Object  
   You may use JSON-to-TypeScript tools to transform response JSON into frontend TypeScript objects.  
   - https://transform.tools/json-to-typescript  
   - https://quicktype.io/typescript  
   - AI Copilot tools (e.g., GitHub Copilot)  

3. Import OpenAPI JSON into API Tools   
   You can import the exported OpenAPI JSON configuration into tools such as Postman or Apifox.  
   - Import to Postman   
     ![import_to_postman.png](document_images/pipelined_model_code_generation/import_to_postman.png)  
   - Import to Apifox   
     ![import_to_apifox.png](document_images/pipelined_model_code_generation/import_to_apifox.png)  
