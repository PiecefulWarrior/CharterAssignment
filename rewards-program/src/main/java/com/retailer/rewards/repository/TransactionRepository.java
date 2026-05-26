package com.retailer.rewards.repository;

import com.retailer.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    Optional<List<Transaction>> findByCustomerId(Long customerId);

    List<Transaction> findByCustomerIdAndTransactionDateBetween(
            Long customerId,
            LocalDate start,
            LocalDate end
    );

}