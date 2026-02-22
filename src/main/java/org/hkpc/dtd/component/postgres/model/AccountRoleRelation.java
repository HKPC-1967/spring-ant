package org.hkpc.dtd.component.postgres.model;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Table name: account_role_relation
 * <p>
 * Description: Relation table between user_account and user_role
 *
 */
@Schema(description = """
        Table name: account_role_relation<br/>Description: Relation table between user_account and user_role
        """)
public class AccountRoleRelation extends AccountRoleRelationKey {
    /**
     * 
     */
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}