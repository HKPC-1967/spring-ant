#  数据库（PostgreSQL）
##  1. 安装 PostgreSQL   
PostgreSQL 的密码配置在 [application-dev.yml](../src/main/resources/application-dev.yml) 的 `spring.datasource` 下。   
你可以使用 Docker 通过以下命令安装 PostgreSQL：
```shell
docker run --name postgres18 -e POSTGRES_DB=postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:18
```

##  2. 创建表 
在 `postgres` 数据库中使用以下 SQL 脚本创建表：    
[tables.sql](sql/tables.sql)



## 3. 修改默认密钥（AES key 和 JWT key）

出于安全考虑，建议修改默认的 AES key 和 JWT key，但在本地测试时可以先跳过这一步。

### 1) 使用 [AesUtil.java](../src/main/java/org/hkpc/dtd/common/utils/AesUtil.java) 生成新密钥

运行 `org.hkpc.dtd.common.utils.AesUtil#main`。然后，方法 `generateNewAesKeyAndEncryptedJwtKey()` 会输出：

- 一个随机的 `aes_key`（长度为 16 个字符）
- 一个加密后的 `jwt_key`（使用生成的 `aes_key` 进行加密）
- 一段用于更新 `db_config.jwt_key` （包含加密后的 `jwt_key`）的 的 SQL

### 2) 替换 YML 中的 AES key

更新 [application-dev.yml](../src/main/resources/application-dev.yml) 中的 `project-config.db-config-aes-key`：

```yaml
project-config:
  db-config-aes-key: <new_aes_key_from_console>
```

### 3) 替换数据库中的加密 JWT key


付至控制台打印出的 SQL（如下demo），在数据库执行，从而替换数据库中的加密 `jwt_key` ：
英文翻译： 

```sql
UPDATE db_config
SET value = '<new_encrypted_jwt_key_from_console>',
		updated_at = CURRENT_TIMESTAMP
WHERE key = 'jwt_key';
```




