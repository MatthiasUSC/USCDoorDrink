package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.database.*;
import android.widget.RadioButton;
import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.LoginResultListener;
import com.ttco.uscdoordrink.database.StoreOrderListener;
import com.ttco.uscdoordrink.database.UserProfile;
import com.ttco.uscdoordrink.database.UserProfileListener;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {
    private class StoreOrderHandler implements StoreOrderListener{

        @Override
        public void onComplete(ArrayList<SellerOrder> orders) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        String fullname = LoginActivity.user.name;
        DatabaseInterface.getStoreOrders(fullname, new StoreOrderHandler);

    }
}