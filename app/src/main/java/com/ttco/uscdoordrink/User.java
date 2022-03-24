package com.ttco.uscdoordrink;

public class  User {
    private String name;
    String password;
    Boolean type; //True is seller, False is customer

    User(String name, String password, Boolean type){
        this.name = name;
        this.password = password;
        this.type = type;
    }
}

