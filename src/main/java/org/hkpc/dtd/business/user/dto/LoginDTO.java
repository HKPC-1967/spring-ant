package org.hkpc.dtd.business.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Schema(description = """
        Table name: user_account<br/>Description: the login account and the basic information of the user
        """)
@Data
public class LoginDTO {
    /**
     * The @NotEmpty annotation is meant for String, Collection, Map, or Array types.
     * For Integer fields, use @NotNull or @Min and @Max annotations.
     */

    @Schema(description = """
            Username for login, must be unique
            """)
    @NotEmpty
    private String username;


    @Schema(description = """
            Password with encryption
            """)
    @NotEmpty
    private String password;

}
