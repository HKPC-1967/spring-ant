package org.hkpc.dtd.business.user.service;


import org.hkpc.dtd.business.user.vo.LoginVO;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.hkpc.dtd.common.core.jwt.model.UserJwtSubject;
import org.hkpc.dtd.component.postgres.model.UserAccount;
import org.hkpc.dtd.common.consts.CommonConst;

import java.util.List;

public interface UserAccountService {
    LoginVO login(UserAccount userAccount) throws CodeException;

    /**
     * update the Permission Version to let the old JWT token invalid
     *
     * @param step the default step is {@link CommonConst#JWT_PERMISSION_VERSION_DEFAULT_STEP}
     * @return the updated UserAccount
     */
    UserAccount updatePermissionVersion(Integer userAccountId, Integer step) throws CodeException;

    Object currentUser(Integer userAccountId, List<Integer> roleIds) throws CodeException;

    LoginVO refreshToken(Integer userAccountId) throws CodeException;

    /**
     * Check if the JWT subject is the same as the data in the database
     */
    void validateJwtSubjectWithDB(UserJwtSubject userJwtSubject) throws CodeException;


//    void login(DemoRule inDbBean);

}
