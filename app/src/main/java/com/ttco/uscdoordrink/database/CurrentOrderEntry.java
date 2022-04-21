package com.ttco.uscdoordrink.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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


    public CurrentOrderEntry(String doc_id, String customer_name, String drink, String startTime,
                             String seller_name, String restaurant_name, String order_location,
                             boolean isCaffeinated) {
        this.doc_id = doc_id;
        this.customer_name = customer_name;
        this.drink = drink;
        this.startTime = startTime;
        this.seller_name = seller_name;
        this.restaurant_name = restaurant_name;
        this.order_location = order_location;
        this.isCaffeinated = isCaffeinated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentOrderEntry that = (CurrentOrderEntry) o;
        return isCaffeinated == that.isCaffeinated && Objects.equals(doc_id, that.doc_id) &&
                Objects.equals(customer_name, that.customer_name)
                && Objects.equals(drink, that.drink) && Objects.equals(startTime, that.startTime)
                && Objects.equals(seller_name, that.seller_name)
                && Objects.equals(restaurant_name, that.restaurant_name)
                && Objects.equals(order_location, that.order_location);
    }

    public CurrentOrderEntry(String doc_id, Map<String, Object> map){
        this.doc_id = doc_id;
        this.customer_name = (String)map.get(FIELD_CUSTOMER_NAME);
        this.drink = (String)map.get(FIELD_DRINK_NAME);
        this.startTime = (String)map.get(FIELD_START_TIME);
        this.seller_name = (String)map.get(FIELD_SELLER_NAME);
        this.restaurant_name = (String)map.get(FIELD_RESTAURANT_NAME);
        this.order_location = (String)map.get(FIELD_ORDER_LOCATION);
        this.isCaffeinated = (Boolean)map.get(FIELD_IS_CAFFEINATED);
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
