package com.retailer.rewards.service;

import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidTransactionException;
import com.retailer.rewards.model.CustomerRewards;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class RewardsServiceTest {

    private TransactionRepository repository;
    private RewardsService service;

    @BeforeEach
    void setup() {
        repository = new TransactionRepository();
        repository.clear();
        service = new RewardsService(repository, new RewardCalculator());
    }

    @Test
    @DisplayName("calculates monthly and total points for a single customer with multiple months")
    void calculatesForSingleCustomerMultipleMonths() {
        // Use fixed dates so the test is deterministic
        repository.save(new Transaction(null, 1L, new BigDecimal("120"), LocalDate.of(2025, 1, 5)));  //  90
        repository.save(new Transaction(null, 1L, new BigDecimal("75"),  LocalDate.of(2025, 1, 20))); //  25
        repository.save(new Transaction(null, 1L, new BigDecimal("200"), LocalDate.of(2025, 2, 10))); // 250
        repository.save(new Transaction(null, 1L, new BigDecimal("99"),  LocalDate.of(2025, 3, 1)));  //  49

        CustomerRewards r = service.calculateRewardsForCustomer(
                1L, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 31));

        assertThat(r.customerId()).isEqualTo(1L);
        assertThat(r.monthlyPoints()).containsEntry(Month.JANUARY, 115L);
        assertThat(r.monthlyPoints()).containsEntry(Month.FEBRUARY, 250L);
        assertThat(r.monthlyPoints()).containsEntry(Month.MARCH, 49L);
        assertThat(r.totalPoints()).isEqualTo(414L);
    }

    @Test
    @DisplayName("aggregates all customers correctly across the period")
    void calculatesForAllCustomers() {
        repository.save(new Transaction(null, 1L, new BigDecimal("120"), LocalDate.of(2025, 1, 5))); //  90
        repository.save(new Transaction(null, 2L, new BigDecimal("150"), LocalDate.of(2025, 1, 6))); // 150
        repository.save(new Transaction(null, 2L, new BigDecimal("60"),  LocalDate.of(2025, 2, 7))); //  10

        List<CustomerRewards> all = service.calculateRewardsForAllCustomers(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 31));

        assertThat(all).hasSize(2);
        assertThat(all).anySatisfy(c -> {
            assertThat(c.customerId()).isEqualTo(1L);
            assertThat(c.totalPoints()).isEqualTo(90L);
        });
        assertThat(all).anySatisfy(c -> {
            assertThat(c.customerId()).isEqualTo(2L);
            assertThat(c.totalPoints()).isEqualTo(160L);
        });
    }

    @Test
    @DisplayName("throws CustomerNotFoundException when customer has no transactions in the range")
    void unknownCustomerThrows() {
        repository.save(new Transaction(null, 1L, new BigDecimal("120"), LocalDate.of(2025, 1, 5)));
        assertThatThrownBy(() -> service.calculateRewardsForCustomer(
                99L, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 31)))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("rejects an inverted date range")
    void invertedRangeThrows() {
        assertThatThrownBy(() -> service.calculateRewardsForAllCustomers(
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 1, 1)))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    @DisplayName("rejects null dates")
    void nullDatesThrow() {
        assertThatThrownBy(() -> service.calculateRewardsForAllCustomers(null, LocalDate.now()))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    @DisplayName("rejects null customer id")
    void nullCustomerIdThrows() {
        assertThatThrownBy(() -> service.calculateRewardsForCustomer(
                null, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 31)))
                .isInstanceOf(InvalidTransactionException.class);
    }
}
