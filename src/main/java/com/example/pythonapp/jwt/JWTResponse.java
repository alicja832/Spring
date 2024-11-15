package com.example.pythonapp.jwt;
import lombok.Data;
import java.util.Date;

@Data
public class JWTResponse {

    private String jwtToken;
    private String refreshToken;
    private Date jwtExpirationDate;
    public JWTResponse(String token,String refreshToken,Date jwtExpirationDate) {

        this.jwtToken = token;
        this.refreshToken = refreshToken;
        this.jwtExpirationDate = jwtExpirationDate;
    }
    public String getJwtToken()
    {
        return jwtToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getJwtExpirationDate() {
        return jwtExpirationDate;
    }
}
