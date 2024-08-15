package com.example.demoggggg.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class JWTResponse {

    private String jwtToken;

    public JWTResponse(String token) {
        jwtToken = token;
    }
}
