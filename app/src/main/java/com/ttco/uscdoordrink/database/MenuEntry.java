package com.ttco.uscdoordrink.database;

import java.util.HashMap;
import java.util.Map;

public class MenuEntry {
    public static final String FIELD_DRINK_NAME = "drink_name";
    public static final String FIELD_IS_CAFFEINATED = "is_caffeinated";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_DISCOUNT = "discount";
    public static final String FIELD_OWNER_USERNAME = "owner_username";

    public String id;
    public String drinkName;
    public Boolean isCaffeinated;
    public String price;
    public String discount;
    public String ownerUsername;

    public MenuEntry(String id, String drinkName, Boolean isCaffeinated, String price, String discount, String ownerUsername) {
        this.id = id;
        this.drinkName = drinkName;
        this.isCaffeinated = isCaffeinated;
        this.price = price;
        this.discount = discount;
        this.ownerUsername = ownerUsername;
    }

    public MenuEntry(String id, Map<String, Object> map){
        this.id = id;
        this.drinkName = (String)map.get(FIELD_DRINK_NAME);
        this.isCaffeinated = (Boolean)map.get(FIELD_IS_CAFFEINATED);
        this.price = (String)map.get(FIELD_PRICE);
        this.discount = (String)map.get(FIELD_DISCOUNT);
        this.ownerUsername = (String)map.get(FIELD_OWNER_USERNAME);
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FIELD_DRINK_NAME, drinkName);
        map.put(FIELD_IS_CAFFEINATED, isCaffeinated);
        map.put(FIELD_PRICE, price);
        map.put(FIELD_DISCOUNT, discount);
        map.put(FIELD_OWNER_USERNAME, ownerUsername);
        return map;
    }
}
