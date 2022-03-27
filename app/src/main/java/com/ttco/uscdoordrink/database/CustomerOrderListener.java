package com.ttco.uscdoordrink.database;


import java.util.ArrayList;

public interface CustomerOrderListener {
    void onComplete(ArrayList<OrderHistoryEntry> orders);

}
