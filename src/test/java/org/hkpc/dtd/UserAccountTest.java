package org.hkpc.dtd;

import com.alibaba.fastjson2.JSON;
import org.hkpc.dtd.component.postgres.dao.AccountRoleRelationMapper;
import org.hkpc.dtd.component.postgres.dao.UserAccountMapper;
import org.hkpc.dtd.component.postgres.model.UserAccount;
import org.hkpc.dtd.component.postgres.model.AccountRoleRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
public class UserAccountTest {

    private final UserAccountMapper userAccountMapper;
    private final AccountRoleRelationMapper accountRoleRelationMapper;

    @Autowired
    public UserAccountTest(UserAccountMapper userAccountMapper, AccountRoleRelationMapper accountRoleRelationMapper) {
        this.userAccountMapper  = userAccountMapper ;
        this.accountRoleRelationMapper = accountRoleRelationMapper;
    }




    @Test
    public void selectByUsername() {
        UserAccount user = userAccountMapper.selectByUsername("admin");
        System.out.println(JSON.toJSONString(user));


    }
    @Test
    public void selectAllByAccountId() {
        // tested there is no NullPointerException when the userAccountId is invalid
        List<AccountRoleRelation> accountRoleRelations = accountRoleRelationMapper.selectAllByAccountId(3);
        List<Integer> roleIds = accountRoleRelations.stream()
                .map(AccountRoleRelation::getUserRoleId)
                .toList();
        System.out.println(JSON.toJSONString(roleIds));
    }
}
