package com.ttco.uscdoordrink;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.database.*;
import android.widget.ListView;
import android.widget.RadioButton;
import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.LoginResultListener;
import com.ttco.uscdoordrink.database.StoreOrderListener;
import com.ttco.uscdoordrink.database.UserProfile;
import com.ttco.uscdoordrink.database.UserProfileListener;
import com.ttco.uscdoordrink.database.CustomerOrderListener;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;
import java.util.Calendar;

public class OrderDetails extends AppCompatActivity {
    ArrayList<String> orderHistory;
    ArrayList<SellerOrder> copy_orders;
    ArrayList<String> displayedHistory;
    ListView history;
    View period_button;

    public void toMap(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)


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
                displayedHistory = new ArrayList<String>();
                LocalDateTime now = LocalDateTime.now();
                int year = now.getYear();
                int month = now.getMonth().getValue();
                int day = now.getDayOfWeek().getValue();
                int hour = now.getHour();
                int minute = now.getMinute();
                int second = now.getSecond();


                for (int i = 0; i < orders.size(); i++) {

                    orderHistory.add("Drink: " + orders.get(i).drink + "\n"
                            + "Restaurant: " + orders.get(i).restaurant_name + "\n" +
                            "Time ordered: " + orders.get(i).startTime + "\n" + "Place enjoyed beverage: "
                            //+ orders.get(i).orderLocation
                            + "\n" + "Time order received: " + orders.get(i).endTime
                    );

                    String timeOfDrink = orders.get(i).endTime;
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(timeOfDrink, dtf);
                    int order_month = dateTime.getMonth().getValue();
                    int order_year = dateTime.getYear();
                    int order_day = dateTime.getDayOfWeek().getValue();
                    int order_hour = dateTime.getHour();
                    int order_minute = dateTime.getMinute();
                    int order_second = dateTime.getSecond();

                    long diffInMinutes = java.time.Duration.between(dateTime, now).toMinutes();
                    System.out.println("difference between time in minutes: " + diffInMinutes);



                    //System.out.println(orders.get(i).drink);
                    //System.out.println(orders.get(i).restaurant_name);
                    //System.out.println(orders.get(i).seller_name);
                    //System.out.println(orders.get(i).startTime);
                }
                System.out.println("Year: " + year);
                System.out.println("Month: " + month);
                System.out.println("Day: " + day);

//                System.out.println(orderHistory.get(0));

                ArrayAdapter arrayAdapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, orderHistory);
                history.setAdapter(arrayAdapter);
                period_button.setEnabled(true);
                period_button.setVisibility(View.VISIBLE);
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
        period_button = findViewById(R.id.period_month);
        period_button.setEnabled(false);
        period_button.setVisibility(View.INVISIBLE);
        DatabaseInterface.getCustomerOrder(fullname, new OrderHistoryHandler(this));
    }



}