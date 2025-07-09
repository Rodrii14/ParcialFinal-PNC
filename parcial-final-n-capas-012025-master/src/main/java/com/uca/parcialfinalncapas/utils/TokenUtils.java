package com.uca.parcialfinalncapas.utils;


import com.uca.parcialfinalncapas.entities.Token;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.repository.iTokenRepository;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Data
@Component
public class TokenUtils {

    private final iTokenRepository TokenRepository;

    @Value("${jwt.secret-key}")
    private String secret;

    @Value("${jwt.expiration-time}")
    private long expiresIn;

    public String generateToken(User user){
        Map<String, Objects> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiresIn))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public Boolean validateToken(String token){
        try{
            verifyTokenExists(token);

            JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build();

            parser.parse(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void verifyTokenExists(String token) throws Exception {
        Token t = TokenRepository.findByToken(token);

        if (t == null){
            throw new Exception("token does not exist");
        }
    }

    public String getUsernameFromToken(String token){
        try{
            JwtParser parser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build();

            return parser.parseSignedClaims(token).getPayload().getSubject();
        }catch(Exception e){
            return null;
        }
    }
}