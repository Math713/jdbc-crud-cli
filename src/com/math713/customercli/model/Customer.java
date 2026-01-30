package com.math713.customercli.model;

public class Customer {
    private String name;
    private String email;
    private Integer id;

    public Customer(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Customer(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void setId(Integer id) {this.id = id;}

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
