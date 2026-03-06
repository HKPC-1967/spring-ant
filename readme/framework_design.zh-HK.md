## 1. 功能細節
- RBAC（基於角色的存取控制）, CORS（跨域資源共享）: [SpringSecurityConfig.java](./../src/main/java/org/hkpc/dtd/common/core/security/SpringSecurityConfig.java)
- Aspect 切面（統一 API 格式、日誌、和 error 處理）: [MainAspect.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/MainAspect.java)  [ErrorCodeEnum.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/enums/ErrorCodeEnum.java)  [GlobalExceptionHandler.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/handler/GlobalExceptionHandler.java)    
  以 JSON 格式記錄請求和回應資訊，便於接入 ELK、AWS CloudWatch 等日誌分析平台。  
  包含 `MARK_ALARM_SYSTEM` 的日誌會被設計用於在 Log Analysis Platform 中觸發告警通知（郵件、短訊等）。

- [流水線式模型代碼生成](./pipelined_model_code_generation.zh-HK.md)
- JWT: [UserJwtSubject.java](../src/main/java/org/hkpc/dtd/common/core/jwt/model/UserJwtSubject.java)  [JwtValidateComponent.java](./../src/main/java/org/hkpc/dtd/common/core/jwt/JwtValidateComponent.java)  [JwtAuthenticationFilter.java](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java)  
- 分頁: [PageHelper](./../src/main/java/org/hkpc/dtd/business/demo/service/impl/DemoRuleServiceImpl.java)
- 文件上傳和下載: [FileController.java](./../src/main/java/org/hkpc/dtd/business/demo/controller/FileController.java)  [test_file_upload_with_postman.md](test_file_upload_with_postman.md)
- 工具類: [AesUtil.java](./../src/main/java/org/hkpc/dtd/common/utils/AesUtil.java)  [RedactUtil.java](./../src/main/java/org/hkpc/dtd/common/utils/RedactUtil.java)  [UuidUtil.java](./../src/main/java/org/hkpc/dtd/common/utils/UuidUtil.java)  [UuidTypeHandler.java](./../src/main/java/org/hkpc/dtd/component/postgres/mybatis/handler/UuidTypeHandler.java)  

## 2. [AGENTS.md](../AGENTS.md)
AGENTS.md 是用來指導 AI 編碼代理的，包括一些基本知識，可以幫助 AI 代理在這個代碼庫中立即變得高效。

## 3. Framework Design philosophy 框架設計理念
- 這個框架的設計理念是輕量、原生、對擴展開放。它結構清晰，經過測試，定義好了核心功能如 API、錯誤碼和安全；即使是實習生，在有經驗的開發者監督下，也可以很快上手編寫可維護的業務代碼。我們主要實現了核心功能以及和 Ant Design Pro 的對接，你可以在此基礎上根據自己的需求進行擴展和定製，框架搭好後，剩下的就是業務代碼了。
- 這個 Spring Ant Family 最適合全棧開發者，這也是我們從原來的 Ant Design Pro 中移除 Data Mock 部分的原因，因為這個額外步驟會讓我們在全棧開發實踐中的開發速度變慢。

## 4. 對象類型
- DTO (Data Transfer Object/ Request Object).   
  用途：用於在系統的不同部分之間傳輸數據，例如在客戶端和服務器之間。  
  我們將它用作 HTTP POST 請求體的映射模型。

- VO (View Object / Response Object).  
  用途：用於 Controller 返回給前端的 HTTP 回應對象，只保留接口輸出所需且可對外暴露的字段。

- Model (MyBatis Model / Database Object).  
  用途：由 MyBatis Generator 生成，用於資料庫互動（DAO/Mapper 層），對應資料庫表結構，不直接暴露給前端。

#### Controller 回應 VO 過濾
在 Controller 返回前多做一層轉換。將資料庫 Model/Entity（`Contact`）轉換為回應 VO（`ContactVO`），只保留允許返回給前端的字段，不要直接返回 Model。  

```java
// 有用的代碼片段
@GetMapping("/contacts")
public List<ContactVO> listContacts() {
  List<Contact> contacts = contactRepository.findAllWithCompany();

  // 在Controller返回邊界多做一層：過濾並重組回應字段
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
- 為什麼這樣做：
  - 隱藏敏感字段（例如密碼哈希、手機號、證件號、內部狀態）。
  - 即使資料庫結構變化，也能保持 API 返回結構穩定。
  - 避免前端接口和資料庫持久化模型強耦合。
- 實踐建議：Model 用於資料庫讀寫，DTO/VO 用於接口請求/回應。Controller 是最清晰的「進站/出站過濾點」。
- 但如果更符合你的業務需求並能提升開發效率，你仍然可以選擇直接在 SQL 中定義返回字段。


