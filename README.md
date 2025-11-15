# Spring Boot Transaction Management System

A high-performance backend service built with Spring Boot, PostgreSQL, and Redis. It handles user onboarding, wallet creation, atomic money transfers, and fast balance retrieval via caching.

## Key Features

  * **Atomicity:** `@Transactional` ensures complete rollback on failure.
  * **Concurrency Control:** Pessimistic locking (`FOR UPDATE`) prevents race conditions.
  * **Caching:** Redis + `@Cacheable` for hot data (wallet balances).
  * **Cache Invalidation:** `@CacheEvict` removes stale entries after transactions.
  * **Idempotency:** Prevents duplicate requests using a unique `Idempotency-Key`.
  * **DTOs:** Secures API response structure.

-----

## 🛠 Tech Stack

  * Java 17+
  * Spring Boot
  * Spring Data JPA
  * PostgreSQL
  * Spring Data Redis
  * Redis
  * Maven

-----

## 🚀 How to Run

### 1\. Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/transaction-system.git
cd transaction-system
```

### 2\. Set Up Databases

Ensure:

  * PostgreSQL is running on port `5432`
  * Redis is running on port `6379`

Create the database:

```sql
CREATE DATABASE transactiondb;
```

### 3\. Set Environment Variable

#### macOS / Linux:

```bash
export DB_PASSWORD='your_postgres_password'
```

#### Windows CMD:

```cmd
set DB_PASSWORD=your_postgres_password
```

### 4\. Run Application

```bash
mvn spring-boot:run
```

The server will start at `http://localhost:8080`.

-----

## 🌎 API Endpoints

### Create User

`POST /users`

**Request:**

```json
{
  "name": "Test User",
  "email": "test@example.com"
}
```

**Response (`201 Created`):**

```json
{
  "id": 1,
  "name": "Test User",
  "email": "test@example.com",
  "createdAt": "2025-11-15T18:00:00.123456"
}
```

-----

### Get Wallet Balance (Cached)

`GET /wallets/{walletId}/balance`

**Response (`200 OK`):**

```json
{
  "balance": 0.00
}
```

-----

### Create Transaction (Idempotent)

`POST /transactions`

**Request:**

```json
{
  "walletId": 1,
  "amount": 100.00,
  "type": "CREDIT",
  "idempotencyKey": "a-unique-uuid-for-this-request"
}
```

**Response (`201 Created`):**

```json
{
  "id": 1,
  "walletId": 1,
  "type": "CREDIT",
  "amount": 100.00,
  "status": "COMPLETED",
  "idempotencyKey": "a-unique-uuid-for-this-request",
  "createdAt": "2025-11-15T18:05:00.123456"
}
```

-----

## ❌ Error Responses

  * **`409 Conflict`**: Returned when a request is sent with a reused `idempotencyKey`.
  * **`422 Unprocessable Entity`**: Returned when a `DEBIT` request is made for an amount greater than the available balance.
