package com.codevui.realworldapp.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codevui.realworldapp.entity.User;
import com.codevui.realworldapp.model.TokenPayload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
    @Value("${JWT_SECRET_KEY}")
    private String secret;

    public String generateToken(User user, long expiredDate) {
        Map<String, Object> claims = new HashMap<String, Object>();
        TokenPayload payload = TokenPayload.builder().id(user.getId()).email(user.getEmail()).build();
        claims.put("payload", payload);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredDate * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public TokenPayload getTokenPayload(String token) {
        return getClaimsFromToken(token, (Claims claims) -> {
            Map<String, Object> mapResult = (Map<String, Object>) claims.get("payload");
            return TokenPayload.builder().id((int) mapResult.get("id"))
                    .email((String) mapResult.get("email"))
                    .build();
        });
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);
    }

    public boolean validate(String token, User user) {
        TokenPayload payload = getTokenPayload(token);
        return payload.getId() == user.getId() && payload.getEmail().equals(user.getEmail())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiredDate = getClaimsFromToken(token, (Claims claims) -> {
            return claims.getExpiration();
        });
        return expiredDate.before(new Date());
    }
}
