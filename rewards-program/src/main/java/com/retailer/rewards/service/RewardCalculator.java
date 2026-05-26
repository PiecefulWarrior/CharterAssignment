package com.retailer.rewards.service;

import com.retailer.rewards.exception.InvalidTransactionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Encapsulates the business rule for converting a single transaction
 * amount into reward points.
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

    /**
     * Calculates reward points for a single transaction.
     *
     * @param transaction the transaction to evaluate; must be non-null
     * @return points earned for this transaction (zero or positive)
     * @throws InvalidTransactionException if the transaction or its amount is null,
     *                                     or the amount is negative
     */
    public int calculate(BigDecimal amount) {

        BigDecimal leastAmount = BigDecimal.valueOf(50);
        if (amount == null || amount.compareTo(leastAmount) <= 0) {
            return 0;
        }
        // Only the integer dollar portion is counted, per the example in the spec.
        int dollars = amount.toBigInteger().intValue();
        int points = 0;
        if (dollars > 100) {
            points += 2 * (dollars - 100);
            points += 50; // full tier-one band ($50 to $100)
        } else if (dollars > 50) {
            points += (dollars - 50);
        }
        return points;
    }
}
