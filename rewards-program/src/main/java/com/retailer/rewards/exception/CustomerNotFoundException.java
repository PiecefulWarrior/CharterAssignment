package com.retailer.rewards.exception;

/**
 * Thrown when a requested customer has no transactions on record.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
