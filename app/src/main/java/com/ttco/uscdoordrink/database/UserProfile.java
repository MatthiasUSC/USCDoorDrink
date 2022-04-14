package com.ttco.uscdoordrink.database;

import java.util.HashMap;

public class UserProfile {
    public String username;
    public String password;
    public boolean isSeller;

    public UserProfile(String username, String password, boolean isSeller) {
        this.username = username;
        this.password = password;
        this.isSeller = isSeller;
    }

    public HashMap<String, Object> toHashMap(){

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("username", this.username);
        hashMap.put("password", this.password);
        hashMap.put("isSeller", this.isSeller);

        return hashMap;
    }
}
