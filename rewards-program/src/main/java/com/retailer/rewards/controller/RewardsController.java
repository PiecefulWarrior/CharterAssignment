package com.retailer.rewards.controller;

import com.retailer.rewards.model.CustomerRewards;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.TransactionRepository;
import com.retailer.rewards.service.RewardsService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST endpoints for the Retailer Rewards Program.
 * Exposes operations to retrieve reward summaries for all customers
 * or for a specific customer over a configurable date range, and to
 * register new transactions.
 */
@RestController
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    private final RewardsService rewardsService;
    private final TransactionRepository transactionRepository;

    public RewardsController(RewardsService rewardsService, TransactionRepository transactionRepository) {
        this.rewardsService = rewardsService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Returns reward daata for every customer with transactions in the period.
     * If no dates are supplied, defaults to the last three calendar months ending today.
     *
     * @param start inclusive start date (optional)
     * @param end   inclusive end date (optional)
     * @return list of per-customer reward summaries
     */
    @GetMapping
    public ResponseEntity<List<CustomerRewards>> getAllCustomerRewards(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {

        LocalDate effectiveEnd = (end != null) ? end : LocalDate.now();
        LocalDate effectiveStart = (start != null) ? start : effectiveEnd.minusMonths(3).plusDays(1);
        return ResponseEntity.ok(rewardsService.calculateRewardsForAllCustomers(effectiveStart, effectiveEnd));
    }

    /**
     * Returns the reward data for a single customer.
     *
     * @param customerId the customer to query
     * @param start      inclusive start date (optional)
     * @param end        inclusive end date (optional)
     * @return reward summary for the customer
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerRewards> getCustomerRewards(
            @PathVariable Long customerId,
            @RequestParam(required = false)
            LocalDate start,
            @RequestParam(required = false) LocalDate end) {

        LocalDate effectiveEnd = (end != null) ? end : LocalDate.now();
        LocalDate effectiveStart = (start != null) ? start : effectiveEnd.minusMonths(3).plusDays(1);
        return ResponseEntity.ok(rewardsService.calculateRewardsForCustomer(customerId, effectiveStart, effectiveEnd));
    }

    /**
     * adds a new transaction
     *
     * @param transaction the transaction to add (validated)
     * @return the saved transaction with an assigned ID
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> addTransaction(@Valid @RequestBody Transaction transaction) {
        Transaction saved = transactionRepository.save(transaction);
        return ResponseEntity.ok(saved);
    }
}
