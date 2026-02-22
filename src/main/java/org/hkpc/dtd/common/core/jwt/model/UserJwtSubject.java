package org.hkpc.dtd.common.core.jwt.model;

import java.util.List;

/**
 * The subject information stored in the User JWT (JSON Web Token) for user authentication and authorization.
 * 中文：用户 JWT（JSON Web Token）中存储的主题信息，用于用户认证和授权。
 * <p>
 * Fields:
 * <ul>
 *   <li><b>tokenType</b> - The type of the token (e.g., access token, refresh token).</li>
 *   <li><b>subVer</b> - The version of the subject structure.</li>
 *   <li><b>id</b> - The unique identifier of the user.</li>
 *   <li><b>username</b> - The username of the user, not nickname.</li>
 *   <li><b>permVer</b> - The version of the user's permissions. It will increment whenever the user's permissions are updated, so that the old JWT tokens can be invalidated.</li>
 *   <li><b>roleIds</b> - The list of role IDs assigned to the user.</li>
 * </ul>
 */
public record UserJwtSubject(Integer tokenType, Integer subVer, Integer id, String username, Integer permVer, List<Integer> roleIds) {
}
