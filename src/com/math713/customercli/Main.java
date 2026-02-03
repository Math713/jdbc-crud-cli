package com.math713.customercli;

import com.math713.customercli.cli.CustomerMenu;
import com.math713.customercli.cli.MainMenu;
import com.math713.customercli.cli.TransactionMenu;
import com.math713.customercli.dao.CustomerDAO;
import com.math713.customercli.dao.TransactionDAO;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        CustomerDAO customerDAO = new CustomerDAO();
        CustomerMenu customerMenu = new CustomerMenu(customerDAO, sc);

        TransactionDAO transactionDAO = new TransactionDAO();
        TransactionMenu transactionMenu = new TransactionMenu(transactionDAO, sc);
        MainMenu menu = new MainMenu(customerMenu, transactionMenu, sc);

        menu.start();
    }
}