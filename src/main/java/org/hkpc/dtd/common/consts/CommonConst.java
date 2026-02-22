package org.hkpc.dtd.common.consts;

import org.hkpc.dtd.business.user.controller.UserLoginController;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;
import java.util.List;

public class CommonConst {
    //======= Config =======
    /**
     * Don't check JWT or Refresh token for these urls </br>
     * <p>
     * Note: you should only allow /swagger-ui /api-doc requests within your internal network, which can be achieved by Nginx or other methods.</br>
     * 注意：你应该只允许内部网络访问 /swagger-ui /api-docs 请求，可以通过 Nginx 或其他方法实现。
     * <p>
     * In the context of AntPathMatcher, the difference between ** and * is as follows:</br>
     * *: matches zero or more characters within a single directory level.</br>
     * **: matches zero or more directories in a path.
     */
    public static final List<String> NO_CHECK_JWT_TOKEN_URLS = Arrays.asList("/user/login/login"
            , "/swagger-ui/**", "/api-docs/**"
            , "/demo/message/errorMessageDemo");

    /**
     * the version of the JWT Subject Version, you may want to change this version when you change the structure of the JWT Subject
     */
    public static final Integer JWT_SUBJECT_VERSION = 1;
    /**
     * If the JWT permission version is JWT_PERMISSION_VERSION_STEP steps smaller than the current version in the database, then we consider the token invalid.<br>
     * <p>
     * the reason why we don't set it to 1 is that there may be multiple concurrent requests at the same time from the browser, or multiple pages for the same account.<br>
     * <p>
     * Currently, the step for {@link org.hkpc.dtd.business.user.controller.UserLoginController#outLogin()} is 5, so all tokens will be invalid after any logout<br>
     * the step for {@link org.hkpc.dtd.business.user.service.impl.UserAccountServiceImpl#refreshToken(Integer)} is 1, so the token will not be invalid after one time of refreshToken<br>
     * <p>
     * You can consider below actions depends on your business, and make sure to test it well after any modification:
     * <ul>
     *   <li>Set {@code JWT_PERMISSION_VERSION_DEFAULT_STEP} to 1 for higher security for refreshToken.</li>
     *   <li>add {@code JWT_PERMISSION_VERSION_DEFAULT_STEP} logic in login method if you want all other tokens to be invalid after any login.</li>
     *   <li>Consider using lock technology,instead of making {@code JWT_PERMISSION_VERSION_DEFAULT_STEP} to be 5.</li>
     * </ul>
     */
    public static final Integer JWT_PERMISSION_VERSION_DEFAULT_STEP = 5;


    //======= Special purpose =======
    /**
     * the error log with this flag will trigger alarm notification(email, sms, etc.)
     */
    public static final Marker MARK_ALARM_SYSTEM = MarkerFactory.getMarker("[MARK_ALARM_SYSTEM]");
    /**
     * log with this flag will trigger alarm notification(email, sms, etc.)
     * </br>
     * ps: in unit test, it doesn't print
     */
    public static final Marker MARK_ALARM_CUSTOM = MarkerFactory.getMarker("[MARK_ALARM_CUSTOM]");

    //======= Static constant =======
    public static final String HEADER_JWT_ACCESS_TOKEN = "Authorization";
    public static final String HEADER_JWT_REFRESH_TOKEN = "refreshToken";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

}
