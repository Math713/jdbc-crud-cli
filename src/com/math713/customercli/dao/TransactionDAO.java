package com.math713.customercli.dao;

import com.math713.customercli.model.Transaction;
import com.math713.customercli.model.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private final String url = System.getenv("DB_URL");
    private final String user = System.getenv("DB_USER");
    private final String password = System.getenv("DB_PASSWORD");

    private void validateEnv() {
        if (url == null || url.isBlank() ||
                user == null || user.isBlank() ||
                password == null || password.isBlank()) {
            throw new IllegalStateException("Missing DB env vars. Set DB_URL, DB_USER, DB_PASSWORD.");
        }
    }

    private void validateCustomerId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID must be > 0. Provided id= " + id);
    }

    private void validateTransactionForInsert(Transaction transaction) {
        if (transaction == null) throw new IllegalArgumentException("Transaction cannot be null");
        validateCustomerId(transaction.getCustomerId());
        if (transaction.getType() == null) throw new IllegalArgumentException("Transaction type cannot be null");
        if (transaction.getAmount() == null) throw new IllegalArgumentException("Amount cannot be null");
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }
    }

    public TransactionDAO() {
        validateEnv();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public boolean insert(Transaction transaction) {
        validateTransactionForInsert(transaction);
        final String sql = "INSERT INTO transactions (customer_id, type, amount) VALUES (?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, transaction.getCustomerId());
            ps.setString(2, transaction.getType().name());
            ps.setBigDecimal(3, transaction.getAmount());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        transaction.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting new transaction for customerID= " + transaction.getCustomerId(), e);
        }
    }

    public List<Transaction> findByCustomerId(int customer_id) {
        validateCustomerId(customer_id);

        String sql = """
                SELECT id, customer_id, type, amount, created_at
                FROM transactions
                WHERE customer_id = ?
                ORDER BY created_at DESC, id DESC
                """;

        List<Transaction> txList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customer_id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            TransactionType.valueOf(rs.getString("type")),
                            rs.getBigDecimal("amount"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    txList.add(transaction);
                }
                return txList;

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer with customerID= " + customer_id, e);
        }
    }

    public BigDecimal getBalance(int customer_id) {
        validateCustomerId(customer_id);

        String sql = """
                SELECT COALESCE(SUM(
                    CASE
                        WHEN type = 'DEPOSIT' THEN amount
                        WHEN type = 'WITHDRAW' THEN -amount
                        ELSE 0
                    END
                ), 0) AS balance
                FROM transactions
                WHERE customer_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customer_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal balance = rs.getBigDecimal("balance");
                    return balance != null ? balance : BigDecimal.ZERO; // In some cases, even with COALESCE, can return null.
                }
                return BigDecimal.ZERO;

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting balance for customerID= " + customer_id, e);
        }
    }
}