package org.hkpc.dtd.business.user.vo;

import lombok.Data;

@Data
public class LoginVO {

    private String status;
    private String type;
//    private String currentAuthority;
    private String accessToken;
    private String accessTokenExpiration;
    private String refreshToken;
    private String refreshTokenExpiration;


}
