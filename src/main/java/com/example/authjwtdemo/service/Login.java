package com.example.authjwtdemo.service;

import lombok.Data;

@Data
public class Login {
    private final JwtToken accessToken;
    private final JwtToken refreshToken;
    private static final Long ACCESS_TOKEN_VALIDITY=1L;
    private static final Long REFRESH_TOKEN_VALIDITY=144L;

    private Login(JwtToken accessToken, JwtToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Login of(Long userId,String accessSecret,String refreshSecret){
        return new Login(
                JwtToken.of(userId,ACCESS_TOKEN_VALIDITY,accessSecret),
                JwtToken.of(userId,REFRESH_TOKEN_VALIDITY,refreshSecret)
        );
    }
    public static Login of(Long userId, String accessSecret, JwtToken refreshToken){
        return new Login(
                JwtToken.of(userId, ACCESS_TOKEN_VALIDITY,accessSecret), refreshToken
        );
    }
}
