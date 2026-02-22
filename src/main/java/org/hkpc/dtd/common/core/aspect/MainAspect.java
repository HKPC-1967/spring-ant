package org.hkpc.dtd.common.core.aspect;


import com.alibaba.fastjson2.JSON;
import org.hkpc.dtd.common.core.jwt.model.UserJwtSubjectContextHolder;
import org.hkpc.dtd.common.consts.CommonConst;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.hkpc.dtd.common.core.aspect.utils.ResponseStructureUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hkpc.dtd.common.utils.CompareUtil;
import org.hkpc.dtd.common.utils.RedactUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Aspect
@Component
public class MainAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * don't log the request and response params (but you can still log whatever you want in the Controller function)
     * for example, you don't want to log Excel download, login password, etc.
     */
    private static final List<String> NO_PRINT_PARAMS_URLS = Arrays.asList("/user/login/login","/demo/file/downloadExcelTest");
    /**
     * don't log the request params d
     */
    private static final List<String> NO_PRINT_REQUEST_PARAMS_URLS = Arrays.asList("/user/login/refreshToken","/demo/file/uploadFileTest");
    /**
     * will not return {@link ResponseStructureUtil.ResponseStructure} for these urls
     * for example, if the HTTP request is for Excel Download, the front will not check result, as the result is not a JSON
     */
    private static final List<String> NO_CHANGE_RESULT_URLS = Arrays.asList("/demo/file/downloadExcelTest");

    private final UserJwtSubjectContextHolder userJwtSubjectContextHolder;
    private final RedactUtil redactUtil;

    public final static String FLAG_NOT_PARSED = "[NotParsed]";

    @Autowired
    public MainAspect(UserJwtSubjectContextHolder userJwtSubjectContextHolder, RedactUtil redactUtil) {
        this.userJwtSubjectContextHolder = userJwtSubjectContextHolder;
        this.redactUtil = redactUtil;
    }

    /**
     * AspectJ Pointcut expression
     * <p>
     * example of !execution: </br>
     * && !execution(public * org.hkpc.dtd.business.demo.controller.xxxController.xxxx(..))
     */
    @Pointcut("""
             execution(public * org.hkpc.dtd.business.*.controller..*.*(..))
            """)
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) {
//        String methodName = pjp.getSignature().getName();
        String urlPath = "";

        long startTime = System.currentTimeMillis();
        Object result;
        Integer userAccountIdForLog = null;

        // the params4Log are only for unified logging
        String urlParams4Log = FLAG_NOT_PARSED;
        String bodyParams4Log = FLAG_NOT_PARSED;
        try {
            // For a unified log format and simplicity, only POST and GET (use "?a=6&b=6", not "/a/6/b/6") methods are tested, ensuring guaranteed support.
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String method = httpServletRequest.getMethod();
            String contentType = httpServletRequest.getHeader("Content-Type");
            urlPath = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
            // token =123&current=1&pageSize=20
            urlParams4Log = httpServletRequest.getQueryString();
            /**
             * Notes: if request method is GET and the params if after ?, JSON.toJSONString(pjp.getArgs()) will print like [1,20,null]
             * GET method result record:[1,20,null] (so ignore this log if it's a GET method, here is just the same value from "urlParams4Log" above)
             * POST method result record: {"current":1,"pageSize":20}
             */
            if("POST".equals(method) && (contentType != null && !contentType.startsWith("multipart/form-data"))) {
                Object[] bodyArgs = pjp.getArgs();
                // Consider limiting the size of bodyParams4Log if your system has long input, and you don't want to log it all.
                bodyParams4Log = JSON.toJSONString(bodyArgs);
            }

//            StringBuffer requestURL = httpServletRequest.getRequestURL(); // result record : http://localhost:8080/demo/demo/selectPage
//            Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
//            paramsJson = JSON.toJSONString(parameterMap); //result record : {"token ":["123"],"current":["1"],"pageSize":["20"]}    this method only get values from GET method

            if (CompareUtil.noneMatch(CommonConst.NO_CHECK_JWT_TOKEN_URLS, urlPath))  {
                userAccountIdForLog = userJwtSubjectContextHolder.getUserJwtSubject().id();
            }

            result = pjp.proceed();
        } catch (Throwable e) {
            return logAndHandleException(e, urlPath, startTime, userAccountIdForLog, urlParams4Log, bodyParams4Log);
        }

        ResponseStructureUtil.ResponseStructure successResponse = ResponseStructureUtil.getSuccessResponse(result);
        logRequest(urlPath, startTime, userAccountIdForLog, urlParams4Log, bodyParams4Log, result);

        if (NO_CHANGE_RESULT_URLS.contains(urlPath)) {
            return result;
        } else {
            return successResponse;
        }
    }

    /**
     * Logs containing {@link CommonConst#MARK_ALARM_SYSTEM} is designed to trigger alarm notifications (email, SMS, etc.) in the Log Analysis Platform.
     */
    private Object logAndHandleException(Throwable e, String urlPath, long startTime, Integer userAccountIdForLog, String urlParams, String bodyParams) {
        long time = System.currentTimeMillis() - startTime;
        String safeUrlParams = redactUtil.redactQueryString(urlParams);
        String safeBodyParams = redactUtil.redactBodyParams(bodyParams);
        if (e instanceof CodeException) {
            logger.info("[{}][{}][{}] errCode:{},{}, params:[{}][{}]", urlPath, time, userAccountIdForLog, e.getMessage(), e.getStackTrace()[0], safeUrlParams, safeBodyParams);
            return ResponseStructureUtil.getResponse(((CodeException) e).getCodeEnum());
        } else {
            logger.error(CommonConst.MARK_ALARM_SYSTEM, "[{}][{}][{}] params:[{}][{}], errSys:{}", urlPath, userAccountIdForLog, time, safeUrlParams, safeBodyParams, e.getMessage(), e);
            return ResponseStructureUtil.getResponse(ErrorCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * Log the request and response information in JSON format for integration with Log Analysis Platforms such as ELK (Elasticsearch, Logstash, Kibana) or AWS CloudWatch.
     */
    private void logRequest(String urlPath, long startTime, Integer userAccountIdForLog, String urlParams, String bodyParams, Object result) {
        long time = System.currentTimeMillis() - startTime;
        String safeUrlParams = redactUtil.redactQueryString(urlParams);
        String safeBodyParams = redactUtil.redactBodyParams(bodyParams);
        if (NO_PRINT_PARAMS_URLS.contains(urlPath)) {
            logger.info("{\"method\":\"{}\",\"time\":{},\"userAccountId\":{}}", urlPath, time, userAccountIdForLog);
        }else if (NO_PRINT_REQUEST_PARAMS_URLS.contains(urlPath)) {
            logger.info("{\"method\":\"{}\",\"time\":{},\"userAccountId\":{},\"urlParams\":\"{}\",\"bodyParams\":{}}", urlPath, time, userAccountIdForLog, safeUrlParams, safeBodyParams);
        } else {
            logger.info("{\"method\":\"{}\",\"time\":{},\"userAccountId\":{},\"urlParams\":\"{}\",\"bodyParams\":{},\"resp\":{}}", urlPath, time, userAccountIdForLog, safeUrlParams, safeBodyParams, JSON.toJSONString(result));
        }
    }
}
