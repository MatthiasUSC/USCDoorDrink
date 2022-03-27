package com.ttco.uscdoordrink;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.CustomerOrderListener;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class OrderDetails extends AppCompatActivity {
    ArrayList<String> orderHistory;
    ArrayList<SellerOrder> copy_orders;
    ListView history;

    public void toMap(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private class OrderHistoryHandler implements CustomerOrderListener{
        Context c;
        OrderHistoryHandler(Context c){
            this.c = c;
        }
        @Override
        public void onComplete(ArrayList<SellerOrder> orders) {
            System.out.println("Loading data....");
            if(orders != null) {
                copy_orders = orders;
                orderHistory = new ArrayList<String>();
                for (int i = 0; i < orders.size(); i++) {
                    orderHistory.add("Drink: " + orders.get(i).drink + "\n"
                            + "Restaurant: " + orders.get(i).restaurant_name + "\n" +
                            "Time ordered: " + orders.get(i).startTime
                    );
                    //System.out.println(orders.get(i).drink);
                    //System.out.println(orders.get(i).restaurant_name);
                    //System.out.println(orders.get(i).seller_name);
                    //System.out.println(orders.get(i).startTime);
                }
                System.out.println(orderHistory.get(0));
                ArrayAdapter arrayAdapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, orderHistory);
                history.setAdapter(arrayAdapter);
            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        String fullname = LoginActivity.user.name;
        System.out.println("The full name is " + fullname);
        history = (ListView) findViewById(R.id.orderChart);
        DatabaseInterface.getCustomerOrderHistory(fullname, new OrderHistoryHandler(this));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
}