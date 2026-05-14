package com.retailer.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Retailer Rewards Program Spring Boot application.
 * The application exposes REST endpoints that compute reward points earned
 * by customers based on their transaction history.
 */
@SpringBootApplication
public class RewardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }
}
