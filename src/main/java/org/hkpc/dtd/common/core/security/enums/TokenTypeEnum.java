package org.hkpc.dtd.common.core.security.enums;

public enum TokenTypeEnum {
    /**
     * access token
     */
    ACCESS_TOKEN(1),

    /**
     * refresh token
     */
    REFRESH_TOKEN(2);

    private final int value;

    TokenTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
