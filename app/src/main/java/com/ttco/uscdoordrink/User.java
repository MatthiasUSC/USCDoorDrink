package com.ttco.uscdoordrink;

public class User {
    String name;
    String password;
    Boolean type; //True is seller, False is customer
    private static String Userid;
    User(String name, String password, Boolean type){
        this.name = name;
        this.password = password;
        this.type = type;
    }
    public static String getUserid(){return Userid;}
    public static void setData(String id){Userid = id;}
}

