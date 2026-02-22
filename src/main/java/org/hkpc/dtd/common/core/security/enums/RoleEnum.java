package org.hkpc.dtd.common.core.security.enums;

/**
 * the role of the user, align with the user_role table
 */
public enum RoleEnum {
    ADMIN("1"),

    USER("2");

    /**
     * the primary key of the user_role table
     */
    private final String roleId;

    RoleEnum(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }
}
