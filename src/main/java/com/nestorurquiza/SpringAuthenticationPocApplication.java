package com.nestorurquiza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringAuthenticationPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthenticationPocApplication.class, args);
    }

}

