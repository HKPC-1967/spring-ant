package org.hkpc.dtd.business.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hkpc.dtd.business.demo.service.DemoRuleService;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.hkpc.dtd.component.postgres.dao.DemoRuleMapper;
import org.hkpc.dtd.component.postgres.model.DemoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class DemoRuleServiceImpl implements DemoRuleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DemoRuleMapper demoRuleMapper;

    @Autowired
    public DemoRuleServiceImpl(DemoRuleMapper demoRuleMapper) {
        this.demoRuleMapper = demoRuleMapper;
    }


    @Override
    public void insert(DemoRule inDbBean) throws CodeException{
        Date date = new Date();
        inDbBean.setDisabled(false);
        inDbBean.setCreatedAt(date);
        inDbBean.setUpdatedAt(date);
        inDbBean.setStatus("1");
        inDbBean.setProgress(1);

//       as we are using auto incremented primary key, we cannot not use insert here, need to use insertSelective
        demoRuleMapper.insertSelective(inDbBean);
    }

    @Override
    public List<DemoRule> selectAll() throws CodeException {
//        PageHelper.startPage(pageNum, pageSize);
        return demoRuleMapper.selectAll();
    }

    @Override
    public PageInfo<DemoRule> selectPage(Integer current, Integer pageSize) throws CodeException {
        PageHelper.startPage(current, pageSize);

//        PageHelper.startPage(2, 10);
        List<DemoRule> demoRuleList = demoRuleMapper.selectAll();
        return new PageInfo<>(demoRuleList);
    }


}
