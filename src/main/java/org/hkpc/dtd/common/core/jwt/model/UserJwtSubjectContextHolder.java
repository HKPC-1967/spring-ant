package org.hkpc.dtd.common.core.jwt.model;

import org.springframework.stereotype.Component;

@Component
public class UserJwtSubjectContextHolder {

    ThreadLocal<UserJwtSubject> threadLocal = new ThreadLocal<>();

    /**
     * get the user information from threadLocal
     */
    public UserJwtSubject getUserJwtSubject() {
        return threadLocal.get();
    }

    /**
     * set the user information to threadLocal
     */
    public void setUserJwtSubject(UserJwtSubject threadLocalBean) {
        this.threadLocal.set(threadLocalBean);
    }

}
