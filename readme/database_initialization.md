#  Database (PostgreSQL)
##  1. Install PostgreSQL   
The PostgreSQL password is configured under `spring.datasource` in [application-dev.yml](../src/main/resources/application-dev.yml).   
You can use Docker to install PostgreSQL using the following command:
```shell
docker run --name postgres18 -e POSTGRES_DB=postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:18
```

##  2. Create tables 
Create tables in the `postgres` database using the following SQL scripts:    
[tables.sql](sql/tables.sql)



## 3. Change default keys (AES key and JWT key)

For security reasons, you should change the default AES key and JWT key, but you can skip this step for local testing.

### 1) Generate new keys with [AesUtil.java](../src/main/java/org/hkpc/dtd/common/utils/AesUtil.java)

Run `org.hkpc.dtd.common.utils.AesUtil#main`. Then the method `generateNewAesKeyAndEncryptedJwtKey()` will print:

- a random `aes_key` (exactly 16 characters)
- an encrypted `jwt_key` (encrypted using the generated `aes_key`)
- the SQL with the encrypted `jwt_key` to update `db_config.jwt_key`

### 2) Replace AES key in YML

Update `project-config.db-config-aes-key` in [application-dev.yml](../src/main/resources/application-dev.yml):

```yaml
project-config:
  db-config-aes-key: <new_aes_key_from_console>
```

### 3) Replace encrypted JWT key in database

Use the SQL printed to the console (as shown in the demo below) and execute it in the database to replace the encrypted `jwt_key` in the database:

```sql
UPDATE db_config
SET value = '<new_encrypted_jwt_key_from_console>',
		updated_at = CURRENT_TIMESTAMP
WHERE key = 'jwt_key';
```




