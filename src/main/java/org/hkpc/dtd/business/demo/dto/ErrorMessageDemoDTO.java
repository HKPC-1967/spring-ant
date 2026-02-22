package org.hkpc.dtd.business.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 */
@Schema(description = """
        error message demo
        """)
@Data
public class ErrorMessageDemoDTO {
    /**
     * The @NotEmpty annotation is meant for String, Collection, Map, or Array types.
     * For Integer fields, use @NotNull or @Min and @Max annotations.
     */
    @Schema(description = """
            Used to control the error message display type at front end </br>
            Refer to ErrorShowTypeEnum: </br>
                    SILENT(0),
                    WARN_MESSAGE(1),
                    ERROR_MESSAGE(2),
                    NOTIFICATION(3),
            """)
    @NotNull
    @Min(value = 0)
    // the message is used to override the default message if you want to
    @Max(value = 3, message = "override default message: errorCode cannot be greater than 3")
    private Integer errorShowType;
}
