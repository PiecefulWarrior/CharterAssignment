# Retailer Rewards Program

A Spring Boot application that calculates loyalty reward points earned by customers based on their purchase history.

## Reward Rules

- **2 points** for every dollar spent **over $100** in a transaction.
- **1 point** for every dollar spent **between $50 and $100** in a transaction.
- **0 points** for any amount at or below $50.

Example: a $120 purchase earns `2 × 20 + 1 × 50 = 90` points.

## Tech Stack

- Java 17
- Spring Boot 3.2.x (Web, Validation)
- JUnit 5, AssertJ, Spring MockMvc
- Maven

## Project Structure

```
rewards-program/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/retailer/rewards/
    │   │   ├── RewardsApplication.java          # Spring Boot entry point
    │   │   ├── controller/
    │   │   │   └── RewardsController.java       # REST endpoints
    │   │   ├── service/
    │   │   │   ├── RewardCalculator.java        # Per-transaction rule
    │   │   │   └── RewardsService.java          # Aggregation logic
    │   │   ├── repository/
    │   │   │   └── TransactionRepository.java   # In-memory data store + seed data
    │   │   ├── model/
    │   │   │   ├── Transaction.java
    │   │   │   └── CustomerRewards.java
    │   │   └── exception/
    │   │       ├── CustomerNotFoundException.java
    │   │       ├── InvalidTransactionException.java
    │   │       └── GlobalExceptionHandler.java  # @RestControllerAdvice
    │   └── resources/
    │       └── application.properties
    └── test/java/com/retailer/rewards/
        ├── service/
        │   ├── RewardCalculatorTest.java        # Unit tests (parameterized)
        │   └── RewardsServiceTest.java          # Unit tests for aggregation
        └── controller/
            └── RewardsControllerIntegrationTest.java   # MockMvc integration tests
```

## Build & Run

```bash
# Build
mvn clean package

# Run all tests
mvn test

# Start the application
mvn spring-boot:run
```

The service starts on port `8080`.

## REST Endpoints

### GET `/api/v1/rewards`
Returns a list of reward summaries for every customer with transactions in the period.

Query parameters (optional):
- `start` — ISO date (e.g. `2025-01-01`)
- `end` — ISO date

Example:
```
GET /api/v1/rewards?start=2025-01-01&end=2025-03-31
```

### GET `/api/v1/rewards/{customerId}`
Returns the reward summary for one customer.

Example:
```
GET /api/v1/rewards/1?start=2025-01-01&end=2025-03-31
```

Sample response:
```json
{
  "customerId": 1,
  "monthlyPoints": { "JANUARY": 115, "FEBRUARY": 250, "MARCH": 49 },
  "totalPoints": 414
}
```

### POST `/api/v1/rewards/transactions`
Adds a new transaction.

```json
{
  "customerId": 1,
  "amount": 175.00,
  "transactionDate": "2025-02-15"
}
```

## Error Responses

All errors return a structured JSON body:
```json
{
  "timestamp": "2025-03-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No transactions found for customer 99 in the specified period"
}
```

| Scenario                              | HTTP Status |
|---------------------------------------|-------------|
| Customer has no transactions in range | 404         |
| Inverted or null date range           | 400         |
| Invalid transaction body              | 400         |
| Unexpected server error               | 500         |

## Tests

- **Unit tests:** `RewardCalculatorTest` covers tier boundaries (\$0, \$50, \$51, \$100, \$101, \$120, \$500) via `@ParameterizedTest`, plus null and negative-amount paths.
- **Service tests:** `RewardsServiceTest` covers multi-customer, multi-month aggregation, unknown-customer 404, and invalid date ranges.
- **Integration tests:** `RewardsControllerIntegrationTest` boots the full Spring context with `@SpringBootTest` and `MockMvc` to verify HTTP status codes, JSON shape, validation, and exception mapping.

Run with:
```bash
mvn test
```
