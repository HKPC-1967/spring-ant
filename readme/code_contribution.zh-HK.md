## 未來發佈計劃
後續有較大的改動，或依賴有大版本更新時，我們會發佈 GitHub Release。  

所以建議在 GitHub 上點 watch -> custom -> releases，這樣就能第一時間收到發佈 releases 的通知。  

我們計劃後續會更新為 Java 25 以及 Spring Boot 4（我們也歡迎社群貢獻代碼）。  

暫時使用上一個大版本的最新小版本的好處是：
1. 穩定性
2. 相容性
3. 更好的 AI Vibe Coding 支援，因為 LLM 是用過往知識訓練的，其本身不熟悉最新版本的特性和語法。

## 建議貢獻領域
1. 升級到 Java 25 和 Spring Boot 4.
2. 增加 Redis 快取以獲得更高效能。  
   例如，我們在註釋中提到 [JwtAuthenticationFilter](../src/main/java/org/hkpc/dtd/common/core/security/filter/JwtAuthenticationFilter.java) 可以通過快取優化。我們尚未添加該功能，因為並非所有系統都需要如此高的效能，而且 Redis 不是該框架的核心特性。歡迎貢獻實作，我們也很樂意審核並合併到 beta 分支。

## 貢獻代碼

我們歡迎任何形式的貢獻，無論是代碼、文檔、測試還是其他方面的改進。

比如更新某個已經被標記為棄用的方法、修正錯別字、完善中英文文檔等，都是歡迎的。

請確保你 pull request 的代碼是仔細測試過的，並且有中英文或英文註釋。

如果你有具體想法想和我們溝通，也歡迎 email dtd-ai-code@hkpc.org，說明你的想法和計劃貢獻的內容，我們會建立貢獻者微信群或 Discord 群，方便交流與協作。


## [代碼規範](./code_convention.zh-HK.md)

