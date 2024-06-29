package org.jeecg.modules.estar.bs.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.jeecg.modules.estar.bs.constant.BsConstant;
import org.jeecg.modules.estar.bs.constant.ResponseCode;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Jwt工具类
 *
 * @author nbacheng
 * @since 2023-03-23
 */
public class JwtUtil {

    private static final String JWT_SECRET = "aj-report";

    public static String createToken(String reportCode, String shareCode, Date expires) {
        return createToken(reportCode, shareCode, null, expires);
    }

    public static String createToken(String reportCode, String shareCode, String password, Date expires) {
        String token = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(expires)
                .withClaim("reportCode", reportCode)
                .withClaim("shareCode", shareCode)
                .withClaim("sharePassword", password)
                .sign(Algorithm.HMAC256(JWT_SECRET));
        return token;
    }


    public static Map<String, Claim> getClaim(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
            DecodedJWT decodedJwt = jwtVerifier.verify(token);
            return decodedJwt.getClaims();
        } catch (Exception e) {
        	Result.error(ResponseCode.REPORT_SHARE_LINK_INVALID, e.getMessage());
        	return null;
        }
    }

    public static String getReportCode(String token) {
        Claim claim = getClaim(token).get("reportCode");
        if (null == claim) {
            Result.error(ResponseCode.REPORT_SHARE_LINK_INVALID);
            return null;
        }
        return claim.asString();
    }

    /**
     * 存在多个分享token
     * @param tokenList
     * @return
     */
    public static List<String> getReportCodeList(String tokenList) {
        return Arrays.stream(tokenList.split(BsConstant.SPLIT)).filter(StringUtils::isNotBlank).map(JwtUtil::getReportCode).distinct().collect(Collectors.toList());
    }

    public static String getShareCode(String token) {
        Claim claim = getClaim(token).get("shareCode");
        if (null == claim) {
        	Result.error(ResponseCode.REPORT_SHARE_LINK_INVALID);
        }
        return claim.asString();
    }

    public static String getPassword(String token) {
        Claim claim = getClaim(token).get("sharePassword");
        if (null == claim) {
            return null;
        }
        if (StringUtils.isNotBlank(claim.asString())) {
            return claim.asString();
        }
        return null;
    }

}
