package org.hkpc.dtd.common.service;


import org.hkpc.dtd.common.core.aspect.exception.CodeException;

public interface DbConfigService {

     enum KeyEnum {

         /**
          * Instead of storing the jwt key in yml file, we store it in DB, and encrypt it before storing, as this is more secure. <br>
          * This open source project only use PostgreSQL as the storage, you can use other more secure storage (e.g., xxx Vault) to store the jwt key for your own project.<br>
          * 中文：为了安全起见，我们将jwt key存储在数据库中，并在存储之前进行加密，而不是直接存储在yml文件中。<br>
          * 这个开源项目只使用PostgreSQL作为存储，你可以使用其他更安全的存储（例如xxx Vault）来存储jwt key以供你自己的项目使用。
          */
        JWT_KEY("jwt_key", true);

        private final String key;
        private final boolean isEncrypted;

        KeyEnum(String key, boolean isEncrypted) {
            this.key = key;
            this.isEncrypted = isEncrypted;
        }

        public String getKey() {
            return key;
        }

        public boolean isEncrypted() {
            return isEncrypted;
        }
    }

    String getValueByKey(KeyEnum keyEnum) throws CodeException;

}
