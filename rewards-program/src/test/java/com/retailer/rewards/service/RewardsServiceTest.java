package com.retailer.rewards.service;

import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidTransactionException;
import com.retailer.rewards.dto.CustomerRewards;
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


}
