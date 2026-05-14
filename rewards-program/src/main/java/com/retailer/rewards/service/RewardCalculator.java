package com.retailer.rewards.service;

import com.retailer.rewards.exception.InvalidTransactionException;
import com.retailer.rewards.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Encapsulates the business rule for converting a single transaction
 * amount into reward points.
 * <p>
 * Rule:
 * <ul>
 *   <li>2 points for every whole dollar spent over $100.</li>
 *   <li>1 point for every whole dollar spent between $50 and $100.</li>
 *   <li>0 points for any amount at or below $50.</li>
 * </ul>
 * Example: a $120 purchase earns 2 &times; 20 + 1 &times; 50 = 90 points.
 * </p>
 */
@Service
public class RewardCalculator {

    private static final BigDecimal TIER_ONE_THRESHOLD = new BigDecimal("50");
    private static final BigDecimal TIER_TWO_THRESHOLD = new BigDecimal("100");

    /**
     * Calculates reward points for a single transaction.
     *
     * @param transaction the transaction to evaluate; must be non-null
     * @return points earned for this transaction (zero or positive)
     * @throws InvalidTransactionException if the transaction or its amount is null,
     *                                     or the amount is negative
     */
    public long calculate(Transaction transaction) {
        if (transaction == null) {
            throw new InvalidTransactionException("Transaction must not be null");
        }
        BigDecimal amount = transaction.getAmount();
        if (amount == null) {
            throw new InvalidTransactionException(
                    "Amount must not be null for transaction " + transaction.getTransactionId());
        }
        if (amount.signum() < 0) {
            throw new InvalidTransactionException(
                    "Amount must not be negative for transaction " + transaction.getTransactionId());
        }

        // Only the integer dollar portion is counted, per the example in the spec.
        long dollars = amount.toBigInteger().longValueExact();

        long points = 0L;
        if (dollars > 100L) {
            points += 2L * (dollars - 100L);
            points += 50L; // full tier-one band ($50 to $100)
        } else if (dollars > 50L) {
            points += (dollars - 50L);
        }
        return points;
    }
}
