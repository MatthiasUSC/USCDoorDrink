package com.ttco.uscdoordrink.database;

import java.util.*;

public interface StoreOrderListener {
    void onComplete(ArrayList<CurrentOrderEntry> orders);
}
