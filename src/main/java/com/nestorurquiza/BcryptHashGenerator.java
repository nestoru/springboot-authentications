package com.nestorurquiza;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptHashGenerator {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: BcryptHashGenerator <password>");
            return;
        }

        String password = args[0];
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashedPassword);
    }
}

