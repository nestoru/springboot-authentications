package com.nestorurquiza;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordHashMatch {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: BcryptPasswordHashMatch <password> <hash>");
            return;
        }

        String password = args[0];
        String hash = args[1];

        System.out.println("Checking if password matches hash...");
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(password, hash)) {
            System.out.println("The password matches the hash.");
        } else {
            System.out.println("The password does not match the hash.");
        }
    }
}

