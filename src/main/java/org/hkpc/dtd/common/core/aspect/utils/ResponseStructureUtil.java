package org.hkpc.dtd.common.core.aspect.utils;


import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import lombok.Data;

public class ResponseStructureUtil {

    @Data
    public static class ResponseStructure {
        private boolean success;
        private String errorCode;

        private Object data;

        private String errorMessage;
        private int showType;

        // for debug message to front end
        private String detail;
    }

    public static ResponseStructure getSuccessResponse(Object data) {
        ResponseStructure responseStructure = getResponseStructure(ErrorCodeEnum.SUCCESS);
        // only success response may have data
        if (data != null) {
            responseStructure.setData(data);
        }
        return responseStructure;
    }

    public static ResponseStructure getResponse(ErrorCodeEnum codeEnum) {
        ResponseStructure responseStructure = getResponseStructure(codeEnum);
        responseStructure.setErrorMessage(codeEnum.getMessage());
        responseStructure.setShowType(codeEnum.getErrorShowTypeEnum().getValue());
        return responseStructure;
    }

    /**
     * in case you want to add some debug message to return to front end.
     * you can change to {@link #getResponse} when you don't need the debug message anymore.
     */
    public static ResponseStructure getResponseWithDebugMessage(ErrorCodeEnum codeEnum, String message) {
        ResponseStructure responseStructure = getResponse(codeEnum);

        responseStructure.setDetail(message);
        return responseStructure;
    }

    private static ResponseStructure getResponseStructure(ErrorCodeEnum codeEnum) {
        ResponseStructure responseStructure = new ResponseStructure();
        responseStructure.setErrorCode(codeEnum.getCode());

        responseStructure.setSuccess(codeEnum.equals(ErrorCodeEnum.SUCCESS));

        return responseStructure;
    }
}
