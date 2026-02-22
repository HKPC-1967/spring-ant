package org.hkpc.dtd;

import com.alibaba.fastjson2.JSON;
import org.hkpc.dtd.business.demo.dto.RuleInsertDTO;
import org.hkpc.dtd.common.utils.ObjectUtil;
import org.hkpc.dtd.component.postgres.model.DemoRule;

/*
* here is the test for pure java static function, no dependency on spring
* so there is no need to use @SpringBootTest and @ActiveProfiles("dev")
* */
public class StaticMethodTest {

    public static void main(String[] args) {
        testClone2TargetClass();
    }

    private static void testClone2TargetClass() {
        RuleInsertDTO ruleInsertDTO = new RuleInsertDTO();
        ruleInsertDTO.setName("test");

        DemoRule demoRule = ObjectUtil.clone2TargetClass(ruleInsertDTO, DemoRule.class);

        System.out.println(JSON.toJSONString(demoRule));
    }

}
