package org.hkpc.dtd.business.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hkpc.dtd.business.user.dto.LoginDTO;
import org.hkpc.dtd.business.user.service.UserAccountService;
import org.hkpc.dtd.business.user.vo.LoginVO;
import org.hkpc.dtd.common.consts.CommonConst;
import org.hkpc.dtd.common.controller.BaseController;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import jakarta.validation.Valid;
import org.hkpc.dtd.common.utils.ObjectUtil;
import org.hkpc.dtd.component.postgres.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/login")
public class UserLoginController extends BaseController {

    private final UserAccountService userAccountService;

    @Autowired
    public UserLoginController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /*
     * NOTE: autologin (from Ant Design Pro login page) not implemented!! we probably don't need it
     * */
    @Operation(summary = "login", description = "login and return jwt accessToken and refreshToken")
    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginDTO dto) throws CodeException {
        // log manually here, as the password should not be logged, even the password is hashed already
        // 中文注释：这里手动日志记录，因为密码不应该被log, 即使密码已经hash过了
        logger.info("loginDTO: {}", dto.getUsername());

        UserAccount userAccount = ObjectUtil.clone2TargetClass(dto, UserAccount.class);

        LoginVO loginVO = userAccountService.login(userAccount);

        logger.info("login resp: {}", loginVO.getStatus());
        return loginVO;
    }

    @Operation(summary = "get current user info", description = """
            response JSON below is from Ant Design Pro
            
            # success response
            {"success":true,"data":{"name":"Serati Ma","avatar":"https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png","userid":"00000001","email":"antdesign@alipay.com","signature":"海纳百川，有容乃大","title":"交互专家","group":"蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED","tags":[{"key":"0","label":"很有想法的"},{"key":"1","label":"专注设计"},{"key":"2","label":"辣~"},{"key":"3","label":"大长腿"},{"key":"4","label":"川妹子"},{"key":"5","label":"海纳百川"}],"notifyCount":12,"unreadCount":11,"country":"China","access":"admin","geographic":{"province":{"label":"浙江省","key":"330000"},"city":{"label":"杭州市","key":"330100"}},"address":"西湖区工专路 77 号","phone":"0752-268888888"}}
            
            # fail response (we don't need this fail response, as the front-end will be redirected to login page, rather than showing this message)
            {"data":{"isLogin":false},"errorCode":"401","errorMessage":"请先登录！","success":true}
            """)
    @GetMapping("/currentUser")
    public Object currentUser() throws CodeException {
        Integer userAccountId = userJwtSubjectContextHolder.getUserJwtSubject().id();
        List<Integer> roleIds = userJwtSubjectContextHolder.getUserJwtSubject().roleIds();

        Object currentUser = userAccountService.currentUser(userAccountId,roleIds);

        return currentUser;
    }

    @Operation(summary = "logout", description = "logout and redirect to login page")
    @PostMapping("/outLogin")
    public Object outLogin() throws CodeException {
        Integer userAccountId = userJwtSubjectContextHolder.getUserJwtSubject().id();
        userAccountService.updatePermissionVersion(userAccountId,CommonConst.JWT_PERMISSION_VERSION_DEFAULT_STEP);

        // here is very special to return an CodeException on purpose, as this can redirect the front-end to the login page
        throw new CodeException(ErrorCodeEnum.TEST_REDIRECT_9);
    }

    /**
    * Note: don't change the current path name, as there is special logic with it in {@link org.hkpc.dtd.common.core.security.filter.JwtAuthenticationFilter#LOGIN_REFRESH_TOKEN_URL}
    */
    @Operation(summary = "user refreshToken to get new tokens", description = """
            don't change the current path name, as there is special logic with it in JwtAuthenticationFilter.java
            """,
            parameters = {@Parameter(name = CommonConst.HEADER_JWT_REFRESH_TOKEN, required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", defaultValue = CommonConst.JWT_TOKEN_PREFIX))})
    @PostMapping("/refreshToken")
    public Object refreshToken() throws CodeException {
        Integer userAccountId = userJwtSubjectContextHolder.getUserJwtSubject().id();

        LoginVO loginVO = userAccountService.refreshToken(userAccountId);

        return loginVO;
    }
}
