package com.ttco.uscdoordrink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DatabaseInterfaceUnitTest {
    @Test
    public void userCreationTest() {
        Map<String, Object> map = DatabaseInterface.createUsersEntry("test_user", "test_pass", true);
        assertEquals(map.get(DatabaseInterface.USERNAME_KEY), "test_user");
        assertEquals(map.get(DatabaseInterface.PASSWORD_KEY), "test_pass");
        assertEquals(map.get(DatabaseInterface.IS_SELLER_KEY), true);
    }

    @Test
    public void createOrderHistoryEntryFromCurrentOrderEntryTest(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = new Date();

        CurrentOrderEntry order = new CurrentOrderEntry("123", "pepe", "tea",
                formatter.format(startDate), "cesar", "olive",
                "123 street", false);

        Map<String, Object> orderHistory = DatabaseInterface.createOrderHistoryEntryFromCurrentOrderEntry(order);

        assertEquals("Customer Correct: ", orderHistory.get("customer_username"),
                order.customer_name);
        assertEquals("Customer Correct: ", orderHistory.get("drink"),
                order.drink);
        assertEquals("Customer Correct: ", orderHistory.get("start_time"),
                order.startTime);
        assertEquals("Seller Correct: ", orderHistory.get("seller_username"),
                order.seller_name);
        assertEquals("Customer Correct: ", orderHistory.get("restaurant_name"),
                order.restaurant_name);
        assertEquals("Customer Correct: ", orderHistory.get("order_location"),
                order.order_location);
    }
}