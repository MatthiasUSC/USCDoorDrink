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

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Button;



public class OrderDetailsActivity extends AppCompatActivity {
    ArrayList<String> orderHistory;
    ArrayList<OrderHistoryEntry> copy_orders;
    static ArrayList<String> displayedHistoryMonth;
    static ArrayList<String> displayedHistoryYear;
    static ArrayList<String> displayedHistoryDay;
    static boolean databaseComplete = false;
    String ReccomendedName;
    ListView history;
    Button period_button;
    public static int cycle;
    public static ArrayList<String> now;
    public void toMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public static String GetPeriod(){
        String period;
        cycle = (cycle + 1) % 3;
        if(cycle == 0) {
            period = "Month";
            now = displayedHistoryMonth;
        }
        else if(cycle == 1){
            period = "Day";
            now = displayedHistoryDay;
        }
        else {
            period = "Year";
            now = displayedHistoryYear;
        }
        return period;
    }
    public void changePeriod(View view) {
        String curr_Month = GetPeriod();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, now);
        history.setAdapter(arrayAdapter);
        period_button.setText(curr_Month);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static HashMap<String, Integer> getDate(LocalDateTime Time){
        HashMap<String, Integer> dateAttr = new HashMap<String, Integer>();
        dateAttr.put("year", Time.getYear());
        dateAttr.put("month", Time.getMonth().getValue());
        dateAttr.put("day", Time.getDayOfWeek().getValue());
        dateAttr.put("hour", Time.getHour());
        dateAttr.put("minute", Time.getMinute());
        dateAttr.put("second", Time.getSecond());
        return dateAttr;

    }

    public static String OrderToString(String drink, String restaurant_name, String startTime, String order_location, String endTime){
        String history;
        history = "Drink: " + drink + "\n"
                + "Restaurant: " + restaurant_name + "\n" +
                "Time ordered: " + startTime + "\n" + "Place enjoyed beverage: "
                + order_location
                + "\n" + "Time order received: " + endTime;
        return history;
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
                HashMap<String, Integer> DateAttr = getDate(LocalDateTime.now());
                int year = DateAttr.get("year");
                int month =DateAttr.get("month");
                int day = DateAttr.get("day");
                int hour = DateAttr.get("hour");
                int minute = DateAttr.get("minute");
                int second = DateAttr.get("second");
                System.out.println(year);

                // For check caffeine warning
                ArrayList<OrderHistoryEntry> dayOrders = new ArrayList<OrderHistoryEntry>();

                for (int i = 0; i < orders.size(); i++) {
                    System.out.println(orders.get(i).drink);
                    orderHistory.add(OrderToString(orders.get(i).drink, orders.get(i).restaurant_name,  orders.get(i).startTime, orders.get(i).order_location, orders.get(i).endTime));

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
                                + orders.get(i).order_location
                                + "\n" + "Time order received: " + orders.get(i).endTime);
                    }
                    if(order_year == year){
                        displayedHistoryYear.add("Drink: " + orders.get(i).drink + "\n"
                                + "Restaurant: " + orders.get(i).restaurant_name + "\n" +
                                "Time ordered: " + orders.get(i).startTime + "\n" + "Place enjoyed beverage: "
                                + orders.get(i).order_location
                                + "\n" + "Time order received: " + orders.get(i).endTime);
                    }
                    if(order_day == day){
                        displayedHistoryDay.add("Drink: " + orders.get(i).drink + "\n"
                                + "Restaurant: " + orders.get(i).restaurant_name + "\n" +
                                "Time ordered: " + orders.get(i).startTime + "\n" + "Place enjoyed beverage: "
                                + orders.get(i).order_location
                                + "\n" + "Time order received: " + orders.get(i).endTime);

                        dayOrders.add(orders.get(i));
                    }

                    System.out.println(orders.get(i).drink);
                    System.out.println(orders.get(i).restaurant_name);
                    System.out.println(orders.get(i).seller_name);
                    System.out.println(orders.get(i).startTime);
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

        databaseComplete = true;


        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String fullname;


        fullname = LoginActivity.user.name;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        createNotificationChannel();
        System.out.println("The name of the customer: " + fullname);
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

    public static String MakeRec(String name){
        if(!name.equals("")) {
            String rec = "You visited " + name + " the most this month we recommend you visit this place again.";
            return rec;
        }
        else {
            String rec = "You haven't had anything to drink yet - grab some drinks here";
            return rec;
        }
    }
    public void sendRecommendation(){
        NotificationCompat.Builder builder;
        String rec = MakeRec(ReccomendedName);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Recommendation")
                .setContentText(rec)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(rec))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
    }

    // This function is assuming all the orders are within the same day
    public void checkForOverdose(List<OrderHistoryEntry> dayOrders){
        if(isOverdoseHistory(dayOrders)){
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
            int notificationId = 1;
            notificationManager.notify(notificationId, builder.build());
        }
    }

    public static boolean isOverdoseHistory(List<OrderHistoryEntry> dayOrders){
        int caffeinatedDrinks = 0;
        for(OrderHistoryEntry order : dayOrders) {
            if(order.isCaffeinated){
                caffeinatedDrinks += 1;
            }
        }
        return caffeinatedDrinks > 5;
    }
}