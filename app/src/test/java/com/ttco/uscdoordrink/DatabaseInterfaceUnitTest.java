package com.ttco.uscdoordrink;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ttco.uscdoordrink.database.*;

import java.util.Map;

class DatabaseInterfaceUnitTest {
    @Test
    public void userCreationTest() {
        Map<String, Object> map = DatabaseInterface.createUsersEntry("test_user", "test_pass", true);
        assertEquals(map.get(DatabaseInterface.USERNAME_KEY), "test_user");
        assertEquals(map.get(DatabaseInterface.PASSWORD_KEY), "test_pass");
        assertEquals(map.get(DatabaseInterface.IS_SELLER_KEY), true);
    }
}