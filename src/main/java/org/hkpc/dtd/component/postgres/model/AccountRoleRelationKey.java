package org.hkpc.dtd.component.postgres.model;

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
public class AccountRoleRelationKey {
    /**
     * Foreign key referencing user_account
     */
    @Schema(description = """
            Foreign key referencing user_account
            """)
    private Integer userAccountId;

    /**
     * Foreign key referencing user_role
     */
    @Schema(description = """
            Foreign key referencing user_role
            """)
    private Integer userRoleId;

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }
}
