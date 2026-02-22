package org.hkpc.dtd.common.service.impl;

import org.hkpc.dtd.common.consts.CommonConst;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.hkpc.dtd.common.service.DbConfigService;
import org.hkpc.dtd.common.utils.AesUtil;
import org.hkpc.dtd.component.postgres.dao.DbConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class DbConfigServiceImpl implements DbConfigService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${project-config.db-config-aes-key}")
    private String DB_CONFIG_AES_KEY;

    private final Map<String, String> singletonParamsContainer = new HashMap<>(4);

    private final DbConfigMapper dbConfigMapper;

    @Autowired
    public DbConfigServiceImpl(DbConfigMapper dbConfigMapper) {
        this.dbConfigMapper = dbConfigMapper;
    }

    @Override
    public String getValueByKey(KeyEnum keyEnum) throws CodeException {
        return getValueByKeySingleton(keyEnum.getKey(), keyEnum.isEncrypted());
    }

    /**
     * @param key the key of the key-value config pair in DB
     * @param isEncrypted  we should encrypt the sensitive value first, then save to DB
     * @return the decrypted value
     * @throws CodeException
     */
    private String getValueByKeySingleton(String key,Boolean isEncrypted) throws CodeException {
        String value = singletonParamsContainer.get(key);

        if (!StringUtils.hasLength(value)) {
            value = getValueByKeyFromDB(key);
            if(isEncrypted){
                value = AesUtil.decrypt(value, DB_CONFIG_AES_KEY);
            }
            singletonParamsContainer.put(key, value);
            // to avoid the sensitive key-value data in log, so only log part of the value
            logger.info("singletonParamsContainer put {},{},{}", key, value.length(),value.substring(0, Math.min(6, value.length())));
        }

        return value;
    }

    private String getValueByKeyFromDB(String key) throws CodeException {
        String value = dbConfigMapper.selectByKey(key);
        if (!StringUtils.hasLength(value)) {
            logger.error(CommonConst.MARK_ALARM_CUSTOM, "{} is null", key);
            throw new CodeException(ErrorCodeEnum.INITIAL_PARAM_DB_ERROR);
        }
        return value;
    }
}
