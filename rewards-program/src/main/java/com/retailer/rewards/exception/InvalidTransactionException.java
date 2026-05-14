package com.retailer.rewards.exception;

/**
 * Thrown when a transaction contains invalid data (e.g., negative amount,
 * null fields) that the reward calculator cannot process.
 */
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }
}
