# Customer CLI (JDBC CRUD)

A terminal-based **Customer Management CLI** built with **Java 17**, **pure JDBC**, and **MySQL**, following the **DAO pattern** and clean separation of responsibilities.

This project is part of my backend fundamentals practice before moving to frameworks like Spring.

---

## Features

- List all customers
- Find customer by ID
- Create customer
- Update customer
- Delete customer
- Uses environment variables for DB configuration (no credentials in code)

---

## Project structure

- `dao/` → JDBC access layer (DAO)
- `model/` → domain entities
- `cli/CustomerMenu.java` → CLI menu and input handling
- `Main.java` → application entry point

---

## Configuration

Set the following environment variables:

- `DB_URL` (example: `jdbc:mysql://localhost:3306/bank_db`)
- `DB_USER`
- `DB_PASSWORD`

---

## Notes

This repository is for educational purposes and represents my continuous learning process in Java backend development.
