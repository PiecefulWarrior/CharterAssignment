package com.retailer.rewards.controller;

import com.retailer.rewards.dto.CustomerRewards;

import com.retailer.rewards.service.RewardsService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for the Retailer Rewards Program.
 * Exposes operations to retrieve reward summaries for a specific customer
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    /**
     * Returns the reward data for a single customer.
     *
     * @param customerId the customer to query
     * @return reward summary for the customer
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerRewards> getRewards(
            @PathVariable @NotNull Long customerId) {

        return ResponseEntity.ok(rewardsService.calculateRewardsForCustomer(customerId));
    }


}
