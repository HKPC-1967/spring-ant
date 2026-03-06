# 代码规范
我们没有严格的代码规范，您可以定义适合您项目的代码规范。 以下是我们有使用的一些规范，供参考。

### 0. IDEA 插件
推荐安装如下 IDEA 插件. 
- MybatisX
- Lombok

### 1. 建议类和字段上使用多行注释
类和字段上的建议用这种多行注释，从而IDEA可以识别，鼠标悬浮在引用的类和字段上时,就可以看到其注释了。
```java
/**
 *
 */
```

### 2. 同样的字段，要在一处定义，不要在不同的地方重复定义同样的字段。请严格遵守这一点以便维护。

### 3. 未严格实现Restful风格, 只使用 POST (以及 GET, 如有需要) 从而简化和统计接口设计
对于 GET, 请使用 "?a=6&b=6", 不要用 "/a/6/b/6", 请查看 [MainAspect.java](./../src/main/java/org/hkpc/dtd/common/core/aspect/MainAspect.java) 代码实现详情.  

未严格实现Restful风格原因:
1. 内部API并不需要restful,还更安全防止被黑客猜路径
2. 统一全是post,简化integrate前端开发，后端aspect和controller, swagger
3. 很多程序员也用不对Restful，不如统一POST简单点


### 4. Controller方法名命名規則：動詞 + 名詞 (類似MyBatis Generator生成的名字)

增删改查 CRUD (CREATE, READ, UPDATE and DELETE)

* READ (沒有副作用（只是查詢, 不會修改數據）) </br>
    * Single selectByPrimaryKey, selectByXxx
    * Multi selectAll, selectAllByTime, selectAllByXxx, selectAllXxxByXxx
    * Page selectPage, selectPageByXxx, selectXxxPageByXxx
  
* CREATE (有副作用，會修改數據, code注意冪等邏輯(即前端請求一次和多次對服務器的影響是否相同)) </br>
    * Single insert, insertXxx, insertSelective (不推荐，原因: 一般不推薦这种通用的business业务层面的HTTP insert接口，否则某个business变化要改时，会影响其他的business。推荐對外不同business暴露不同的business接口。)
    * Multi batchInsert, batchInsertXxx
  
* UPDATE (有副作用，會修改數據,一般冪等) </br>
    * Single updateByPrimaryKey, updateByXxx, updateByPrimaryKeySelective (不推荐，原因同 insertSelective)
    * Multi batchUpdateByXxx
* DELETE (有副作用，會修改數據,一般冪等) </br>
    * Single deleteByPrimaryKey, deleteByXxx
    * Single batchDeleteByXxx
  
* Others, 根据业务命名 </br>
    * xxxSchedule

### 5. Git commit 提交信息规范  
| 我们建议用同 Ant Design Pro 一样的 git commit 提交信息规范                                          |
|-----------------------------------------------------------------|
| 常见的 git commit 类型包括：                        |
| `feat`：新功能                                  |
| `fix`：修复 bug                                |
| `docs`：文档变更                                 |
| `style`：代码格式（不影响功能，例如空格、分号等）                |
| `refactor`：代码重构（既不是新增功能，也不是修复 bug）          |
| `perf`：性能优化                                 |
| `test`：增加或修改测试                              |
| `build`：影响构建系统或外部依赖的更改（如 npm、gulp、webpack）  |
| `ci`：持续集成相关配置或脚本的更改                         |
| `chore`：其他不属于上述类型的更改（如构建流程、辅助工具、依赖库升级等）     |
| `revert`：回滚某个更早之前的提交                        |

### 6. 阿里巴巴 Java 代码规范
我们推荐参考阿里巴巴Java代码规范:   
https://github.com/alibaba/p3c
