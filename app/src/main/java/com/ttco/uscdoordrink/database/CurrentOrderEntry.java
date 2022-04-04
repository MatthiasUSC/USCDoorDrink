package com.ttco.uscdoordrink.database;

import java.util.HashMap;
import java.util.Map;

public class CurrentOrderEntry {
    public String doc_id;
    public String customer_name;
    public String drink;
    public String startTime;
    public String seller_name;
    public String restaurant_name;
    public String order_location;
    public boolean isCaffeinated;

    public static final String FIELD_DRINK_NAME = "drink_name";
    public static final String FIELD_CUSTOMER_NAME = "customer_name";
    public static final String FIELD_START_TIME = "start_time";
    public static final String FIELD_SELLER_NAME = "seller_name";
    public static final String FIELD_RESTAURANT_NAME = "restaurant_name";
    public static final String FIELD_IS_CAFFEINATED = "is_caffeinated";
    public static final String FIELD_ORDER_LOCATION = "order_location";


    public CurrentOrderEntry(String doc_id, String customer_name, String drink, String startTime, String seller_name, String restaurant_name, String order_location, boolean isCaffeinated) {
        this.doc_id = doc_id;
        this.customer_name = customer_name;
        this.drink = drink;
        this.startTime = startTime;
        this.seller_name = seller_name;
        this.restaurant_name = restaurant_name;
        this.order_location = order_location;
        this.isCaffeinated = isCaffeinated;
    }


    public CurrentOrderEntry(String doc_id, Map<String, Object> map){
        this.doc_id = doc_id;
        this.customer_name = (String)map.get("customer_username");
        this.drink = (String)map.get("drink");
        this.startTime = (String)map.get("start_time");
        this.seller_name = (String)map.get("seller_username");
        this.restaurant_name = (String)map.get("restaurant_name");
        this.order_location = (String)map.get("order_location");
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FIELD_CUSTOMER_NAME, customer_name);
        map.put(FIELD_DRINK_NAME, drink);
        map.put(FIELD_IS_CAFFEINATED, isCaffeinated);
        map.put(FIELD_START_TIME, startTime);
        map.put(FIELD_SELLER_NAME, seller_name);
        map.put(FIELD_RESTAURANT_NAME, restaurant_name);
        map.put(FIELD_ORDER_LOCATION, order_location);
        return map;
    }
}
