package com.ttco.uscdoordrink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.*;

public class StoreEntryUnitTest {
    @Test
    public void conversionTest() {
        StoreEntry testEntry = new StoreEntry("test", "test","test", "test");
        assertEquals(testEntry, new StoreEntry("test", testEntry.toMap()));
    }


    @Test
    public void equalsSameInstanceTest(){
        StoreEntry testEntry = new StoreEntry("123", "tea house",
                "30,30", "el pepe");

        assertTrue(testEntry.equals(testEntry));
    }

    @Test
    public void equalsNullObjectTest(){
        StoreEntry testEntry = new StoreEntry("123", "tea house",
                "30,30", "el pepe");

        assertFalse(testEntry.equals(null));
    }

    @Test
    public void equalsDifferentClassTest(){
        StoreEntry testEntry = new StoreEntry("123", "tea house",
                "30,30", "el pepe");


        OrderHistoryEntry diffClassEntry = new OrderHistoryEntry("456", testEntry.toMap());

        assertFalse(testEntry.equals(diffClassEntry));
    }

    @Test
    public void equalsSameEntryDiffObjectTest(){
        StoreEntry testEntry1 = new StoreEntry("123", "tea house",
                "30,30", "el pepe");

        StoreEntry testEntry2 = new StoreEntry("123", "tea house",
                "30,30", "el pepe");

        assertTrue(testEntry1.equals(testEntry2));
    }

    @Test
    public void equalsDiffEntryTest(){
        StoreEntry testEntry1 = new StoreEntry("123", "coffee house",
                "30,30", "el pepe");

        StoreEntry testEntry2 = new StoreEntry("123", "tea house",
                "30,30", "el pepe");

        assertFalse(testEntry1.equals(testEntry2));
    }
}