# 程式碼規範
我們沒有嚴格的程式碼規範，你可以定義適合你項目的程式碼規範。以下是我們有使用的一些規範，供參考。

### 0. IDEA 插件
推薦安裝如下 IDEA 插件。 
- MybatisX
- Lombok

### 1. 建議類和字段上使用多行註釋
類和字段上的建議用這種多行註釋，從而 IDEA 可以識別，當滑鼠懸浮在引用的類和字段上時，就可以看到其註釋了。
```java
/**
 *
 */
```

### 2. 相同的字段，要在一處定義，不要在不同地方重複定義相同字段。請嚴格遵守這一點以便維護。

### 3. 未嚴格實現 Restful 風格，只使用 POST（以及 GET，如有需要）從而簡化和統計接口設計
對於 GET，請使用 "?a=6&b=6"，不要用 "/a/6/b/6"，請查看 [MainAspect.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/MainAspect.java) 程式碼實現詳情。  

未嚴格實現 Restful 風格原因：
1. 內部 API 並不需要 restful，還更安全，可防止被黑客猜路徑
2. 統一全部使用 post，簡化整合前端開發、後端 aspect 和 controller、swagger
3. 很多程式員也用不好 Restful，不如統一 POST 簡單些


### 4. Controller 方法名命名規則：動詞 + 名詞（類似 MyBatis Generator 生成的名字）

增刪改查 CRUD (CREATE, READ, UPDATE and DELETE)

* READ（沒有副作用（只是查詢，不會修改數據）） </br>
    * Single selectByPrimaryKey, selectByXxx
    * Multi selectAll, selectAllByTime, selectAllByXxx, selectAllXxxByXxx
    * Page selectPage, selectPageByXxx, selectXxxPageByXxx
  
* CREATE（有副作用，會修改數據，code 注意冪等邏輯（即前端請求一次和多次對服務器的影響是否相同）） </br>
    * Single insert, insertXxx, insertSelective（不推薦，原因：一般不推薦這種通用的 business 業務層面的 HTTP insert 接口，否則某個 business 變化要改時，會影響其他的 business。推薦對外為不同 business 暴露不同的 business 接口。）
    * Multi batchInsert, batchInsertXxx
  
* UPDATE（有副作用，會修改數據，一般冪等） </br>
    * Single updateByPrimaryKey, updateByXxx, updateByPrimaryKeySelective（不推薦，原因同 insertSelective）
    * Multi batchUpdateByXxx
* DELETE（有副作用，會修改數據，一般冪等） </br>
    * Single deleteByPrimaryKey, deleteByXxx
    * Single batchDeleteByXxx
  
* Others，根據業務命名 </br>
    * xxxSchedule

### 5. Git commit 提交資訊規範  
| 我們建議使用和 Ant Design Pro 一樣的 git commit 提交資訊規範                                          |
|-----------------------------------------------------------------|
| 常見的 git commit 類型包括：                        |
| `feat`：新功能                                  |
| `fix`：修復 bug                                |
| `docs`：文檔變更                                 |
| `style`：程式碼格式（不影響功能，例如空格、分號等）                |
| `refactor`：程式碼重構（既不是新增功能，也不是修復 bug）          |
| `perf`：效能優化                                 |
| `test`：增加或修改測試                              |
| `build`：影響建構系統或外部依賴的更改（如 npm、gulp、webpack）  |
| `ci`：持續整合相關配置或腳本的更改                         |
| `chore`：其他不屬於上述類型的更改（如建構流程、輔助工具、依賴庫升級等）     |
| `revert`：回滾某個更早之前的提交                        |

### 6. 阿里巴巴 Java 程式碼規範
我們推薦參考阿里巴巴 Java 程式碼規範:   
https://github.com/alibaba/p3c

