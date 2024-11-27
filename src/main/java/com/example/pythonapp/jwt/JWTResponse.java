package com.example.pythonapp.jwt;
import lombok.Data;
import java.util.Date;

@Data
public class JWTResponse {

    private String token;
    private Date jwtExpirationDate;
    public JWTResponse(String token,Date jwtExpirationDate) {

        this.token = token;
        this.jwtExpirationDate = jwtExpirationDate;
    }
    public String getToken()
    {
        return token;
    }
    public void setToken(String token)
    {
        this.token = token;
    }

    public void setJwtExpirationDate(Date jwtExpirationDate) {
        this.jwtExpirationDate = jwtExpirationDate;
    }

    public Date getJwtExpirationDate() {
        return jwtExpirationDate;
    }
}
