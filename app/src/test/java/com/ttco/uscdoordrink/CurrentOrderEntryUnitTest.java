package com.ttco.uscdoordrink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.*;

public class CurrentOrderEntryUnitTest {
    @Test
    public void conversionTest() {
        CurrentOrderEntry testEntry = new CurrentOrderEntry("test", "test",
                "test", "test", "test",
                "test", "test", true);
        assertEquals(testEntry, new CurrentOrderEntry("test", testEntry.toMap()));
    }

    @Test
    public void equalsTest(){

    }
}