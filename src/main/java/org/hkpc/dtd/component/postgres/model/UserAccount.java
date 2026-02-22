package org.hkpc.dtd.component.postgres.model;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Table name: user_account
 * <p>
 * Description: the login account and the basic information of the user
 *
 */
@Schema(description = """
        Table name: user_account<br/>Description: the login account and the basic information of the user
        """)
public class UserAccount {
    /**
     * Auto-incremented unique ID
     */
    @Schema(description = """
            Auto-incremented unique ID
            """)
    private Integer id;

    /**
     * 
     */
    private Boolean disabled;

    /**
     * permission_version, if the permission changes, the JWT token will be invalid
     */
    @Schema(description = """
            permission_version, if the permission changes, the JWT token will be invalid
            """)
    private Integer permVer;

    /**
     * Username for login, must be unique
     */
    @Schema(description = """
            Username for login, must be unique
            """)
    private String username;

    /**
     * Password with encryption
     */
    @Schema(description = """
            Password with encryption
            """)
    private String password;

    /**
     * 
     */
    private String email;

    /**
     * The nickname for display
     */
    @Schema(description = """
            The nickname for display
            """)
    private String nickname;

    /**
     * 
     */
    private String avatar;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private Object additionalInfo;

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

    public Integer getPermVer() {
        return permVer;
    }

    public void setPermVer(Integer permVer) {
        this.permVer = permVer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Object getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Object additionalInfo) {
        this.additionalInfo = additionalInfo;
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
