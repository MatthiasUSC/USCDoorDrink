package com.ttco.uscdoordrink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.*;

public class MenuEntryUnitTest {
    @Test
    public void conversionTest() {
        MenuEntry testEntry = new MenuEntry("test", "test",
                true, "test", "test", "test");
        assertEquals(testEntry, new MenuEntry("test", testEntry.toMap()));
    }
}