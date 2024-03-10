package com.nestorurquiza;

import java.time.Instant;
import java.util.Date;

public class AuthTokenResponse {
    private String token;
    private Instant expiry;

    public AuthTokenResponse(String token, Instant expiry) {
        this.token = token;
        this.expiry = expiry;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public Instant getExpiry() {
        return expiry;
    }

    // Setters
    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }
}

