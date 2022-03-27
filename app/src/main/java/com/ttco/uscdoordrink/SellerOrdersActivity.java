package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import com.ttco.uscdoordrink.database.CompleteOrderListener;
import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.StoreOrderListener;

import java.util.*;

public class SellerOrdersActivity extends AppCompatActivity {

    private ListView listview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders);

        listview = (ListView) findViewById(R.id.order_list);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final SellerOrder item = (SellerOrder) parent.getItemAtPosition(position);
                DatabaseInterface.completeStoreOrder(item, new CompleteOrderListener() {
                    @Override
                    public void onComplete(Boolean isSuccessful) {
                        refresh(null); // Refresh if order is successful
                    }
                });
            }
        });

        // initial refresh
        refresh(null);
    }

    // Fetches from the database and refreshes order list
    public void refresh(View view) {
        ArrayList<SellerOrder> values;
        // TODO REPLACE bob2 WITH GLOBAL VARIABLE OF SELLER USERNAME THAT IS LOGGED IN, AND SET
        // AT LOGIN
        DatabaseInterface.getCurrentOrders("bob2", new UpdateListListener());
    }

    // Fetches from the database and refreshes order list
    public void goBack(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    // Callback that updates list when database returns orders
    class UpdateListListener implements StoreOrderListener {
        public void onComplete(ArrayList<SellerOrder> orders){
            SellerOrder[] arr = new SellerOrder[orders.size()];
            final ArrayAdapter<SellerOrder> adapter =
                    new SellerOrdersAdapter(getApplicationContext(), orders.toArray(arr));
            listview.setAdapter(adapter);
        }
    }



}