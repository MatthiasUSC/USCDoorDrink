package com.ttco.uscdoordrink.database;

import com.ttco.uscdoordrink.SellerOrder;

import java.util.Map;

public class OrderHistoryEntry {

    public static final String FIELD_DRINK_NAME = "drink_name";
    public static final String FIELD_CUSTOMER_NAME = "customer_name";
    public static final String FIELD_START_TIME = "start_time";
    public static final String FIELD_SELLER_NAME = "seller_name";
    public static final String FIELD_RESTAURANT_NAME = "restaurant_name";
    public static final String FIELD_END_TIME = "end_time";

    public String doc_id;
    public String customer_name;
    public String drink;
    public String startTime;
    public String seller_name;
    public String restaurant_name;
    public String endTime;
    public Boolean isCaffeinated;


    public OrderHistoryEntry(String doc_id, String customer_name, String drink,
                             String startTime, String seller_name, String restaurant_name,
                             String endTime, Boolean isCaffeinated) {

        this.doc_id = doc_id;
        this.customer_name = customer_name;
        this.drink = drink;
        this.startTime = startTime;
        this.seller_name = seller_name;
        this.restaurant_name = restaurant_name;
        this.endTime = endTime;
        this.isCaffeinated = isCaffeinated;
    }

    public OrderHistoryEntry(String doc_id, Map<String, Object> map){
        this.doc_id = doc_id;
        this.customer_name = customer_name;
        this.drink = drink;
        this.startTime = startTime;
        this.seller_name = seller_name;
        this.restaurant_name = restaurant_name;
        this.endTime = endTime;
        this.isCaffeinated = isCaffeinated;
    }

    public Map<String, Object> toHashMap(){
        return null;
    }
}
