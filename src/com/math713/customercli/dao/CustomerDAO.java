package com.math713.customercli.dao;

import com.math713.customercli.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerDAO {
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

    public CustomerDAO() {
        validateEnv();
    }

    private void validateId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id must be > 0");
    }

    private void validateCustomer(Customer customer) {
        Objects.requireNonNull(customer, "Customer cannot be null");
        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email cannot be blank");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public List<Customer> findAll() {
        String sql = "SELECT id, name, email FROM customers";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
            return customers;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all customers", e);
        }
    }

    public Customer findById(int id) {
        validateId(id);

        String sql = "SELECT id, name, email FROM customers WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer with id = " + id, e);
        }
    }

    public boolean insert(Customer customer) {
        validateCustomer(customer);

        String sql = "INSERT INTO customers (name, email) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());

            return ps.executeUpdate() == 1;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Email already exists: " + customer.getEmail(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting customer", e);
        }
    }

    public boolean update(int id, Customer customer) {
        validateId(id);
        validateCustomer(customer);

        String sql = "UPDATE customers SET name = ?, email = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setInt(3, id);

            return ps.executeUpdate() == 1;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Email already exists: " + customer.getEmail(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer with id = " + id, e);
        }
    }

    public boolean delete(int id) {
        validateId(id);

        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer with id = " + id, e);
        }
    }
}
