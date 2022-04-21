package com.ttco.uscdoordrink.database;

import java.util.HashMap;

public class UserProfile {
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_IS_SELLER = "isSeller";


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

        hashMap.put(FIELD_USERNAME, this.username);
        hashMap.put(FIELD_PASSWORD, this.password);
        hashMap.put(FIELD_IS_SELLER, this.isSeller);

        return hashMap;
    }
}
