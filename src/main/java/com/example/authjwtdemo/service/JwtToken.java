package com.example.authjwtdemo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class JwtToken {
    @Getter
    private final String token;
    @Getter
    private final Long userId;
    @Getter
    private final LocalDateTime issuedAt;
    @Getter
    private final LocalDateTime expiredAt;

    public JwtToken(String token, Long userId, LocalDateTime issuedAt, LocalDateTime expiredAt) {
        this.token = token;
        this.userId = userId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }


//    SHA256 Generator
    public static String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public static JwtToken of(
            Long userId,
            Long validInMinutes,
            String secretKey
    ){
        var issueDate = Instant.now();
        var expiredDate= issueDate.plus(validInMinutes,ChronoUnit.MINUTES);
        return new JwtToken(
                Jwts.builder().claim("user_id",userId)
                .setIssuedAt(Date.from(issueDate))
                .setExpiration(Date.from(issueDate.plus(validInMinutes, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, sha256(secretKey))
                .compact(),
                userId,
                LocalDateTime.ofInstant(issueDate, ZoneId.systemDefault()),
                LocalDateTime.ofInstant(expiredDate,ZoneId.systemDefault())
        );
    }

    public static JwtToken from(String token,String secretKey){
        var claims=(Claims) Jwts.parserBuilder()
                .setSigningKey(sha256(secretKey))
                .build().parse(token).getBody();
        var userId=claims.get("user_id", Long.class);
        var issuedAt=claims.getIssuedAt();
        var expiredAt=claims.getExpiration();
        return new JwtToken(token,userId,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(issuedAt.getTime()),ZoneId.systemDefault()),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(expiredAt.getTime()),ZoneId.systemDefault()));
    }

//    public static JwtToken of(String token){
//        return new JwtToken(token);
//    }
}
