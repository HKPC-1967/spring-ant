package org.hkpc.dtd.business.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * <p>
 */
@Schema(description = """
        Table name: demo_rule<br/>Description: Table to store demo records of rule
        """)
@Data
public class RuleInsertDTO {
    /**
     * The @NotEmpty annotation is meant for String, Collection, Map, or Array types.
     * For Integer fields, use @NotNull or @Min and @Max annotations.
     */
    @NotNull
    @Schema(description = """
            Name of the item or entity
            """)
    private String name;

    @Schema(description = """
            Description of the rule
            """)
    private String desc;
}
