## 1. 功能细节
- RBAC（基于角色的访问控制）, CORS（跨域资源共享）: [SpringSecurityConfig.java](./../src/main/java/org/hkpc/dtd/common/core/security/SpringSecurityConfig.java)
- Aspect切面 (统一 API 格式, 日志, 和 error 处理): [MainAspect.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/MainAspect.java)  [ErrorCodeEnum.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/enums/ErrorCodeEnum.java)  [GlobalExceptionHandler.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/handler/GlobalExceptionHandler.java)    
  以 JSON 格式记录请求和响应信息，便于接入 ELK、AWS CloudWatch 等日志分析平台。  
  包含 `MARK_ALARM_SYSTEM` 的日志会被设计用于在 Log Analysis Platform 中触发告警通知（邮件、短信等）。

- [流水线式模型代码生成](./pipelined_model_code_generation.zh-CN.md)
- JWT: [UserJwtSubject.java](../src/main/java/org/hkpc/dtd/common/core/jwt/model/UserJwtSubject.java)  [JwtValidateComponent.java](./../src/main/java/org/hkpc/dtd/common/core/jwt/JwtValidateComponent.java)  [JwtAuthenticationFilter.java](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java)  
- 分页: [PageHelper](./../src/main/java/org/hkpc/dtd/business/demo/service/impl/DemoRuleServiceImpl.java)
- 文件上传和下载: [FileController.java](./../src/main/java/org/hkpc/dtd/business/demo/controller/FileController.java)  [test_file_upload_with_postman.md](test_file_upload_with_postman.md)
- 工具类: [AesUtil.java](./../src/main/java/org/hkpc/dtd/common/utils/AesUtil.java)  [RedactUtil.java](./../src/main/java/org/hkpc/dtd/common/utils/RedactUtil.java)  [UuidUtil.java](./../src/main/java/org/hkpc/dtd/common/utils/UuidUtil.java)  [UuidTypeHandler.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/handler/UuidTypeHandler.java)  

## 2. [AGENTS.md](../AGENTS.md)
AGENTS.md是用来指导AI编码代理的，包括一些基本的知识，可以帮助AI代理在这个代码库中立即变得高效。

## 3. Framework Design philosophy 框架设计理念
- 这个框架的设计理念是轻量，原生，对扩展开放。 它结构清晰，经过测试，定义好了核心功能如API，错误码和安全，一个实习生在有经验的开发者的监督下也都可以很快上手编写可维护的业务代码。 我们主要实现了核心功能以及和Ant Design Pro的对接，您可以在此基础上根据自己的需求进行扩展和定制，框架好后，剩下的就是业务代码了。
- 这个Spring Ant Family最适合全栈开发者，这也是我们从原来的Ant Design Pro中移除Data Mock部分的原因，因为这个额外的步骤会让我们在全栈开发实践中的开发速度变慢。

## 4. 对象类型
- DTO (Data Transfer Object/ Request Object).   
  用途：用于在系统的不同部分之间传输数据，例如在客户端和服务器之间。  
  我们将它用作HTTP POST请求体的映射模型。

- VO (View Object / Response Object).  
  用途：用于Controller返回给前端的HTTP响应对象，只保留接口输出所需且可对外暴露的字段。

- Model (MyBatis Model / Database Object).  
  用途：由MyBatis Generator生成，用于数据库交互（DAO/Mapper层），对应数据库表结构，不直接暴露给前端。

#### Controller响应VO过滤
在Controller返回前多做一层转换。将数据库Model/Entity（`Contact`）转换为响应VO（`ContactVO`），只保留允许返回给前端的字段，不要直接返回Model。  

```java
// 有用的代码片段
@GetMapping("/contacts")
public List<ContactVO> listContacts() {
  List<Contact> contacts = contactRepository.findAllWithCompany();

  // 在Controller返回边界多做一层：过滤并重组响应字段
  return contacts.stream().map(this::toContactRecord).toList();
}

private ContactVO toContactRecord(Contact contact) {
  return new ContactVO(
          contact.getId(),
          contact.getFirstName(),
          contact.getLastName(),
          contact.getEmail(),
          new CompanyRecord(
                  contact.getCompany().getId(),
                  contact.getCompany().getName()
          )
  );
}
```
- 为什么这样做：
  - 隐藏敏感字段（例如密码哈希、手机号、证件号、内部状态）。
  - 即使数据库结构变化，也能保持API返回结构稳定。
  - 避免前端接口和数据库持久化模型强耦合。
- 实践建议：Model用于数据库读写，DTO/VO用于接口请求/响应。Controller是最清晰的“进站/出站过滤点”。
- 但如果更符合你的业务需求并能提升开发效率，你仍然可以选择直接在SQL中定义返回字段。


