package com.ttco.uscdoordrink;

import static org.junit.Assert.*;

import com.ttco.uscdoordrink.database.UserProfile;

import org.junit.Test;

import java.util.HashMap;

public class UserProfileTest {

    @Test
    public void toHashMapTest() {
        UserProfile user = new UserProfile("pepe", "123", true);

        HashMap<String, Object> hashMap = user.toHashMap();

        assertEquals("Correct Username: ", hashMap.get("username"), user.username);
        assertEquals("Correct Password: ", hashMap.get("password"), user.password);
        assertEquals("Correct isSeller: ", hashMap.get("isSeller"), user.isSeller);
    }
}