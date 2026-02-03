package com.math713.customercli;

import com.math713.customercli.dao.CustomerDAO;
import com.math713.customercli.dao.TransactionDAO;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        CustomerDAO customerDAO = new CustomerDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
    }
}