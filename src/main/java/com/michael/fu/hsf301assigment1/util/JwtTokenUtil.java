package com.michael.fu.hsf301assigment1.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

// class nay de xu ly token , lam nhung viec lien quan toi token su dung thu vien jwt
@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    // trong class nay ta su dung interface Jwt voi Builder vaf Parser
    // Builder dung de tao token
    // Parser dung de giai ma
    // Co cac thuoc tinh chinh
    // subject: ten token
    // issueAt: thoi gian tao token, thoi gian token co hieu luc
    // expiration: thoi gian het hieu luc token
    // agorithm: thuat toan tao token
    // ngoai ra con cac thuoc tinh khac nhu: header, payload
    //signWith: la ham set thuat toan( co chut khac voi cac thuoc tinh kia)
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String TOKEN_EXPIRATION_TIME = "1800000";


    // Ham tao ra token
    public static String generateToken(String email) {
        logger.info("Generating token for email: {}", email);
        return Jwts.builder()             // Khoi tao JwtBuilder
                .setSubject(email)   // Gan ten cho token
                .setIssuedAt(new Date())  // gan thoi gian bat dau token
                .setExpiration(new Date(new Date().getTime() + Long.parseLong(TOKEN_EXPIRATION_TIME)))   // thoi gian token het hieu luc
                .signWith(SECRET_KEY)         // them thuat toan tao token
                .compact();                   // chuyen sang string
    }

    // ham kiem tra token co hop le hay khong
    public  boolean validateToken(String token) {
        String email = getEmail(token);
        logger.info("Validating token for email: {}",email );
        return !isTokenExported(token, email);
    }

     // kiem tra thoi gian song cua token con khong
    private  boolean isTokenExported(String token, String email) {
        logger.info("Checking if token is expired for email:{}", email);
        return extractExpiration(token).before(new Date());
    }

    // lay thoi gian het hieu luc cua token
    // dung JwtParser de xu ly
    private  Date extractExpiration(String token) {
        logger.info("Extracting expiration date from token");
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

     // lay ra email cua customer, chinh la ten subject cua token.
    // dung JwtParser de xu ly
    public String getEmail(String token) {
        logger.info("Extracting email from token");
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

}
