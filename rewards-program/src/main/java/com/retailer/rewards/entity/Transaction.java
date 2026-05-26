package com.retailer.rewards.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull(message = "customerId must not be null")
    private Long customerId;

    @NotNull(message = "amount must not be null")
    @PositiveOrZero(message = "amount must be zero or positive")
    private BigDecimal amount;

    @NotNull(message = "transactionDate must not be null")
    private LocalDate transactionDate;

}
