package com.math713.customercli.cli;

import com.math713.customercli.dao.CustomerDAO;
import com.math713.customercli.model.Customer;

import java.util.Scanner;

public class CustomerMenu {
    private final Scanner sc;
    private final CustomerDAO customerDAO;

    public CustomerMenu(CustomerDAO customerDAO, Scanner sc) {
        this.customerDAO = customerDAO;
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== CUSTOMER MANAGEMENT ===");
            System.out.println("[1] List all customers");
            System.out.println("[2] Find customer by ID");
            System.out.println("[3] Create new customer");
            System.out.println("[4] Update existing customer");
            System.out.println("[5] Delete customer");
            System.out.println("[0] Back");
            System.out.println("Choose: ");
            System.out.print("> ");

            int option = readInt();
            switch (option) {
                case 1 -> listAllCustomers();
                case 2 -> findCustomerById();
                case 3 -> createNewCustomer();
                case 4 -> updateNewCustomer();
                case 5 -> deleteCustomerById();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void listAllCustomers(){
        var customers = customerDAO.findAll();
        System.out.println("Customers (" + customers.size() + "):");

        for (Customer c : customers) {
            System.out.printf("#%-4d | %-20s | %-30s%n",
                    c.getId(), c.getName(), c.getEmail());
        }
        System.out.println();
    }

    private void findCustomerById(){
        int customer_id = askID();
        customerDAO.findById(customer_id).ifPresentOrElse(
                found -> {
                    System.out.println("ID: " + found.getId());
                    System.out.println("Name : " + found.getName());
                    System.out.println("Email: " + found.getEmail());
                    System.out.println();
                },
                () -> System.out.println("Customer not found or inactive\n")
        );
    }

    private void createNewCustomer() {
        String name = askString("Name");
        String email = askString("Email");

        try {
            boolean inserted = customerDAO.insert(new Customer(name, email));
            System.out.println(inserted ? "Customer created successfully!\n" : "Could not create customer. Try again.\n");

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Email already exists")) {
                System.out.println("Error: The email '" + email + "' is already in use.\n");
            } else {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private void updateNewCustomer() {
        int id = askID();
        customerDAO.findById(id).ifPresentOrElse(
                existing -> {
                    System.out.println("Updating: " + existing.getName());
                    String newName = askString("New Name (current: " + existing.getName() + ")");
                    String newEmail = askString("New Email (current: " + existing.getEmail() + ")");

                    try {
                        boolean updated = customerDAO.update(id, new Customer(newName, newEmail));
                        System.out.println(updated ? "Customer updated.\n" : "Error updating.\n");

                    } catch (RuntimeException e) {
                        if (e.getMessage().contains("Email already exists")){
                            System.out.println("Error: The email '" + existing.getEmail() + "' is already in use.\n");
                        } else {
                            System.out.println("An unexpected error occurred: " + e.getMessage());
                        }
                    }
                },
                () -> System.out.println("Customer not found.\n")
        );
    }

    public void deleteCustomerById(){
        int id = askID();
        boolean deleted = customerDAO.delete(id);
        System.out.println(deleted ? "Customer deactivated.\n" : "Customer not found.\n");
    }

    private String askString(String label) {
        while (true) {
            System.out.print(label + ": ");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("This field cannot be empty.");
                continue;
            }

            if (label.equalsIgnoreCase("Email") && (!input.contains("@") || !input.contains("."))){
                System.out.println("Invalid email format (must contain '.' and '@')");
                continue;
            }
            return input;
        }
    }

    private int askID(){
        System.out.print("ID: ");
        return readInt();
    }

    private int readInt(){
        while (true){
            String raw = sc.nextLine().trim();
            try {
                return Integer.parseInt(raw);
            }catch (NumberFormatException e){
                System.out.println("Enter a value number");
            }
        }
    }
}