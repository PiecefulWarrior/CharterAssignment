package com.retailer.rewards.repository;

import com.retailer.rewards.model.Transaction;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory store of transactions used to demonstrate the rewards service.
 * <p>
 * In a real application this would be backed by a relational database
 * accessed via Spring Data JPA. For the purposes of this exercise it
 * is seeded with a representative data set covering multiple customers
 * and multiple months of activity.
 * </p>
 */
@Repository
public class TransactionRepository {

    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idSequence = new AtomicLong(1L);

    /**
     * Seeds the repository with a representative data set after construction.
     */
    @PostConstruct
    public void seed() {
        LocalDate today = LocalDate.now();
        LocalDate monthMinus0 = today.withDayOfMonth(5);
        LocalDate monthMinus1 = today.minusMonths(1).withDayOfMonth(10);
        LocalDate monthMinus2 = today.minusMonths(2).withDayOfMonth(15);

        // Customer 1
        save(new Transaction(null, 1L, new BigDecimal("120.00"), monthMinus2)); //  90 pts
        save(new Transaction(null, 1L, new BigDecimal("75.00"),  monthMinus2)); //  25 pts
        save(new Transaction(null, 1L, new BigDecimal("200.00"), monthMinus1)); // 250 pts
        save(new Transaction(null, 1L, new BigDecimal("45.00"),  monthMinus1)); //   0 pts
        save(new Transaction(null, 1L, new BigDecimal("99.00"),  monthMinus0)); //  49 pts

        // Customer 2
        save(new Transaction(null, 2L, new BigDecimal("150.00"), monthMinus2)); // 150 pts
        save(new Transaction(null, 2L, new BigDecimal("60.00"),  monthMinus1)); //  10 pts
        save(new Transaction(null, 2L, new BigDecimal("310.00"), monthMinus0)); // 470 pts

        // Customer 3
        save(new Transaction(null, 3L, new BigDecimal("100.00"), monthMinus2)); //  50 pts
        save(new Transaction(null, 3L, new BigDecimal("50.00"),  monthMinus1)); //   0 pts
        save(new Transaction(null, 3L, new BigDecimal("500.00"), monthMinus0)); // 850 pts
    }

    /**
     * Persists a transaction, assigning an ID if one is not provided.
     *
     * @param transaction the transaction to save
     * @return the saved transaction with its assigned ID
     */
    public Transaction save(Transaction transaction) {
        if (transaction.getTransactionId() == null) {
            transaction.setTransactionId(idSequence.getAndIncrement());
        }
        transactions.add(transaction);
        return transaction;
    }

    /**
     * Returns an unmodifiable view of all stored transactions.
     *
     * @return all transactions
     */
    public List<Transaction> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(transactions));
    }

    /**
     * Returns all transactions for a customer within the inclusive date range.
     *
     * @param customerId customer to filter by
     * @param start      inclusive lower bound on transaction date
     * @param end        inclusive upper bound on transaction date
     * @return matching transactions
     */
    public List<Transaction> findByCustomerAndDateRange(Long customerId, LocalDate start, LocalDate end) {
        return transactions.stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .filter(t -> !t.getTransactionDate().isBefore(start))
                .filter(t -> !t.getTransactionDate().isAfter(end))
                .toList();
    }

    /**
     * Returns all transactions within the inclusive date range.
     *
     * @param start inclusive lower bound on transaction date
     * @param end   inclusive upper bound on transaction date
     * @return matching transactions
     */
    public List<Transaction> findByDateRange(LocalDate start, LocalDate end) {
        return transactions.stream()
                .filter(t -> !t.getTransactionDate().isBefore(start))
                .filter(t -> !t.getTransactionDate().isAfter(end))
                .toList();
    }

    /**
     * Clears all stored transactions. Useful for test isolation.
     */
    public void clear() {
        transactions.clear();
        idSequence.set(1L);
    }
}
