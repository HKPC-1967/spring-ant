package org.hkpc.dtd.business.demo.service;


import com.github.pagehelper.PageInfo;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;

import org.hkpc.dtd.component.postgres.model.DemoRule;

import java.util.List;


public interface DemoRuleService {


    void insert(DemoRule inDbBean) throws CodeException;


    List<DemoRule> selectAll() throws CodeException;

    PageInfo<DemoRule> selectPage(Integer current, Integer pageSize) throws CodeException;
}
