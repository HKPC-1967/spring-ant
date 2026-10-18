## 未來發佈計劃
後續有較大的改動，或依賴有大版本更新時，我們會發佈 GitHub Release。

所以建議在 GitHub 上點 watch -> custom -> releases，這樣就能第一時間收到發佈 releases 的通知。

本倉庫已基於 **Java 25** 與 **Spring Boot 4**（版本見 `build.gradle`）。日常的小版本依賴升級與框架改進透過 Release 與 Pull Request 交付。


## 建議貢獻領域
1. **CI/CD** — 例如 GitHub Actions 執行 `compileJava`、`test`，以及可選的 Docker 映像構建。
2. **可觀測性** — Spring Boot Actuator（容器健康/就緒探針）、結構化日誌或指標接入。
3. **Redis 快取**以獲得更高效能。  
   例如，我們在註釋中提到 [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) 可以通過快取優化。我們尚未添加該功能，因為並非所有系統都需要如此高的效能，而且 Redis 不是該框架的核心特性。歡迎貢獻實作，我們也很樂意審核並合併到 beta 分支。
4. **測試** — 使用 MockMvc / `spring-security-test` 覆蓋登入、刷新令牌、RBAC 與統一 API 回應格式。
5. **配置規範** — 無密鑰的示例 YAML、生產環境憑據外置及相關文檔。


## 貢獻程式碼

我們歡迎任何形式的貢獻，無論是程式碼、文檔、測試還是其他方面的改進。

比如更新某個已經被標記為棄用的方法、修正錯別字、完善中英文文檔等，都是歡迎的。

請確保你 pull request 的程式碼是仔細測試過的，並且有中英文或英文註釋。

如果你有具體想法想和我們溝通，也歡迎 email dtd-ai-code@hkpc.org，說明你的想法和計劃貢獻的內容，我們會建立貢獻者微信群或 Discord 群，方便交流與協作。


## [程式碼規範](./code_convention.zh-HK.md)
