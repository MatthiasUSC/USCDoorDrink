package com.ttco.uscdoordrink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentOrderEntryUnitTest {
    @Test
    public void conversionTest() {
        CurrentOrderEntry testEntry = new CurrentOrderEntry("test", "test",
                "test", "test", "test",
                "test", "test", true);
        assertEquals(testEntry, new CurrentOrderEntry("test", testEntry.toMap()));
    }

    @Test
    public void equalsSameInstanceTest(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = new Date();

        CurrentOrderEntry testEntry = new CurrentOrderEntry("123", "pepe",
                "tea", formatter.format(startDate), "cesar",
                "coolders", "california", true);

        assertTrue(testEntry.equals(testEntry));
    }

    @Test
    public void equalsNullObjectTest(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = new Date();

        CurrentOrderEntry testEntry = new CurrentOrderEntry("123", "pepe",
                "tea", formatter.format(startDate), "cesar",
                "coolders", "california", true);

        assertFalse(testEntry.equals(null));
    }

    @Test
    public void equalsDifferentClassTest(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = new Date();

        CurrentOrderEntry testEntry = new CurrentOrderEntry("123", "pepe",
                "tea", formatter.format(startDate), "cesar",
                "coolders", "california", true);


        OrderHistoryEntry diffClassEntry = new OrderHistoryEntry("456", testEntry.toMap());

        assertFalse(testEntry.equals(diffClassEntry));
    }

    @Test
    public void equalsSameEntryDiffObjectTest(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = new Date();

        CurrentOrderEntry testEntry1 = new CurrentOrderEntry("123", "pepe",
                "tea", formatter.format(startDate), "cesar",
                "coolders", "california", true);

        CurrentOrderEntry testEntry2 = new CurrentOrderEntry("123", "pepe",
                "tea", formatter.format(startDate), "cesar",
                "coolders", "california", true);


        assertTrue(testEntry1.equals(testEntry2));
    }

    @Test
    public void equalsDiffEntryTest(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = new Date();

        CurrentOrderEntry testEntry1 = new CurrentOrderEntry("123", "pepe",
                "tea", formatter.format(startDate), "cesar",
                "coolders", "california", true);

        CurrentOrderEntry testEntry2 = new CurrentOrderEntry("123", "pepe",
                "tea", formatter.format(startDate), "cesar",
                "coolders", "california", false);


        assertFalse(testEntry1.equals(testEntry2));
    }


}