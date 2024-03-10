package com.nestorurquiza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenUtil {

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Value("${app.session.jwt.expiration}")
    private int jwtExpirationInSeconds;

    // Generate a token and save it in the database with an expiration date
    public JwtToken generateToken(String username) {
        String token = java.util.UUID.randomUUID().toString();
        Instant now = Instant.now();
        Instant expiryDate = now.plus(jwtExpirationInSeconds, ChronoUnit.SECONDS);
 
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setUsername(username);
        jwtToken.setExpiry(expiryDate);
        jwtTokenRepository.save(jwtToken);
        
        return jwtToken;
    }

    // Validate the token by checking it exists and hasn't expired
    public boolean validateToken(String token) {
        return jwtTokenRepository.findByToken(token)
                .map(jwtToken -> jwtToken.getExpiry().isAfter(Instant.now()))
                .orElse(false);
    }
}

