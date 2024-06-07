package org.acme.Service;


import io.jsonwebtoken.*;

import java.util.Date;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.Optional;
import org.acme.Model.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import java.util.Date;
@ApplicationScoped
public class JwtToken {

    @ConfigProperty(name = "ACCESS_TOKEN_SECRET")
    String accessTokenSecret;

    @ConfigProperty(name = "ACCESS_TOKEN_LIFE")
    long expiredIn;
//    private static final String JWT_SECRET = "lodaaaaaa";
//    private static final long JWT_EXPIRATION = 604800000L;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiredIn);
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, accessTokenSecret)
                .compact();
    }




}