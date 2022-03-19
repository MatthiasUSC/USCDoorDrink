package com.ttco.uscdoordrink.database;

import com.ttco.uscdoordrink.*;
import java.util.*;

public interface StoreOrderListener {
    void onComplete(ArrayList<SellerOrder> orders);
}
