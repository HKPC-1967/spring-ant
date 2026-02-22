package org.hkpc.dtd.common.core.aspect.handler;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.MalformedJwtException;
import org.hkpc.dtd.common.consts.CommonConst;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.utils.ResponseStructureUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.hkpc.dtd.common.utils.RedactUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Global exception handling
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RedactUtil redactUtil;

    @Autowired
    public GlobalExceptionHandler(RedactUtil redactUtil) {
        this.redactUtil = redactUtil;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e) {
        // ===== 1, get the request parameters for logging
        Map<String, Map<String, String>> allRequestParams;
        try {
            allRequestParams = getAllRequestParams();
        } catch (Exception e1) {
            logger.error(CommonConst.MARK_ALARM_SYSTEM, "exceptionHandlerErr when getAllRequestParams:{}", e.getMessage(), e);
            return ResponseStructureUtil.getResponse(ErrorCodeEnum.SYSTEM_ERROR_2);
        }

        // ===== 2, for some specific Exception, we can return the specific error message defined in ErrorCodeEnum.java
        switch (e) {
            case MethodArgumentNotValidException methodArgumentNotValidException -> {
                /**
                 * For example, Error from {@link NotEmpty} {@link Min} {@link Max}
                 */
                logger.info("exceptionHandlerErr MethodArgumentNotValidException: [{}] errSys:{}", JSON.toJSONString(allRequestParams), e.getMessage(), e);
//            can not get the field,
//            String defaultMessage = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();
//            if (StringUtils.hasLength(defaultMessage)) {
//                return ResponseUtil.getCodeResponseWithMessage(defaultMessage);
//            }
                ObjectError objectError = methodArgumentNotValidException.getBindingResult().getAllErrors().getFirst();
                String defaultMessage = objectError.getDefaultMessage();
                String message = defaultMessage + "___" + objectError.toString();
                if (objectError instanceof FieldError) {
                    String field = ((FieldError) objectError).getField();
                    message = field + "___" + message;
                }
                return ResponseStructureUtil.getResponseWithDebugMessage(ErrorCodeEnum.PARAM_INVALID_BY_VALIDATOR, message);
            }
            case HttpMessageNotReadableException httpMessageNotReadableException -> {
                // For example, Error from: 1, UUID has to be represented by standard 36-char representation  2, not one of the values accepted for Enum class 3, the required request body is missing
//            (@Valid @RequestBody RuleInsertDTO dto) org.springframework.http.converter.HttpMessageNotReadableException   errSys:Required request body is missing: public java.lang.Object org.hkpc.dtd.business.demo.controller.RuleController.insert(org.hkpc.dtd.business.demo.dto.RuleInsertDTO)
                logger.info("exceptionHandlerErr HttpMessageNotReadableException: [{}] errSys:{}", JSON.toJSONString(allRequestParams), e.getMessage(), e);
                return ResponseStructureUtil.getResponse(ErrorCodeEnum.PARAM_FORMAT_INVALID);
            }
            case MalformedJwtException malformedJwtException -> {
                // For example, Error from: 1, JWT Token not provided
                logger.info("exceptionHandlerErr MalformedJwtException: [{}] errSys:{}", JSON.toJSONString(allRequestParams), e.getMessage(), e);
                return ResponseStructureUtil.getResponse(ErrorCodeEnum.JWT_PARAM_FORMAT_INVALID);
            }
            case NoResourceFoundException noResourceFoundException -> {
                // For example, Error from: 1, the requested HTTP path is not implemented
                logger.info("exceptionHandlerErr MalformedJwtException: [{}] errSys:{}", JSON.toJSONString(allRequestParams), e.getMessage(), e);
                return ResponseStructureUtil.getResponse(ErrorCodeEnum.HTTP_REQUEST_NOT_SUPPORTED);
            }
            default -> {
                // ===== 3, log the unexpected Exception and trigger the notification for analysis
                logger.error(CommonConst.MARK_ALARM_SYSTEM, "exceptionHandlerErr system: [{}] errSys:{}", JSON.toJSONString(allRequestParams), e.getMessage(), e);
                return ResponseStructureUtil.getResponse(ErrorCodeEnum.SYSTEM_ERROR_3);
            }
        }
    }

    private Map<String, Map<String, String>> getAllRequestParams() {
        Map<String, Map<String, String>> allParams = new HashMap<>();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取所有请求头的参数名称并加入Map对象
        Map<String, String> headerParams = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerParams.put(key, value);
        }
        allParams.put("headers", redactUtil.redactHeadersMap(headerParams));

        // 获取所有请求参数的参数名称并加入Map对象
        /*
        Answer from AI：request.getParameterNames() 不会返回 JSON 请求体。
        GET：返回 query string 里的参数（?a=1&b=2）。
        POST：只有在 application/x-www-form-urlencoded 或 multipart/form-data 这类表单提交时，才会把表单字段当作 parameter 返回。
        POST + JSON（application/json）：不会出现在 getParameterNames() 里，需要从 request.getInputStream() / @RequestBody 读取。
        */
        Map<String, String> queryParams = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String key = paramNames.nextElement();
            String value = request.getParameter(key);
            queryParams.put(key, value);
        }
        allParams.put("query", redactUtil.redactQueryMap(queryParams));

        return allParams;
    }
}
