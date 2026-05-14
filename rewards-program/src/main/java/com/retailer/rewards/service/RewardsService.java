package com.retailer.rewards.service;

import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidTransactionException;
import com.retailer.rewards.model.CustomerRewards;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregates reward points across customers and time periods.
 * <p>
 * The service computes per-month and total reward points for one or more
 * customers based on transactions stored in {@link TransactionRepository}.
 * The set of months is derived dynamically from the queried date range
 * — months are never hardcoded.
 * </p>
 */
@Service
public class RewardsService {

    private final TransactionRepository repository;
    private final RewardCalculator calculator;

    public RewardsService(TransactionRepository repository, RewardCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    /**
     * Computes reward points earned by every customer with at least one transaction
     * in the given date range.
     *
     * @param start inclusive start date of the period
     * @param end   inclusive end date of the period
     * @return list of per-customer reward summaries
     * @throws InvalidTransactionException if the date range is invalid
     */
    public List<CustomerRewards> calculateRewardsForAllCustomers(LocalDate start, LocalDate end) {
        validateRange(start, end);
        List<Transaction> transactions = repository.findByDateRange(start, end);

        Map<Long, Map<Month, Long>> byCustomer = new HashMap<>();
        for (Transaction t : transactions) {
            long points = calculator.calculate(t);
            byCustomer
                    .computeIfAbsent(t.getCustomerId(), id -> new EnumMap<>(Month.class))
                    .merge(t.getTransactionDate().getMonth(), points, Long::sum);
        }

        return byCustomer.entrySet().stream()
                .map(e -> toCustomerRewards(e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * Computes reward points earned by a single customer over the given period.
     *
     * @param customerId the customer to query
     * @param start      inclusive start date
     * @param end        inclusive end date
     * @return reward summary for that customer
     * @throws CustomerNotFoundException   if the customer has no transactions in the range
     * @throws InvalidTransactionException if the date range is invalid
     */
    public CustomerRewards calculateRewardsForCustomer(Long customerId, LocalDate start, LocalDate end) {
        validateRange(start, end);
        if (customerId == null) {
            throw new InvalidTransactionException("customerId must not be null");
        }

        List<Transaction> transactions = repository.findByCustomerAndDateRange(customerId, start, end);
        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException(
                    "No transactions found for customer " + customerId + " in the specified period");
        }

        Map<Month, Long> monthly = new EnumMap<>(Month.class);
        for (Transaction t : transactions) {
            monthly.merge(t.getTransactionDate().getMonth(), calculator.calculate(t), Long::sum);
        }
        return toCustomerRewards(customerId, monthly);
    }

    private CustomerRewards toCustomerRewards(Long customerId, Map<Month, Long> monthly) {
        long total = monthly.values().stream().mapToLong(Long::longValue).sum();
        return new CustomerRewards(customerId, monthly, total);
    }

    private void validateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new InvalidTransactionException("start and end dates must not be null");
        }
        if (end.isBefore(start)) {
            throw new InvalidTransactionException("end date must not be before start date");
        }
    }
}
