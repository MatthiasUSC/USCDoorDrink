package com.ttco.uscdoordrink;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.CustomerOrderListener;
import com.ttco.uscdoordrink.database.OrderHistoryEntry;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;
import android.widget.Button;



public class OrderDetails extends AppCompatActivity {
    ArrayList<String> orderHistory;
    ArrayList<OrderHistoryEntry> copy_orders;
    ArrayList<String> displayedHistoryMonth;
    ArrayList<String> displayedHistoryYear;
    ArrayList<String> displayedHistoryDay;

    String ReccomendedName;
    ListView history;
    Button period_button;
    int cycle;

    public void toMap(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void changePeriod(View view) {
        cycle = (cycle + 1) % 3;
        if(cycle == 0) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayedHistoryMonth);
            history.setAdapter(arrayAdapter);
            period_button.setText("Month");
        }
        else if(cycle == 1){
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayedHistoryDay);
            history.setAdapter(arrayAdapter);
            period_button.setText("Day");

        }
        else {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayedHistoryYear);
            history.setAdapter(arrayAdapter);
            period_button.setText("Year");

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)


    private class OrderHistoryHandler implements CustomerOrderListener{
        Context c;
        OrderHistoryHandler(Context c){
            this.c = c;
        }
        @Override
        public void onComplete(ArrayList<OrderHistoryEntry> orders) {
            System.out.println("Loading data....");
            HashMap<String, Integer> res_visited = new HashMap<>();
            if(orders != null) {
                copy_orders = orders;
                orderHistory = new ArrayList<String>();
                displayedHistoryMonth = new ArrayList<String>();
                displayedHistoryDay = new ArrayList<String>();
                displayedHistoryYear = new ArrayList<String>();
                LocalDateTime now = LocalDateTime.now();
                int year = now.getYear();
                int month = now.getMonth().getValue();
                int day = now.getDayOfWeek().getValue();
                int hour = now.getHour();
                int minute = now.getMinute();
                int second = now.getSecond();

                // For check caffeine warning
                ArrayList<OrderHistoryEntry> dayOrders = new ArrayList<OrderHistoryEntry>();

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

//                    long diffInMinutes = java.time.Duration.between(dateTime, now).toMinutes();
//                    System.out.println("difference between time in minutes: " + diffInMinutes);
                    if(order_month == month) {
                        boolean present = res_visited.containsKey(orders.get(i).restaurant_name);
                        if(present){
                            res_visited.put(orders.get(i).restaurant_name, res_visited.get(orders.get(i).restaurant_name) + 1);
                        }
                        else {
                            res_visited.put(orders.get(i).restaurant_name, 1);
                        }
                        displayedHistoryMonth.add("Drink: " + orders.get(i).drink + "\n"
                                + "Restaurant: " + orders.get(i).restaurant_name + "\n" +
                                "Time ordered: " + orders.get(i).startTime + "\n" + "Place enjoyed beverage: "
                                //+ orders.get(i).orderLocation
                                + "\n" + "Time order received: " + orders.get(i).endTime);
                    }
                    if(order_year == year){
                        displayedHistoryYear.add("Drink: " + orders.get(i).drink + "\n"
                                + "Restaurant: " + orders.get(i).restaurant_name + "\n" +
                                "Time ordered: " + orders.get(i).startTime + "\n" + "Place enjoyed beverage: "
                                //+ orders.get(i).orderLocation
                                + "\n" + "Time order received: " + orders.get(i).endTime);
                    }
                    if(order_day == day){
                        displayedHistoryDay.add("Drink: " + orders.get(i).drink + "\n"
                                + "Restaurant: " + orders.get(i).restaurant_name + "\n" +
                                "Time ordered: " + orders.get(i).startTime + "\n" + "Place enjoyed beverage: "
                                //+ orders.get(i).orderLocation
                                + "\n" + "Time order received: " + orders.get(i).endTime);

                        dayOrders.add(orders.get(i));
                    }

                    //System.out.println(orders.get(i).drink);
                    //System.out.println(orders.get(i).restaurant_name);
                    //System.out.println(orders.get(i).seller_name);
                    //System.out.println(orders.get(i).startTime);
                }

                // Checks for overdose, if more than x amount of caffeinated drinks in a single day
                checkForOverdose(dayOrders);

                System.out.println("Year: " + year);
                System.out.println("Month: " + month);
                System.out.println("Day: " + day);

//                System.out.println(orderHistory.get(0));
                int maxx_num = 0;
                for(String key : res_visited.keySet()) {
                    if(res_visited.get(key) > maxx_num) {
                        ReccomendedName = key;
                        maxx_num = res_visited.get(key);
                    }

                }
                System.out.println("ReccomendedName is: " + ReccomendedName );


                ArrayAdapter arrayAdapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, displayedHistoryMonth);
                history.setAdapter(arrayAdapter);
                period_button.setEnabled(true);
                period_button.setVisibility(View.VISIBLE);
                period_button.setText("Month");
                sendRecommendation();
            }


        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        createNotificationChannel();
        String fullname = LoginActivity.user.name;
        cycle = 0;
        ReccomendedName = "";
        System.out.println("The full name is " + fullname);
        history = (ListView) findViewById(R.id.orderChart);
        period_button = (Button) findViewById(R.id.period);
        period_button.setEnabled(false);
        period_button.setVisibility(View.INVISIBLE);
        DatabaseInterface.getCustomerOrderHistory(fullname, new OrderHistoryHandler(this));
    }


    public static final String CHANNEL_NAME = "notification_channel";
    public static final String CHANNEL_DESCRIPTION = "A channel for notifications.";
    public static final String CHANNEL_ID = "0";
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendRecommendation(){
        NotificationCompat.Builder builder;
        if(!ReccomendedName.equals("")) {
            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.star_on)
                    .setContentTitle("Recommendation")
                    .setContentText("You visited " + ReccomendedName + " the most this month we recommend you visit this place again.")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("You visited " + ReccomendedName + " the most this month we recommend you visit this place again."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        else{
            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.star_on)
                    .setContentTitle("Recommendation")
                    .setContentText("You haven't had anything to drink yet - grab some drinks here")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("You haven't had anything to drink yet - grab some drinks here"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
    }

    // This function is assuming all the orders are within the same day
    public void checkForOverdose(List<OrderHistoryEntry> orders){
        int caffeinatedDrinks = 0;
        for(OrderHistoryEntry order : orders) {
            if(order.isCaffeinated){
                caffeinatedDrinks += 1;
            }
        }
        if(caffeinatedDrinks > 5){
            String quote = "Quote from USDA “Currently, strong evidence shows that consumption of coffee within the " +
            "moderate range (3 to 5 cups per day or up to 400 mg/d caffeine) is not associated with " +
            "increased long-term health risks among healthy individuals.” You have drank more than 5 caffeinated drinks today.";

            // Send notifcation about too much caffeine
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.star_on)
                    .setContentTitle("Alert: Too much caffeine!")
                    .setContentText(quote)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(quote))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            // notificationId is a unique int for each notification that you must define
            int notificationId = 0;
            notificationManager.notify(notificationId, builder.build());
        }
    }
}