package com.ttco.uscdoordrink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuEntryUnitTest {
    @Test
    public void conversionTest() {
        MenuEntry testEntry = new MenuEntry("test", "test",
                true, "test", "test", "test");
        assertEquals(testEntry, new MenuEntry("test", testEntry.toMap()));
    }

    @Test
    public void equalsSameInstanceTest(){
        MenuEntry testEntry = new MenuEntry("123", "tea",
                true, "$2", "", "pepe");

        assertTrue(testEntry.equals(testEntry));
    }

    @Test
    public void equalsNullObjectTest(){
        MenuEntry testEntry = new MenuEntry("123", "tea",
                true, "$2", "", "pepe");

        assertFalse(testEntry.equals(null));
    }

    @Test
    public void equalsDifferentClassTest(){
        MenuEntry testEntry = new MenuEntry("123", "tea",
                true, "$2", "", "pepe");


        OrderHistoryEntry diffClassEntry = new OrderHistoryEntry("456", testEntry.toMap());

        assertFalse(testEntry.equals(diffClassEntry));
    }

    @Test
    public void equalsSameEntryDiffObjectTest(){
        MenuEntry testEntry1 = new MenuEntry("123", "tea",
                true, "$2", "", "pepe");

        MenuEntry testEntry2 = new MenuEntry("123", "tea",
                true, "$2", "", "pepe");


        assertTrue(testEntry1.equals(testEntry2));
    }

    @Test
    public void equalsDiffEntryTest(){
        MenuEntry testEntry1 = new MenuEntry("123", "water",
                true, "$2", "", "pepe");

        MenuEntry testEntry2 = new MenuEntry("123", "tea",
                true, "$2", "", "pepe");


        assertFalse(testEntry1.equals(testEntry2));
    }
}