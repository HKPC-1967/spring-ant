package org.hkpc.dtd.business.demo.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.hkpc.dtd.business.demo.dto.ErrorMessageDemoDTO;
import org.hkpc.dtd.common.controller.BaseController;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.enums.ErrorShowTypeEnum;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping(value = "/demo/message")
public class MessageController extends BaseController {


    @Operation(summary = "test error message", description = "")
    @PostMapping("/errorMessageDemo")
    public Object errorMessageDemo(@Valid @RequestBody ErrorMessageDemoDTO dto) throws CodeException {
        try {
            // to test loading at front end
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ErrorShowTypeEnum errorShowTypeEnum = ErrorShowTypeEnum.fromValue(dto.getErrorShowType());
        if (errorShowTypeEnum == null) {
            throw new CodeException(ErrorCodeEnum.PARAM_INVALID_BY_VALIDATOR);
        }

        return switch (errorShowTypeEnum) {
            case SILENT -> throw new CodeException(ErrorCodeEnum.TEST_SILENT_0);
            case WARN_MESSAGE -> throw new CodeException(ErrorCodeEnum.TEST_WARN_MESSAGE_1);
            case ERROR_MESSAGE -> throw new CodeException(ErrorCodeEnum.TEST_ERROR_MESSAGE_2);
            case NOTIFICATION -> throw new CodeException(ErrorCodeEnum.TEST_NOTIFICATION_3);
            default -> null;
        };
    }
}
