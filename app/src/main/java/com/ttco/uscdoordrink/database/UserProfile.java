package com.ttco.uscdoordrink.database;

public class UserProfile {
    public String username;
    public String password;
    public boolean isSeller;

    public UserProfile(String username, String password, boolean isSeller) {
        this.username = username;
        this.password = password;
        this.isSeller = isSeller;
    }

}
