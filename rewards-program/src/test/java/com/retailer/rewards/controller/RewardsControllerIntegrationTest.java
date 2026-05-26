package com.retailer.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    @DisplayName("GET /api/v1/rewards/{customerId} returns the expected totals")
    void getRewardsForCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalPoints").value(340))
                .andExpect(jsonPath("$.monthlyPoints.JANUARY").value(90))
                .andExpect(jsonPath("$.monthlyPoints.FEBRUARY").value(250));
    }

    @Test
    @DisplayName("GET for unknown customer returns 404")
    void unknownCustomerReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

}
