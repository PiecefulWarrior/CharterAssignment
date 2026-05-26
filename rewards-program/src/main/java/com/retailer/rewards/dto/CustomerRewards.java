package com.retailer.rewards.dto;

import java.time.Month;
import java.util.Map;

/**
 * Aggregated reward summary for a single customer.
 * <p>
 * Holds the points earned per calendar month (keyed by {@link Month})
 * and the cumulative total across all months in the queried period.
 * </p>
 *
 * @param customerId    identifier of the customer
 * @param monthlyPoints map of calendar month to points earned in that month
 * @param totalPoints   sum of points earned across all months in the period
 */
public record CustomerRewards(
        Long customerId,
        Map<String, Integer> monthlyPoints,
        long totalPoints
) {
}
