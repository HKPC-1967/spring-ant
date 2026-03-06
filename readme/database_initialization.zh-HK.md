#  資料庫（PostgreSQL）
##  1. 安裝 PostgreSQL   
PostgreSQL 的密碼配置在 [application-dev.yml](../src/main/resources/application-dev.yml) 的 `spring.datasource` 下。   
你可以使用 Docker 通過以下命令安裝 PostgreSQL：
```shell
docker run --name postgres18 -e POSTGRES_DB=postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:18
```

##  2. 建立表 
在 `postgres` 資料庫中使用以下 SQL 腳本建立表：    
[tables.sql](sql/tables.sql)



## 3. 修改預設密鑰（AES key 和 JWT key）

出於安全考慮，建議修改預設的 AES key 和 JWT key，但在本地測試時可以先跳過這一步。

### 1) 使用 [AesUtil.java](../src/main/java/org/hkpc/dtd/common/utils/AesUtil.java) 生成新密鑰

運行 `org.hkpc.dtd.common.utils.AesUtil#main`。然後，方法 `generateNewAesKeyAndEncryptedJwtKey()` 會輸出：

- 一個隨機的 `aes_key`（長度為 16 個字符）
- 一個加密後的 `jwt_key`（使用生成的 `aes_key` 進行加密）
- 一段用於更新 `db_config.jwt_key`（包含加密後 `jwt_key`）的 SQL

### 2) 替換 YML 中的 AES key

更新 [application-dev.yml](../src/main/resources/application-dev.yml) 中的 `project-config.db-config-aes-key`：

```yaml
project-config:
  db-config-aes-key: <new_aes_key_from_console>
```

### 3) 替換資料庫中的加密 JWT key


複製控制台打印出的 SQL（如下 demo），在資料庫執行，從而替換資料庫中的加密 `jwt_key`：

```sql
UPDATE db_config
SET value = '<new_encrypted_jwt_key_from_console>',
		updated_at = CURRENT_TIMESTAMP
WHERE key = 'jwt_key';
```

