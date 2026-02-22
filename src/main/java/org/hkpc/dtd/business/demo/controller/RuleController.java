package org.hkpc.dtd.business.demo.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import org.hkpc.dtd.business.demo.dto.RuleInsertDTO;
import org.hkpc.dtd.business.demo.service.DemoRuleService;
import org.hkpc.dtd.common.controller.BaseController;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.hkpc.dtd.common.utils.ObjectUtil;
import org.hkpc.dtd.component.postgres.model.DemoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping(value = "/demo/rule")
public class RuleController extends BaseController {

    private final DemoRuleService demoRuleService;

    @Autowired
    public RuleController(final DemoRuleService demoRuleService) {
        this.demoRuleService = demoRuleService;
    }


    @Operation(summary = "select the rule record by page", description = """
            you can use "name" to test different exception, for example, input "1" to test WARN_MESSAGE, check the code to see why
            """)
    @GetMapping("/selectPage")
    public Object selectPage(@RequestParam(required = true) Integer current, @RequestParam(required = true) Integer pageSize, String name) throws CodeException {
        try {
            // to test loading at front end
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /**
         * 1, If a field in the response object has a null value, it will not be shown in the final JSON response to frond end.
         *
         * Java basic:
         * user "Integer" than "int" if you need to distinguish between 0 and null("int" don't have null, the default value is always 0, 所以就不知道0代表沒有值，還是值為0)
         */
        PageInfo<DemoRule> demoRulePageInfo = demoRuleService.selectPage(current, pageSize);

//      below is a demo to remove some fields (to not send to front-end), for example, remove the "password" field before sending to front-end
        demoRulePageInfo.getList()
                .forEach(demoRule -> {
////            will show "" in response JSON
//                    demoRule.setName("");
////            will not show the "name" field in response JSON
//                    demoRule.setName(null);
                });


        return demoRulePageInfo;
    }


    @Operation(summary = "insert the rule record", description = "")
    @PostMapping("/insert")
    public Object insert(@Valid @RequestBody RuleInsertDTO dto) throws CodeException {
        DemoRule demoRule = ObjectUtil.clone2TargetClass(dto, DemoRule.class);

        demoRuleService.insert(demoRule);

        return null;
    }


    @Operation(summary = "for your test, you can return any JSON to front as you want", description = "")
    @PostMapping("/testPost")
    public Object testPost() throws CodeException {
//       Copy the JSON string you want to return here for test
        String jsonString = """
                {"name":"Serati Ma","avatar":"https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png","userid":"00000001","email":"antdesign@alipay.com","signature":"海纳百川，有容乃大","title":"交互专家","group":"蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED","tags":[{"key":"0","label":"很有想法的"},{"key":"1","label":"专注设计"},{"key":"2","label":"辣~"},{"key":"3","label":"大长腿"},{"key":"4","label":"川妹子"},{"key":"5","label":"海纳百川"}],"notifyCount":12,"unreadCount":11,"country":"China","access":"admin","geographic":{"province":{"label":"浙江省","key":"330000"},"city":{"label":"杭州市","key":"330100"}},"address":"西湖区工专路 77 号","phone":"0752-268888888"}
                """;
        // change JSON String to JSON Object
        JSONObject jsonObject = JSON.parseObject(jsonString);
        return jsonObject;
    }
}
