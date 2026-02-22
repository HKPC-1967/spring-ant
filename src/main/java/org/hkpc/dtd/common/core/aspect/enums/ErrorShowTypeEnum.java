package org.hkpc.dtd.common.core.aspect.enums;

public enum ErrorShowTypeEnum {
    SILENT(0),
    WARN_MESSAGE(1),
    ERROR_MESSAGE(2),
    NOTIFICATION(3),
    REDIRECT(9);

    private final int value;

    ErrorShowTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * from value (int) to Enum
     */
    public static ErrorShowTypeEnum fromValue(int value) {
        for (ErrorShowTypeEnum e : ErrorShowTypeEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }
}
