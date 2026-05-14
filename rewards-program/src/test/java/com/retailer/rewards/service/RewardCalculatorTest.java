package com.retailer.rewards.service;

import com.retailer.rewards.exception.InvalidTransactionException;
import com.retailer.rewards.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RewardCalculatorTest {

    private final RewardCalculator calculator = new RewardCalculator();

    @ParameterizedTest(name = "amount=${0} -> {1} points")
    @CsvSource({
            "0,     0",
            "50,    0",
            "51,    1",
            "75,    25",
            "100,   50",
            "101,   52",
            "120,   90",
            "150,   150",
            "200,   250",
            "500,   850"
    })
    @DisplayName("calculates the correct points for each amount tier")
    void calculatesPointsCorrectly(String amount, long expected) {
        Transaction t = new Transaction(1L, 1L, new BigDecimal(amount), LocalDate.now());
        assertThat(calculator.calculate(t)).isEqualTo(expected);
    }

    @Test
    @DisplayName("the canonical $120 example yields 90 points")
    void exampleFromSpec() {
        Transaction t = new Transaction(1L, 1L, new BigDecimal("120"), LocalDate.now());
        assertThat(calculator.calculate(t)).isEqualTo(90L);
    }

    @Test
    @DisplayName("a null transaction throws InvalidTransactionException")
    void nullTransactionThrows() {
        assertThatThrownBy(() -> calculator.calculate(null))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    @DisplayName("a null amount throws InvalidTransactionException")
    void nullAmountThrows() {
        Transaction t = new Transaction(1L, 1L, null, LocalDate.now());
        assertThatThrownBy(() -> calculator.calculate(t))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    @DisplayName("a negative amount throws InvalidTransactionException")
    void negativeAmountThrows() {
        Transaction t = new Transaction(1L, 1L, new BigDecimal("-10"), LocalDate.now());
        assertThatThrownBy(() -> calculator.calculate(t))
                .isInstanceOf(InvalidTransactionException.class);
    }
}
