package com.ttco.uscdoordrink.database;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryEntry {
    public static final String FIELD_DRINK_NAME = "drink_name";
    public static final String FIELD_CUSTOMER_NAME = "customer_name";
    public static final String FIELD_START_TIME = "start_time";
    public static final String FIELD_SELLER_NAME = "seller_name";
    public static final String FIELD_RESTAURANT_NAME = "restaurant_name";
    public static final String FIELD_END_TIME = "end_time";
    public static final String FIELD_IS_CAFFEINATED = "is_caffeinated";
    public static final String FIELD_ORDER_LOCATION = "order_location";

    public String doc_id;
    public String customer_name;
    public String drink;
    public String startTime;
    public String seller_name;
    public String restaurant_name;
    public String endTime;
    public Boolean isCaffeinated;
    public String order_location;

    public OrderHistoryEntry(String doc_id, String customer_name, String drink,
                             String startTime, String seller_name, String restaurant_name,
                             String endTime, Boolean isCaffeinated, String order_location) {

        this.doc_id = doc_id;
        this.customer_name = customer_name;
        this.drink = drink;
        this.startTime = startTime;
        this.seller_name = seller_name;
        this.restaurant_name = restaurant_name;
        this.endTime = endTime;
        this.isCaffeinated = isCaffeinated;
        this.order_location = order_location;
    }

    public OrderHistoryEntry(String doc_id, Map<String, Object> map){
        this.doc_id = doc_id;
        this.customer_name = (String)map.get(FIELD_CUSTOMER_NAME);
        this.drink = (String)map.get(FIELD_DRINK_NAME);
        this.startTime = (String)map.get(FIELD_START_TIME);
        this.seller_name = (String)map.get(FIELD_SELLER_NAME);
        this.restaurant_name = (String)map.get(FIELD_RESTAURANT_NAME);
        this.endTime = (String)map.get(FIELD_END_TIME);
        this.isCaffeinated = (Boolean) map.get(FIELD_IS_CAFFEINATED);
        this.order_location = (String) map.get(FIELD_ORDER_LOCATION);
    }

    public Map<String, Object> toHashMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FIELD_CUSTOMER_NAME, customer_name);
        map.put(FIELD_DRINK_NAME, drink);
        map.put(FIELD_IS_CAFFEINATED, isCaffeinated);
        map.put(FIELD_START_TIME, startTime);
        map.put(FIELD_SELLER_NAME, seller_name);
        map.put(FIELD_RESTAURANT_NAME, restaurant_name);
        map.put(FIELD_END_TIME, endTime);
        map.put(FIELD_ORDER_LOCATION, order_location);
        return map;
    }
}
