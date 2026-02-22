package org.hkpc.dtd.component.postgres.model;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Table name: demo_rule
 * <p>
 * Description: Table to store demo records of rule
 *
 */
@Schema(description = """
        Table name: demo_rule<br/>Description: Table to store demo records of rule
        """)
public class DemoRule {
    /**
     * Use "id" for new table ("key" is just aligned with Ant Design Pro); auto-incremented primary key
     */
    @Schema(description = """
            Use "id" for new table ("key" is just aligned with Ant Design Pro); auto-incremented primary key
            """)
    private Integer key;

    /**
     * Indicates if the record is disabled (true/false)
     */
    @Schema(description = """
            Indicates if the record is disabled (true/false)
            """)
    private Boolean disabled;

    /**
     * URL link associated with the record
     */
    @Schema(description = """
            URL link associated with the record
            """)
    private String href;

    /**
     * URL of the avatar image
     */
    @Schema(description = """
            URL of the avatar image
            """)
    private String avatar;

    /**
     * Name of the item or entity
     */
    @Schema(description = """
            Name of the item or entity
            """)
    private String name;

    /**
     * Owner of the item or entity
     */
    @Schema(description = """
            Owner of the item or entity
            """)
    private String owner;

    /**
     * Description of the rule
     */
    @Schema(description = """
            Description of the rule
            """)
    private String desc;

    /**
     * Call number or a related numeric identifier
     */
    @Schema(description = """
            Call number or a related numeric identifier
            """)
    private Integer callNo;

    /**
     * Status of the item, typically a short string
     */
    @Schema(description = """
            Status of the item, typically a short string
            """)
    private String status;

    /**
     * Use updateTime for new table (here is just aligned with Ant Design Pro); Timestamp of the last update to the record
     */
    @Schema(description = """
            Use updateTime for new table (here is just aligned with Ant Design Pro); Timestamp of the last update to the record
            """)
    private Date updatedAt;

    /**
     * Use createTime for new table (here is just aligned with Ant Design Pro); Timestamp of when the record was created
     */
    @Schema(description = """
            Use createTime for new table (here is just aligned with Ant Design Pro); Timestamp of when the record was created
            """)
    private Date createdAt;

    /**
     * Progress value, typically a percentage or score
     */
    @Schema(description = """
            Progress value, typically a percentage or score
            """)
    private Integer progress;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Integer getCallNo() {
        return callNo;
    }

    public void setCallNo(Integer callNo) {
        this.callNo = callNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
