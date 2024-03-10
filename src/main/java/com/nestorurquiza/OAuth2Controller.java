package com.nestorurquiza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Correct import for UserDetails
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Controller to handle OAuth2 token generation requests.
 */
@RestController
public class OAuth2Controller {
    @Value("${app.session.jwt.expiration}")
    private int jwtExpirationInSeconds;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService; // Ensure this service is correctly defined and imported

    @PostMapping("/api/oauth2/token")
    public ResponseEntity<?> createToken(@RequestParam("grant_type") String grantType,
                                         @RequestParam("client_id") String clientId,
                                         @RequestParam("client_secret") String clientSecret) {
        // Validate grant type
        if (!"client_credentials".equals(grantType)) {
            return ResponseEntity.badRequest().body("Unsupported grant type");
        }

        // Replace direct findByUsernameAndPassword with secure password handling
        Optional<User> userOpt = userRepository.findByUsername(clientId);
        if (userOpt.isEmpty() || !passwordEncoder.matches(clientSecret, userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }

        // Assuming you have a method to generate JWT for the user
        String token = jwtTokenUtil.generateToken(clientId).getToken();

        // Prepare and return the token response
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", token);
        tokenResponse.put("token_type", "Bearer");
        tokenResponse.put("expires_in", "3600"); // Example expiration time

        return ResponseEntity.ok(tokenResponse);
    }
}

