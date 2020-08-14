package com.util;

import com.auth0.jwt.internal.org.apache.commons.codec.binary.Base64;
import com.servlet.CommonServlet;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {
    private static final String signingKey = "signingKey";

    public static String generateToken( String subject, long timeToAlive) {
        SecretKey key = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(now)
            .signWith(SignatureAlgorithm.HS256, key);
        if (timeToAlive > 0) {
            Date exp = new Date(nowMillis + timeToAlive);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static String getSubject(HttpServletRequest req, String jwtTokenCookieName){
        SecretKey key = generalKey();
        String token = req.getHeader(CommonServlet.headerName);
        if(token == null) return null;
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtTokenCookieName).getBody().getSubject();
    }

    static private SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(signingKey);

        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        return key;
    }
}
