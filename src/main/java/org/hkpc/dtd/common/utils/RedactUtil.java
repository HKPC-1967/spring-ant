package org.hkpc.dtd.common.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.PostConstruct;
import org.hkpc.dtd.common.core.aspect.MainAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redacts sensitive values from headers/query/body based on configurable key lists.
 * 根据可配置的关键字列表对 header/query/body 中的敏感值进行脱敏。
 */
@Component
@ConfigurationProperties(prefix = "project-config.redact")
public class RedactUtil {
    /**
     * Logger for startup and debugging messages.
     * 用于启动和调试日志。
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Replacement text for redacted values.
     * 脱敏值的替换文本。
     */
    private static final String REDACTED = "[REDACTED]";

    /**
     * Keys for header redaction (case-insensitive, exact match).
     * Header 脱敏关键字（大小写不敏感，完全匹配）。
     */
    private Set<String> headerKeys = Collections.emptySet();
    /**
     * Keys for query-string redaction (case-insensitive, exact match).
     * Query-string 脱敏关键字（大小写不敏感，完全匹配）。
     */
    private Set<String> queryKeys = Collections.emptySet();
    /**
     * Keys for body redaction (case-insensitive, exact match).
     * Body 脱敏关键字（大小写不敏感，完全匹配）。
     */
    private Set<String> bodyKeys = Collections.emptySet();

    /**
     * Bind header keys from `project-config.redact.header-keys`.
     * 绑定 `project-config.redact.header-keys` 的配置。
     */
    public void setHeaderKeys(List<String> headerKeys) {
        this.headerKeys = normalizeKeys2lowerCase(headerKeys);
    }

    /**
     * Bind query keys from `project-config.redact.query-keys`.
     * 绑定 `project-config.redact.query-keys` 的配置。
     */
    public void setQueryKeys(List<String> queryKeys) {
        this.queryKeys = normalizeKeys2lowerCase(queryKeys);
    }

    /**
     * Bind body keys from `project-config.redact.body-keys`.
     * 绑定 `project-config.redact.body-keys` 的配置。
     */
    public void setBodyKeys(List<String> bodyKeys) {
        this.bodyKeys = normalizeKeys2lowerCase(bodyKeys);
    }

    @PostConstruct
    public void init() {
        // Log active redaction rules for debugging and audits.
        // 记录当前启用的脱敏规则，便于调试和审计。
        logger.info("RedactUtil initialized with headerKeys: {}, queryKeys: {}, bodyKeys: {}", headerKeys, queryKeys, bodyKeys);
    }

    /**
     * Redact header values by configured header keys.
     * 根据 header 关键字脱敏 header 值。
     */
    public Map<String, String> redactHeadersMap(Map<String, String> input) {
        return redactMap(input, headerKeys);
    }

    /**
     * Redact query parameter values by configured query keys.
     * 根据 query 关键字脱敏查询参数值。
     */
    public Map<String, String> redactQueryMap(Map<String, String> input) {
        return redactMap(input, queryKeys);
    }

    /**
     * Redact raw query string like "a=1&token=xxx".
     * 脱敏形如 "a=1&token=xxx" 的原始 query 字符串。
     */
    public String redactQueryString(String query) {
        if (queryKeys.isEmpty() || query == null || query.isBlank() || MainAspect.FLAG_NOT_PARSED.equals(query)) {
            return query;
        }
        if (!query.contains("=") && !query.contains("&")) {
            return query;
        }
        StringBuilder redacted = new StringBuilder();
        String[] pairs = query.split("&");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            int idx = pair.indexOf('=');
            String key = idx >= 0 ? pair.substring(0, idx) : pair;
            String value = idx >= 0 ? pair.substring(idx + 1) : "";
            if (isSensitiveKey(key, queryKeys)) {
                value = REDACTED;
            }
            if (i > 0) {
                redacted.append("&");
            }
            redacted.append(key);
            if (idx >= 0) {
                redacted.append("=").append(value);
            }
        }
        return redacted.toString();
    }

    /**
     * Redact body params when the body is JSON or key-value text.
     * 当 body 为 JSON 或键值对文本时进行脱敏。
     */
    public String redactBodyParams(String bodyParams) {
        if (bodyKeys.isEmpty() || bodyParams == null || bodyParams.isBlank() || MainAspect.FLAG_NOT_PARSED.equals(bodyParams)) {
            return bodyParams;
        }

        try {
            Object parsed = JSON.parse(bodyParams);
            Object redacted = redactObject(parsed, bodyKeys);
            return JSON.toJSONString(redacted);
        } catch (Exception ex) {
            logger.error("Failed to parse bodyParams as JSON for redaction. bodyParams: {}, error: {}", bodyParams, ex.getMessage());
//            return redactKeyValueString(bodyParams, bodyKeys);
            return bodyParams;
        }
    }




    /**
     * Redact values in a map with a given key list.
     * 使用指定关键字列表对 Map 进行脱敏。
     */
    private Map<String, String> redactMap(Map<String, String> input, Set<String> sensitiveKeys) {
        if (sensitiveKeys.isEmpty() || input == null || input.isEmpty()) {
            return input;
        }
        Map<String, String> redacted = new HashMap<>();
        for (Map.Entry<String, String> entry : input.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (isSensitiveKey(key, sensitiveKeys)) {
                redacted.put(key, REDACTED);
            } else {
                redacted.put(key, value);
            }
        }
        return redacted;
    }


    /**
     * Recursively redact objects (Map/List/Array) by key name.
     * 递归对对象（Map/List/Array）按字段名进行脱敏。
     */
    private Object redactObject(Object obj, Set<String> sensitiveKeys) {
        if (obj instanceof Map<?, ?> map) {
            Map<Object, Object> redacted = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key instanceof String keyStr && isSensitiveKey(keyStr, sensitiveKeys)) {
                    redacted.put(key, REDACTED);
                } else {
                    redacted.put(key, redactObject(value, sensitiveKeys));
                }
            }
            return redacted;
        }
        if (obj instanceof Iterable<?> iterable) {
            List<Object> redactedList = new ArrayList<>();
            for (Object item : iterable) {
                redactedList.add(redactObject(item, sensitiveKeys));
            }
            return redactedList;
        }
        if (obj != null && obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            List<Object> redactedList = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                redactedList.add(redactObject(Array.get(obj, i), sensitiveKeys));
            }
            return redactedList;
        }
        return obj;
    }

    /**
     * Case-insensitive exact match for sensitive keys.
     * 大小写不敏感的完全匹配判断。
     */
    private boolean isSensitiveKey(String key, Set<String> sensitiveKeys) {
        if (sensitiveKeys.isEmpty() || key == null) {
            return false;
        }
        String keyLowerCase = key.toLowerCase(Locale.ROOT);
        return sensitiveKeys.contains(keyLowerCase);
/*        //uncomment this if you want substring match
        for (String sensitiveKey : sensitiveKeys) {
            if (keyLowerCase.contains(sensitiveKey)) {
                return true;
            }
        }*/
    }

    /**
     * Normalize keys to lower-case and remove blanks.
     * 将关键字统一为小写并过滤空值。
     */
    private Set<String> normalizeKeys2lowerCase(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptySet();
        }
        return keys.stream()
                .filter(key -> key != null && !key.isBlank())
                .map(key -> key.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }
}
