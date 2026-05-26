# Retailer Rewards Program

A Spring Boot application that calculates loyalty reward points earned by customers based on their purchase history.

## Reward Rules

- 2 points for every dollar spent over $100 in a transaction
- 1 point for every dollar spent between $50 and $100 in a transaction
- 0 points for any amount at or below $50

### Example

A `$120` purchase earns:

```text
2 × 20 + 1 × 50 = 90 points
```

---

# Tech Stack

- Java 17
- Spring Boot 3.2.x
- Spring Web
- Spring Validation
- Spring Data JPA
- H2 In-Memory Database
- JUnit 5
- AssertJ
- Spring MockMvc
- Maven

---

# Project Features

- RESTful API implementation
- Reward point calculation engine
- Monthly and total reward aggregation
- H2 in-memory database integration
- JPA-based persistence layer
- SQL initialization using `schema.sql` and `data.sql`
- Global exception handling
- Input validation
- Unit and integration test coverage

---

# Database Configuration

The application uses an H2 in-memory database.

## H2 Console

```text
http://localhost:8080/h2-console
```

# Sample Data

File:

```text
src/main/resources/data.sql
```

```sql
INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 120.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 75.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 200.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 45.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 99.00, CURRENT_DATE);

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (2, 150.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (2, 60.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (2, 310.00, CURRENT_DATE);

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (3, 100.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (3, 50.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (3, 500.00, CURRENT_DATE);
```

---

## Start Application

```bash
mvn spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

---

# REST API

## Get Rewards for a Customer

### Request

```http
GET /api/v1/rewards/{customerId}
```

### Example

```http
GET /api/v1/rewards/1
```

### Sample Response

```json
{
  "customerId": 1,
  "monthlyPoints": {
    "JANUARY": 115,
    "FEBRUARY": 250,
    "MARCH": 49
  },
  "totalPoints": 414
}
```

---

# Error Responses

All errors return a structured JSON response.

## Example

```json
{
  "timestamp": "2025-03-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No transactions found for customer 99 in the specified period"
}
```

---

# Test Coverage

The project includes:

- Service layer unit tests
- Controller integration tests
- Validation testing
- Exception handling testing
- H2 database integration testing

---

# Future Improvements

Possible production-grade enhancements:

- PostgreSQL/MySQL integration
- Flyway database migrations
- Docker support
- Swagger/OpenAPI documentation
- Authentication & authorization
- Redis caching
- Kafka event publishing
- Pagination and filtering
- Distributed tracing and monitoring
- Testcontainers integration

---

# Author

Bhushan Shimpi
