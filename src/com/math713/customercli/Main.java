package com.math713.customercli;

import com.math713.customercli.cli.CustomerMenu;
import com.math713.customercli.dao.CustomerDAO;

public class Main {
    public static void main(String[] args){
        CustomerDAO dao = new CustomerDAO();
        CustomerMenu menu = new CustomerMenu(dao);

        menu.start();
    }
}