package com.ttco.uscdoordrink.database;

import java.util.Map;

public class CurrentOrderEntry {
    public String doc_id;
    public String customer_name;
    public String drink;
    public String startTime;
    public String seller_name;
    public String restaurant_name;
    public String order_location;


    public CurrentOrderEntry(String doc_id, String customer_name, String drink, String startTime, String seller_name, String restaurant_name, String order_location) {
        this.doc_id = doc_id;
        this.customer_name = customer_name;
        this.drink = drink;
        this.startTime = startTime;
        this.seller_name = seller_name;
        this.restaurant_name = restaurant_name;
        this.order_location = order_location;
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

    public Map<String, Object> toHashMap(){
        return null;
    }
}
