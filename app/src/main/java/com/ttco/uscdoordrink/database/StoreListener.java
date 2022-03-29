package com.ttco.uscdoordrink.database;

import java.util.List;

public interface StoreListener {
    void onComplete(List<StoreEntry> stores);
}
