package com.retailer.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void resetData() {
        repository.clear();
        // Seed deterministic data for the period under test
        repository.save(new Transaction(null, 1L, new BigDecimal("120"), LocalDate.of(2025, 1, 5)));
        repository.save(new Transaction(null, 1L, new BigDecimal("200"), LocalDate.of(2025, 2, 10)));
        repository.save(new Transaction(null, 2L, new BigDecimal("75"),  LocalDate.of(2025, 1, 6)));
    }

    @Test
    @DisplayName("GET /api/v1/rewards returns aggregated rewards for all customers")
    void getAllRewards() throws Exception {
        mockMvc.perform(get("/api/v1/rewards")
                        .param("start", "2025-01-01")
                        .param("end", "2025-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)));
    }

    @Test
    @DisplayName("GET /api/v1/rewards/{customerId} returns the expected totals")
    void getRewardsForCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/1")
                        .param("start", "2025-01-01")
                        .param("end", "2025-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalPoints").value(340))
                .andExpect(jsonPath("$.monthlyPoints.JANUARY").value(90))
                .andExpect(jsonPath("$.monthlyPoints.FEBRUARY").value(250));
    }

    @Test
    @DisplayName("GET for unknown customer returns 404")
    void unknownCustomerReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/9999")
                        .param("start", "2025-01-01")
                        .param("end", "2025-03-31"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET with inverted date range returns 400")
    void invertedRangeReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/rewards")
                        .param("start", "2025-04-01")
                        .param("end", "2025-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/rewards/transactions adds a valid transaction")
    void addTransactionSucceeds() throws Exception {
        Transaction t = new Transaction(null, 3L, new BigDecimal("250"), LocalDate.of(2025, 2, 1));
        mockMvc.perform(post("/api/v1/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").exists());
    }

    @Test
    @DisplayName("POST with negative amount is rejected by validation")
    void negativeAmountRejected() throws Exception {
        Transaction t = new Transaction(null, 3L, new BigDecimal("-1"), LocalDate.of(2025, 2, 1));
        mockMvc.perform(post("/api/v1/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isBadRequest());
    }
}
