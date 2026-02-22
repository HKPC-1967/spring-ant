package org.hkpc.dtd.component.postgres.model;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Table name: db_config
 * <p>
 * Description: Stores initialization settings for the application
 *
 */
@Schema(description = """
        Table name: db_config<br/>Description: Stores initialization settings for the application
        """)
public class DbConfig {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Boolean disabled;

    /**
     * The unique key for the setting
     */
    @Schema(description = """
            The unique key for the setting
            """)
    private String key;

    /**
     * The value associated with the key
     */
    @Schema(description = """
            The value associated with the key
            """)
    private String value;

    /**
     * Description of the setting
     */
    @Schema(description = """
            Description of the setting
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
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
