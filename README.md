# 🏦 Bank CLI (JDBC CRUD & Transactions)

A terminal-based Banking Management System built with **Java 17**, using **JDBC** for **MySQL** persistence. This project demonstrates mastery of backend fundamentals, separation of concerns (DAO pattern), robust error handling, and data integrity.

## 🛠️ Technologies & Concepts
* **Language:** Java 17 (Features: Switch Expressions, Optionals, Text Blocks).
* **Database:** MySQL 8.0 with integrity constraints (`UNIQUE`, `CHECK`, `FOREIGN KEY`).
* **Persistence:** Pure JDBC for full control over transactions and queries.
* **Architecture:** DAO (Data Access Object) pattern for data layer isolation.

---

## 🚀 Key Features

### 👤 Customer Management (`CustomerMenu`)
* **Full CRUD Flow:** Listing, individual search, creation, and updates.
* **Soft Delete:** Professional implementation where customers are deactivated (`active = false`) instead of deleted, preserving financial history.
* **Data Security:** Email validation (format and uniqueness) with `SQLIntegrityConstraintViolationException` handling.

### 💸 Financial Operations (`TransactionMenu`)
* **Deposits & Withdrawals:** Centralized processing with strict balance validation.
* **Database Logic (SQL):** Dynamic balance calculation using `SUM` with `CASE WHEN` directly in the query for performance optimization.
* **Detailed Statements:** Transaction history filtered by customer and sorted chronologically.

---

## 💎 Technical Highlights

* **Null-Safe:** Systematic use of `Optional<Customer>` for lookups, eliminating `NullPointerException` risks.
* **CLI UX:** ANSI escape codes for screen clearing (`clearScreen`), providing a fluid and professional navigation.
* **Layered Validation:** Business rules verified both in Java code and via Database Constraints.
* **Security:** Environment variables for database credentials, following Twelve-Factor App best practices.

---

## 🗄️ Data Model



```sql
-- Core table structures and constraints
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    type ENUM('DEPOSIT', 'WITHDRAW') NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
```

---

## ⚙️ How to Run

### 1. Database Setup
Execute the SQL script above in your MySQL instance to create the `bank_cli` database and tables.

### 2. Environment Variables
Set the following environment variables on your system or IDE:

- **DB_URL**: Connection string (e.g., `jdbc:mysql://localhost:3306/bank_cli`)
- **DB_USER**: Your MySQL username
- **DB_PASSWORD**: Your MySQL password

### 3. Execution
- Compile the project using your preferred IDE (IntelliJ / VS Code) or via terminal
- Run the `Main.java` class
- Use the numeric keyboard to navigate through the menus
