package org.hkpc.dtd;

import com.alibaba.fastjson2.JSON;
import org.hkpc.dtd.component.postgres.dao.DemoRuleMapper;
import org.hkpc.dtd.component.postgres.model.DemoRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class DemoRuleTest {


    private final DemoRuleMapper demoRuleMapper;

    @Autowired
    public DemoRuleTest(DemoRuleMapper demoRuleMapper) {
        this.demoRuleMapper = demoRuleMapper;
    }

    @Test
    public void testFilterFields() {
        List<DemoRule> demoRules = demoRuleMapper.selectAll();
        System.out.println(JSON.toJSONString(demoRules));

//       if you want to filter some fields that you don't want to return to front end, you can do it like this at controller level
        demoRules.forEach(demoRule -> {
            demoRule.setDisabled(null);
            demoRule.setHref(null);
            demoRule.setAvatar(null);
            demoRule.setName(null);
            demoRule.setOwner(null);
            demoRule.setDesc(null);
            demoRule.setStatus(null);
            demoRule.setProgress(null);
        });

        System.out.println(JSON.toJSONString(demoRules));
    }

}
