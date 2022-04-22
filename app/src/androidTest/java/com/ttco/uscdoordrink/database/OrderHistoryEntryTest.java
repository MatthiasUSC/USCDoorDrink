package com.ttco.uscdoordrink.database;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OrderHistoryEntryTest {
    OrderHistoryEntry ohet;

    @Test
    public void toHashMapTest(){
        ohet = new OrderHistoryEntry("1", "2", "3",
                "4", "5", "6",
                "7", false, "8");

        Map<String, Object> Ohet = ohet.toHashMap();


        assertEquals("2", Ohet.get("customer_name"));
        assertEquals("3", Ohet.get("drink_name"));
        assertEquals("4", Ohet.get("start_time"));
        assertEquals("5", Ohet.get("seller_name"));
        assertEquals("6", Ohet.get("restaurant_name"));
        assertEquals("7", Ohet.get("end_time"));
        assertEquals(false, Ohet.get("is_caffeinated"));
        assertEquals("8", Ohet.get("order_location"));




    }



}