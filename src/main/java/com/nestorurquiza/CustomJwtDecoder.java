package com.nestorurquiza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import java.time.Instant;

public class CustomJwtDecoder implements JwtDecoder {

    private final JwtTokenRepository jwtTokenRepository;

    @Autowired
    public CustomJwtDecoder(JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return jwtTokenRepository.findByToken(token)
                .map(jwtToken -> {
                    // Custom logic to validate and decode the token
                    // For example, you could check if the token is in the database and valid
                    if (jwtToken.getExpiry().isBefore(Instant.now())) {
                        throw new JwtException("Expired token");
                    }
                    // Assuming you have a method to convert your JwtToken instance to a Jwt
                    return Jwt.withTokenValue(jwtToken.getToken())
                            .header("alg", "none") // Placeholder for algorithm
                            .subject(jwtToken.getUsername()) // Example claim
                            .build();
                })
                .orElseThrow(() -> new JwtException("Invalid token"));
    }
}
