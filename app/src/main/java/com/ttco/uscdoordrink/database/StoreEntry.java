package com.ttco.uscdoordrink.database;

import java.util.HashMap;
import java.util.Map;

public class StoreEntry {
    public static final String FIELD_STORE_NAME = "store_name";
    public static final String FIELD_STORE_LOCATION = "store_location";
    public static final String FIELD_OWNER_USERNAME = "owner_username";

    public String storeName;
    public String storeLocation;
    public String ownerUsername;

    public StoreEntry(String storeName, String storeLocation, String ownerUsername) {
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.ownerUsername = ownerUsername;
    }

    public StoreEntry(Map<String, Object> map){
        this.storeName = (String)map.get(FIELD_STORE_NAME);
        this.storeLocation = (String)map.get(FIELD_STORE_LOCATION );
        this.ownerUsername = (String)map.get(FIELD_OWNER_USERNAME);
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FIELD_STORE_NAME , storeName);
        map.put(FIELD_STORE_LOCATION, storeLocation);
        map.put(FIELD_OWNER_USERNAME, ownerUsername);
        return map;
    }
}
