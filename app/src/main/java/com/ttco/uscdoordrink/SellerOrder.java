package com.ttco.uscdoordrink;

import java.util.Map;

public class SellerOrder {
    public String doc_id;
    public String customer_name;
    public String drink;
    public String startTime;
    public String seller_name;
    public String restaurant_name;
    public String restaurant_location;
    public SellerOrder(String doc_id, String customer_name, String drink, String startTime, String seller_name, String restaurant_name) {
        this.doc_id = doc_id;
        this.customer_name = customer_name;
        this.drink = drink;
        this.startTime = startTime;
        this.seller_name = seller_name;
        this.restaurant_name = restaurant_name;
    }


    public static SellerOrder fromHashMap(){
        return null;
    }

    public Map<String, Object> toHashMap(){
        return null;
    }
}
