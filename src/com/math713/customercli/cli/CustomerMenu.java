package com.math713.customercli.cli;

import com.math713.customercli.dao.CustomerDAO;
import com.math713.customercli.model.Customer;

import java.util.Scanner;

public class CustomerMenu {
    private CustomerDAO customerDAO;

    public CustomerMenu(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

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

                int option;
                try {
                    option = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option. Please enter a number.\n");
                    continue;
                }

                switch (option) {
                    case 0 -> {
                        System.out.println("Leaving...");
                        return;
                    }

                    case 1 -> {
                        var customers = customerDAO.findAll();
                        System.out.println("Customers (" + customers.size() + "):");

                        for (Customer c : customers) {
                            System.out.println("ID: " + c.getId() +
                                    " | Name: " + c.getName() +
                                    " | Email: " + c.getEmail());
                        }
                        System.out.println();
                    }

                    case 2 -> {
                        System.out.print("Id: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        Customer found = customerDAO.findById(id);
                        if (found == null) {
                            System.out.println("Customer not found.\n");
                        } else {
                            System.out.println("ID: " + found.getId());
                            System.out.println("Name: " + found.getName());
                            System.out.println("Email: " + found.getEmail() + "\n");
                        }
                    }

                    case 3 -> {
                        System.out.print("Name: ");
                        String name = sc.nextLine().trim();
                        System.out.print("Email: ");
                        String email = sc.nextLine().trim();

                        boolean inserted = customerDAO.insert(new Customer(name, email));
                        System.out.println(inserted ? "Customer created.\n" : "Could not create customer.\n");
                    }

                    case 4 -> {
                        System.out.print("Id: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Name: ");
                        String name = sc.nextLine().trim();
                        System.out.print("Email: ");
                        String email = sc.nextLine().trim();

                        boolean updated = customerDAO.update(id, new Customer(name, email));
                        System.out.println(updated ? "Customer updated.\n" : "Customer not found.\n");
                    }

                    case 5 -> {
                        System.out.print("Id: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        boolean deleted = customerDAO.delete(id);
                        System.out.println(deleted ? "Customer deleted.\n" : "Customer not found.\n");
                    }

                    default -> System.out.println("Invalid option.\n");
            }
        }
    }
}