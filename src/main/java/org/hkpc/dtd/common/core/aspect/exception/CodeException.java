package org.hkpc.dtd.common.core.aspect.exception;

import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;

public class CodeException extends Exception {

    private final ErrorCodeEnum codeEnum;
//    private ErrorShowTypeEnum errorShowTypeEnum;

    public CodeException(ErrorCodeEnum codeEnum) {
        super(String.valueOf(codeEnum));
        this.codeEnum = codeEnum;
    }

    /**
     * 可以把其它异常包进CodeException，其异常也会打印出来
     */
    public CodeException(ErrorCodeEnum codeEnum, Exception cause) {
        super(codeEnum.getCode() + cause.getMessage(), cause);
        this.codeEnum = codeEnum;
    }

    public ErrorCodeEnum getCodeEnum() {
        return codeEnum;
    }

//    public CodeException(CodeEnum codeEnum, Exception cause, ErrorShowTypeEnum errorShowTypeEnum) {
//        super(codeEnum.getCode() + cause.getMessage(), cause);
//        this.codeEnum = codeEnum;
//        this.errorShowTypeEnum = errorShowTypeEnum;
//    }

//    public ErrorShowTypeEnum getErrorShowTypeEnum() {
//        return errorShowTypeEnum;
//    }
}
