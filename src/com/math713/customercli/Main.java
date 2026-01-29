package com.math713.customercli;

import com.math713.customercli.cli.CustomerMenu;
import com.math713.customercli.dao.CustomerDAO;

public class Main {
    public static void main(String[] args){
        CustomerDAO customerDAO = new CustomerDAO();
        CustomerMenu customerMenu = new CustomerMenu(customerDAO);

        customerMenu.start();
    }
}