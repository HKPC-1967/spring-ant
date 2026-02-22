package org.hkpc.dtd.business.user.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.hkpc.dtd.business.user.emums.LoginTypeEnum;
import org.hkpc.dtd.business.user.service.UserAccountService;
import org.hkpc.dtd.business.user.vo.LoginVO;
import org.hkpc.dtd.common.core.jwt.JwtValidateComponent;
import org.hkpc.dtd.common.core.security.enums.TokenTypeEnum;
import org.hkpc.dtd.common.core.jwt.model.UserJwtSubject;
import org.hkpc.dtd.common.consts.CommonConst;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.hkpc.dtd.component.postgres.dao.AccountRoleRelationMapper;
import org.hkpc.dtd.component.postgres.dao.UserAccountMapper;
import org.hkpc.dtd.component.postgres.model.UserAccount;
import org.hkpc.dtd.component.postgres.model.AccountRoleRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountRoleRelationMapper accountRoleRelationMapper;
    private final UserAccountMapper userAccountMapper;
    private final PasswordEncoder passwordEncoder;

    private final JwtValidateComponent jwtValidateComponent;

    @Autowired
    public UserAccountServiceImpl(UserAccountMapper userAccountMapper, PasswordEncoder passwordEncoder, JwtValidateComponent jwtValidateComponent, AccountRoleRelationMapper accountRoleRelationMapper) {
        this.userAccountMapper = userAccountMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtValidateComponent = jwtValidateComponent;
        this.accountRoleRelationMapper = accountRoleRelationMapper;
    }


    @Override
    public LoginVO login(UserAccount userAccount) throws CodeException {
        UserAccount userAccountFromDB = userAccountMapper.selectByUsername(userAccount.getUsername());

        if (userAccountFromDB == null) {
            throw new CodeException(ErrorCodeEnum.INVALID_USERNAME_PASS);
        }

        if (!passwordEncoder.matches(userAccount.getPassword(), userAccountFromDB.getPassword())) {
            throw new CodeException(ErrorCodeEnum.INVALID_USERNAME_PASS);
        }

        return getLoginVO(userAccountFromDB);
    }

    @Override
    public LoginVO refreshToken(Integer userAccountId) throws CodeException {
        // There may be concurrent request from browser, so the step is only 1
        UserAccount userAccountFromDB = updatePermissionVersion(userAccountId, 1);
        return getLoginVO(userAccountFromDB);
    }

    @Override
    public void validateJwtSubjectWithDB(UserJwtSubject userJwtSubject) throws CodeException {
        UserAccount userAccountDB = userAccountMapper.selectByPrimaryKey(userJwtSubject.id());
        // compare all fields of UserJwtSubject except for "tokenType"
        if (CommonConst.JWT_SUBJECT_VERSION.equals(userJwtSubject.subVer())
//                && userAccountDB.getPermVer().equals(userJwtSubject.permVer()
                && ((userAccountDB.getPermVer() - CommonConst.JWT_PERMISSION_VERSION_DEFAULT_STEP) < (userJwtSubject.permVer()))

                && userAccountDB.getId().equals(userJwtSubject.id())
                && userAccountDB.getUsername().equals(userJwtSubject.username())) {
            // do nothing
        } else {
            logger.info("jwt subject not match with db, jwtSubject: {}, db: {}", userJwtSubject, userAccountDB);
            throw new CodeException(ErrorCodeEnum.INVALID_JWT_TOKEN);
        }
    }

    @Override
    public UserAccount updatePermissionVersion(Integer userAccountId, Integer step) throws CodeException {
        UserAccount userAccountFromDB = userAccountMapper.selectByPrimaryKey(userAccountId);

        Integer newVersion = getNewVersion(userAccountFromDB.getPermVer(), step);

        UserAccount userAccountFromUpdate = new UserAccount();
        userAccountFromUpdate.setId(userAccountId);
        userAccountFromUpdate.setPermVer(newVersion);
        userAccountMapper.updateByPrimaryKeySelective(userAccountFromUpdate);

        userAccountFromDB.setPermVer(newVersion);
        return userAccountFromDB;
    }

    private Integer getNewVersion(Integer permVer, int step) {
        // Integer.MAX_VALUE should be far more enough for the version, but just in case, we can reset it to 1 if it's too big, to avoid overflow
        if (permVer > (Integer.MAX_VALUE - 1000)){
            logger.info(CommonConst.MARK_ALARM_CUSTOM, "permVer is too big : {}", permVer);
            return 1;
        }
        return permVer + step;
    }

    @Override
    public Object currentUser(Integer userAccountId, List<Integer> roleIds) throws CodeException {
        UserAccount userAccount = userAccountMapper.selectByPrimaryKey(userAccountId);
//        This JSONString below is from Ant Design Pro; In a real project, get values from the userAccount object above
//        String jsonString = """
//                {"name":"Serati Ma","avatar":"https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png","userid":"00000001","email":"antdesign@alipay.com","signature":"海纳百川，有容乃大","title":"交互专家","group":"蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED","tags":[{"key":"0","label":"很有想法的"},{"key":"1","label":"专注设计"},{"key":"2","label":"辣~"},{"key":"3","label":"大长腿"},{"key":"4","label":"川妹子"},{"key":"5","label":"海纳百川"}],"notifyCount":12,"unreadCount":11,"country":"China","access":"admin","geographic":{"province":{"label":"浙江省","key":"330000"},"city":{"label":"杭州市","key":"330100"}},"address":"西湖区工专路 77 号","phone":"0752-268888888"}
//                """;
        String jsonString = String.format("""
                {"name":"%s","avatar":"https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png","userid":"00000001","email":"antdesign@alipay.com","signature":"海纳百川，有容乃大","title":"交互专家","group":"蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED","tags":[{"key":"0","label":"很有想法的"},{"key":"1","label":"专注设计"},{"key":"2","label":"辣~"},{"key":"3","label":"大长腿"},{"key":"4","label":"川妹子"},{"key":"5","label":"海纳百川"}],"notifyCount":12,"unreadCount":11,"country":"China","access":"admin","geographic":{"province":{"label":"浙江省","key":"330000"},"city":{"label":"杭州市","key":"330100"}},"address":"西湖区工专路 77 号","phone":"0752-268888888"}
                """, userAccount.getNickname());
        // 将字符串转换为 JSON 对象
        JSONObject jsonObject = JSON.parseObject(jsonString);

        // Used by front end for RBAC
        jsonObject.put("roleIds", roleIds);
        return jsonObject;
    }


    private LoginVO getLoginVO(UserAccount userAccountFromDB) throws CodeException {
        List<Integer> roleIds = getRoleIdsByAccountId(userAccountFromDB.getId());

        UserJwtSubject userJwtSubjectAccess = getUserJwtSubject(TokenTypeEnum.ACCESS_TOKEN, userAccountFromDB, roleIds);
        UserJwtSubject userJwtSubjectRefresh = getUserJwtSubject(TokenTypeEnum.REFRESH_TOKEN, userAccountFromDB, roleIds);
        String accessToken = jwtValidateComponent.generateAccessToken(JSON.toJSONString(userJwtSubjectAccess));
        String refreshToken = jwtValidateComponent.generateRefreshToken(JSON.toJSONString(userJwtSubjectRefresh));

//      this JSON is from ant design pro :  {"status":"ok","type":"account","currentAuthority":"admin"}
        LoginVO loginVO = new LoginVO();
        loginVO.setStatus("ok");
//        Ant Design Pro did not use the fields below, so here removed them. the actual user information is required by "fetchUserInfo()" in Ant Design Pro
        loginVO.setType(LoginTypeEnum.account.toString());
//        loginVO.setCurrentAuthority(userAccount.getUsername());
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        long currentTime = System.currentTimeMillis();
        loginVO.setAccessTokenExpiration(String.valueOf(currentTime + JwtValidateComponent.ACCESS_TOKEN_EXPIRE_TIME));
        loginVO.setRefreshTokenExpiration(String.valueOf(currentTime + JwtValidateComponent.REFRESH_TOKEN_EXPIRE_TIME));
        return loginVO;
    }

    private List<Integer> getRoleIdsByAccountId(Integer userAccountId) {
        List<AccountRoleRelation> accountRoleRelations = accountRoleRelationMapper.selectAllByAccountId(userAccountId);
        return accountRoleRelations.stream()
                .map(AccountRoleRelation::getUserRoleId)
                .toList();
    }

    private static UserJwtSubject getUserJwtSubject(TokenTypeEnum refresh, UserAccount userAccountFromDB, List<Integer> roleIds) {
        return new UserJwtSubject(refresh.getValue(), CommonConst.JWT_SUBJECT_VERSION, userAccountFromDB.getId(), userAccountFromDB.getUsername(), userAccountFromDB.getPermVer(), roleIds);
    }
}
