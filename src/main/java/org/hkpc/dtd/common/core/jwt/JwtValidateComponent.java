package org.hkpc.dtd.common.core.jwt;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.hkpc.dtd.common.consts.CommonConst;
import org.hkpc.dtd.common.core.jwt.model.UserJwtSubject;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.hkpc.dtd.common.service.DbConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtValidateComponent {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final static long ACCESS_TOKEN_EXPIRE_TIME = 1 * 60 * 60 * 1000;
    public final static long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000; // 7 days

    private final DbConfigService dbConfigService;

    @Autowired
    public JwtValidateComponent(DbConfigService dbConfigService) {
        this.dbConfigService = dbConfigService;
    }


    public UserJwtSubject ValidateJwtAndGetSubject(String jwtToken) throws CodeException {
        try {
            String jwtKey = dbConfigService.getValueByKey(DbConfigService.KeyEnum.JWT_KEY);
            SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
            // 解析JWT字符串
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

            // 校验JWT是否过期
            Date expiration = claims.getExpiration();
            if (!expiration.after(new Date())){
                throw new CodeException(ErrorCodeEnum.INVALID_JWT_TOKEN);
            }
            String subjectStr = claims.getSubject();
//            JSONObject subjectJSON = JSON.parseObject(subjectStr);
//            UserJwtSubject userJwtSubject = new UserJwtSubject(Integer.parseInt(subjectJSON.get("subjectVersion").toString()),Integer.parseInt(subjectJSON.get("id").toString()),subjectJSON.get("username").toString());
            UserJwtSubject userJwtSubject = JSON.parseObject(subjectStr,UserJwtSubject.class);
            return userJwtSubject;

        } catch (ExpiredJwtException e) {
            logger.info("expired jwt token {}",jwtToken);
            // 如果解析或校验过程中抛出异常，则认为JWT无效
            throw new CodeException(ErrorCodeEnum.INVALID_JWT_TOKEN,e);
        } catch (Exception e) {
            logger.error(CommonConst.MARK_ALARM_SYSTEM, "getJwtSubject error {},{},{}",jwtToken,e.getMessage(),jwtToken, e);
            // 如果解析或校验过程中抛出异常，则认为JWT无效
            throw new CodeException(ErrorCodeEnum.INVALID_JWT_TOKEN,e);
        }
    }

    public String generateAccessToken(String subject) throws CodeException {
        return generateJwtToken(subject, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generateRefreshToken(String subject) throws CodeException {
        return generateJwtToken(subject, REFRESH_TOKEN_EXPIRE_TIME);
    }

    private String generateJwtToken(String subject, long expirationMillis) throws CodeException {
        String jwtKey = dbConfigService.getValueByKey(DbConfigService.KeyEnum.JWT_KEY);
        SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(subject)
                .claim("created", new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

}
