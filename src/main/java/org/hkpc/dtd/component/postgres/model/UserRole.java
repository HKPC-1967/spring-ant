package org.hkpc.dtd.component.postgres.model;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Table name: user_role
 * <p>
 * Description: the role of the user for permission control
 *
 */
@Schema(description = """
        Table name: user_role<br/>Description: the role of the user for permission control
        """)
public class UserRole {
    /**
     * Auto-incremented unique identifier
     */
    @Schema(description = """
            Auto-incremented unique identifier
            """)
    private Integer id;

    /**
     * 
     */
    private Boolean disabled;

    /**
     * Password with encryption
     */
    @Schema(description = """
            Password with encryption
            """)
    private String roleName;

    /**
     * nickname for display
     */
    @Schema(description = """
            nickname for display
            """)
    private String description;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
