package com.ttco.uscdoordrink;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class OrderDetailsTest {
    //OrderHistoryEntry ohe;
    OrderDetailsActivity ohe;
    @Before
//    String doc_id, String customer_name, String drink,
//    String startTime, String seller_name, String restaurant_name,
//    String endTime, Boolean isCaffeinated, String order_location
//
    public void init(){
        //orderDetails = new OrderDetails();
        //ohe = new OrderHistoryEntry(1, "bob", "cocaine");
        //ohe = new OrderDetails();
    }
    @Test
    public void getPeriod() {
        System.out.println("hi");
        OrderDetailsActivity.cycle = 1;
        String testmonth = OrderDetailsActivity.GetPeriod();

//        orderDetails.cycle = 1;
//        String test_month = orderDetails.GetPeriod();
        assertEquals(testmonth, "Year");
    }

    @Test
    public void OrderToString(){
        String res = OrderDetailsActivity.OrderToString("coke", "inandout", "never", "nowhere", "3");
            assertEquals(res, "Drink: " + "coke" + "\n"
                    + "Restaurant: " + "inandout" + "\n" +
                    "Time ordered: " + "never" + "\n" + "Place enjoyed beverage: "
                    + "nowhere"
                    + "\n" + "Time order received: " + "3");
    }
    @Test
    public void MakeRecTest(){
        String test_rec = OrderDetailsActivity.MakeRec("Spongebob");
        assertEquals(test_rec, "You visited Spongebob the most this month we recommend you visit this place again.");
    }
    @Test
    public void getDateTest(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("23/03/0323 03:23:01", dtf);

        int order_month = dateTime.getMonth().getValue();
        int order_year = dateTime.getYear();
        int order_day = dateTime.getDayOfWeek().getValue();
        int order_hour = dateTime.getHour();
        int order_minute = dateTime.getMinute();
        int order_second = dateTime.getSecond();

        HashMap dateAttr = OrderDetailsActivity.getDate(dateTime);
        assertEquals(order_month, dateAttr.get("month"));
        assertEquals(order_year, dateAttr.get("year"));
        assertEquals(order_day, dateAttr.get("day"));
        assertEquals(order_hour, dateAttr.get("hour"));
        assertEquals(order_minute, dateAttr.get("minute"));
        assertEquals(order_second, dateAttr.get("second"));


    }
}