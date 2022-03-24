package com.ttco.uscdoordrink.database;

import com.ttco.uscdoordrink.SellerOrder;


import java.util.ArrayList;

public interface CustomerOrderListener {
    void onComplete(ArrayList<SellerOrder> orders);

}
