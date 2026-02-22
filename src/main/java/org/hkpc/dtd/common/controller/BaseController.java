package org.hkpc.dtd.common.controller;


import org.hkpc.dtd.common.core.jwt.model.UserJwtSubjectContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    protected UserJwtSubjectContextHolder userJwtSubjectContextHolder;


}
