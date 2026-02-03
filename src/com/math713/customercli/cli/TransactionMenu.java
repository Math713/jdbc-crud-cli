package com.math713.customercli.cli;

import com.math713.customercli.dao.TransactionDAO;
import com.math713.customercli.model.Transaction;
import com.math713.customercli.model.TransactionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TransactionMenu {
    private final Scanner sc;
    private final TransactionDAO transactionDAO;

    public TransactionMenu(TransactionDAO transactionDAO, Scanner sc) {
        this.transactionDAO = transactionDAO;
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== TRANSACTIONS ===");
            System.out.println("[1] Deposit");
            System.out.println("[2] Withdraw");
            System.out.println("[3] List transactions by customer");
            System.out.println("[4] Show balance");
            System.out.println("[0] Back");
            System.out.println("Choose: ");
            System.out.print("> ");

            int option = readInt();
            switch (option){
                case 1 -> processTransaction(TransactionType.DEPOSIT);
                case 2 -> processTransaction(TransactionType.WITHDRAW);
                case 3 -> listByCustomerId();
                case 4 -> showBalance();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void processTransaction(TransactionType type){
        int customer_id = askCustomerID();
        BigDecimal amount = askAmount();

        if (type == TransactionType.WITHDRAW) {
            BigDecimal balance = transactionDAO.getBalance(customer_id);
            if (amount.compareTo(balance) > 0) {
                System.out.println("Insufficient funds! Current balance: " + balance);
                return;
            }
        }

        Transaction transaction = new Transaction(customer_id, type, amount);
        boolean ok = transactionDAO.insert(transaction);

        String action = type == TransactionType.DEPOSIT ? "Deposit" : "Withdraw";
        System.out.println(ok ? action + " registered (ID = " + transaction.getId() + ")"
                : "Error: " + action + " not registered.");
    }

    private void listByCustomerId(){
        int customer_id = askCustomerID();
        List<Transaction> transactionList = transactionDAO.findByCustomerId(customer_id);

        if (transactionList.isEmpty()){
            System.out.println("No transactions found for customerId= " + customer_id);
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("\n --- Transactions (customerId= " + customer_id + ") ---");
        for (Transaction t: transactionList){
            System.out.printf("#%-4d | %-19s | %-8s | %s%s%n",
                    t.getId(),
                    t.getCreatedAt() != null ? t.getCreatedAt().format(fmt) : "null",
                    t.getType(),
                    t.getType() == TransactionType.DEPOSIT ? "+" : "-",
                    t.getAmount());
        }
    }

    private void showBalance(){
        int customer_id = askCustomerID();
        BigDecimal balance = transactionDAO.getBalance(customer_id);
        System.out.println("Balance for customerId= " + customer_id + " = " + balance);
    }

    private BigDecimal askAmount(){
        while (true) {
            System.out.println("Amount (e.g 10.50): ");
            String raw = sc.nextLine().trim().replace(",", ".");
            try{
                BigDecimal value = new BigDecimal(raw);
                if (value.compareTo(BigDecimal.ZERO) <= 0){
                    System.out.println("amount must be > 0");
                    continue;
                }
                return value.setScale(2, RoundingMode.HALF_UP);

            } catch (Exception e) {
                System.out.println("Invalid amount.");
            }
        }
    }

    private int askCustomerID(){
        System.out.println("Customer ID: ");
        return readInt();
    }

    private int readInt(){
        while (true) {
            String raw = sc.nextLine().trim();
            try{
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}