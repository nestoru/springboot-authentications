package com.nestorurquiza;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;

@Service
public class TokenCleanupService {

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    // Example: Run every hour
    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredTokens() {
        jwtTokenRepository.deleteAllExpiredTokens(Instant.now());
    }
}
