package com.retailer.rewards.service;

import com.retailer.rewards.entity.Transaction;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidTransactionException;
import com.retailer.rewards.dto.CustomerRewards;
import com.retailer.rewards.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@AllArgsConstructor
public class RewardsService {

    private final TransactionRepository repository;
    private final RewardCalculator calculator;
    /**
     * Computes reward points earned by every customer with at least one transaction
     * in the given date range.
     *
     * @param start inclusive start date of the period
     * @param end   inclusive end date of the period
     * @return list of per-customer reward summaries
     * @throws InvalidTransactionException if the date range is invalid
     */
    public CustomerRewards calculateRewardsForCustomer(long customerId) {

        repository.findByCustomerId(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with Customer ID: " + customerId));

        LocalDate start = LocalDate.now().minusMonths(3).withDayOfMonth(1);
        LocalDate end = LocalDate.now();

        List<Transaction> transactions =
                repository.findByCustomerIdAndTransactionDateBetween(
                        customerId, start, end
                );



        Map<String, Integer> monthlyPoints =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                t -> t.getTransactionDate().getMonth().toString(),
                                Collectors.summingInt(
                                        t -> calculator.calculate(t.getAmount())
                                )
                        ));

        int totalPoints = monthlyPoints.values()
                .stream()
                .reduce(0, Integer::sum);

        return new CustomerRewards(customerId, monthlyPoints, totalPoints);
    }

}
