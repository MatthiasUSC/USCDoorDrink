package com.ttco.uscdoordrink;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.OrderHistoryEntry;

import java.util.ArrayList;
import java.util.List;


class CaffeineTest {
    @Test
    public void overdoseTest() {
        List<OrderHistoryEntry> dayOrders = new ArrayList<OrderHistoryEntry>();
        assertFalse(OrderDetails.isOverdoseHistory(dayOrders));

        OrderHistoryEntry testObjDecaf = new OrderHistoryEntry("0", "test", "test",
                "test", "test", "test",
                "test", false, "test");

        OrderHistoryEntry testObjCaf = new OrderHistoryEntry("0", "test", "test",
                "test", "test", "test",
                "test", true, "test");

        dayOrders.add(testObjDecaf);
        assertFalse(OrderDetails.isOverdoseHistory(dayOrders));
        dayOrders.add(testObjDecaf);
        dayOrders.add(testObjDecaf);
        dayOrders.add(testObjDecaf);
        dayOrders.add(testObjDecaf);
        assertFalse(OrderDetails.isOverdoseHistory(dayOrders));
        dayOrders.add(testObjCaf);
        assertFalse(OrderDetails.isOverdoseHistory(dayOrders));
        dayOrders.add(testObjCaf);
        dayOrders.add(testObjCaf);
        dayOrders.add(testObjCaf);
        dayOrders.add(testObjCaf);
        assertTrue(OrderDetails.isOverdoseHistory(dayOrders));
    }
}