package org.hkpc.dtd.common.core.aspect.enums;

public enum ErrorCodeEnum {
    /**
     * Success
     */
    SUCCESS("0", "Success!", null),

    /**
     * System Error
     */
    SYSTEM_ERROR("1000", "Something unexpected happened (report code:1), try again later", ErrorShowTypeEnum.ERROR_MESSAGE),
    SYSTEM_ERROR_2("1001", "Something unexpected happened (report code:2), try again later", ErrorShowTypeEnum.ERROR_MESSAGE),
    SYSTEM_ERROR_3("1002", "Something unexpected happened (report code:3), try again later", ErrorShowTypeEnum.ERROR_MESSAGE),

    /**
     * Parameter Validater
     */
    PARAM_INVALID_BY_VALIDATOR("1011", "Parameter invalid", ErrorShowTypeEnum.ERROR_MESSAGE),
    PARAM_FORMAT_INVALID("1012", "Parameter format invalid", ErrorShowTypeEnum.ERROR_MESSAGE),
    INITIAL_PARAM_DB_ERROR("1013", "System parameter initialization error", ErrorShowTypeEnum.NOTIFICATION),
    JWT_PARAM_FORMAT_INVALID("1014", "JWT Token format invalid", ErrorShowTypeEnum.NOTIFICATION),
    HTTP_REQUEST_NOT_SUPPORTED("1015", "This HTTP request is not supported by Back-end", ErrorShowTypeEnum.NOTIFICATION),

    /**
     * Login
     */
    INVALID_USERNAME_PASS("1020", "Invalid username or password", ErrorShowTypeEnum.ERROR_MESSAGE),
    INVALID_CUSTOMER_TOKEN("1021", "Invalid token, please try re-login", ErrorShowTypeEnum.REDIRECT),
    INVALID_JWT_TOKEN("1022", "Invalid token, please try re-login", ErrorShowTypeEnum.REDIRECT),
    INVALID_JWT_TYPE("1023", "Invalid token type", ErrorShowTypeEnum.REDIRECT),

    /**
     * Business code
     */
//    PERMISSION_DENIED("2001", "Permission denied", ErrorShowTypeEnum.ERROR_MESSAGE),

    /**
     * test
     */
    TEST_SILENT_0("10000", "Application level error message : SILENT", ErrorShowTypeEnum.SILENT),
    TEST_WARN_MESSAGE_1("10001", "Application level error message : WARN_MESSAGE", ErrorShowTypeEnum.WARN_MESSAGE),
    TEST_ERROR_MESSAGE_2("10002", "Application level error message : ERROR_MESSAGE", ErrorShowTypeEnum.ERROR_MESSAGE),
    TEST_NOTIFICATION_3("10003", "Application level error message : NOTIFICATION", ErrorShowTypeEnum.NOTIFICATION),
    TEST_REDIRECT_9("10009", "Application level error message : REDIRECT", ErrorShowTypeEnum.REDIRECT);

    /**
     * error code, the front-end will use this code to find the corresponding message in the locales dictionary (internationalization (i18n)) and display it to users. <br>
     * 错误代码，前端会使用这个代码去 locales 字典（国际化 i18n）里找到对应的消息并显示给用户。
     */
    private final String code;
    /**
     * This message is only for developers to check with developer tools. The front-end will not display this message.<br>
     * The front-end displays the messages from the locales dictionary (internationalization (i18n)) and is based on the code returned by the back-end.<br>
     * 此消息仅供开发人员通过开发者工具查看。前端不会显示此消息。<br>
     * 前端显示的消息来自本地化字典（国际化 i18n），并且是基于后端返回的 code。
     */
    private final String message;
    private final ErrorShowTypeEnum errorShowTypeEnum;

    ErrorCodeEnum(String code, String message, ErrorShowTypeEnum errorShowTypeEnum) {
        this.code = code;
        this.message = message;
        this.errorShowTypeEnum = errorShowTypeEnum;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorShowTypeEnum getErrorShowTypeEnum() {
        return errorShowTypeEnum;
    }
}
