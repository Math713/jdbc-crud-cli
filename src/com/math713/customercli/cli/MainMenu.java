package com.math713.customercli.cli;

import java.util.Scanner;

public class MainMenu {
    private final CustomerMenu customerMenu;
    private final TransactionMenu transactionMenu;
    private final Scanner sc;

    public MainMenu(CustomerMenu customerMenu, TransactionMenu transactionMenu, Scanner sc) {
        this.customerMenu = customerMenu;
        this.transactionMenu = transactionMenu;
        this.sc = sc;
    }

    public void start(){
        while (true){
            clearScreen();
            System.out.println("\n===== BANK SYSTEM CLI =====");
            System.out.println("[1] Customer Management");
            System.out.println("[2] Transaction Operations");
            System.out.println("[0] Exit");
            System.out.print("> ");

            try {
                String input = sc.nextLine().trim();
                int option = Integer.parseInt(input);

                switch (option){
                    case 1 -> customerMenu.start();
                    case 2 -> transactionMenu.start();
                    case 0 -> {
                        System.out.println("Closing system... Goodbye!");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please, enter a valid number.");
            }
        }
    }
    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
