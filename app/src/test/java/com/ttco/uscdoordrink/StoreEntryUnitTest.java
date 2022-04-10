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
}