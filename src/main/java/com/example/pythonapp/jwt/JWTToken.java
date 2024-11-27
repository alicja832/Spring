package com.example.pythonapp.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTToken implements Serializable {

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationTime;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public  long getJwtExpirationTime()
    {
        return jwtExpirationTime;
    }
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    // W tych dwóch funkcjach trochę nie wiem za bardzo o co chodzi niestety
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims =  Jwts.parser().setSigningKey(generateJwtKeyEncryption(secretKey)).parseClaimsJws(token).getBody();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return claims;
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
       
    }


    public String  generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name",userDetails.getUsername());
        String subject = userDetails.getUsername();

        String token = null;
        try{
           token =  Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime * 1000))
                 .signWith(SignatureAlgorithm.RS256,  generateJwtKeyEncryption(secretKey)).compact();

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return token;

    }

      public String  generateRefreshToken(UserDetails userDetails) {
        
        Map<String, Object> claims = new HashMap<>();
        String subject = userDetails.getUsername();
        String token = null;
       
        try{
            token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime * 1000 * 3))
                    .signWith(SignatureAlgorithm.RS256,  generateJwtKeyEncryption(secretKey)).compact();

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return token;

    }


    public PublicKey generateJwtKeyDecryption(String jwtKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] keyBytes = Base64.decodeBase64(jwtKey);
        X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

     public PrivateKey generateJwtKeyEncryption(String jwtKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         byte[] keyBytes = Base64.decodeBase64(jwtKey);
         PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(keyBytes);
         return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
     }
    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
