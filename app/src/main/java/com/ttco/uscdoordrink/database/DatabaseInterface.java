package com.ttco.uscdoordrink.database;
import com.google.firebase.firestore.*;
import com.google.android.gms.tasks.*;


import com.ttco.uscdoordrink.*;

import org.w3c.dom.Document;

import java.util.*;


import java.text.SimpleDateFormat;
import java.util.Date;

//TODO make query faster by not searching each
public class DatabaseInterface {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String IS_SELLER_KEY = "is_seller";


    public static void doesUsernameExist(String targetUsername, UsernameExistenceListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String username = (String)data.get(USERNAME_KEY);
                        if(username.equals(targetUsername)){
                            listener.onComplete(true);
                            return;
                        }
                    }
                    listener.onComplete(false);
                } else {
                    listener.onComplete(null);
                }
            }
        });
    }

    public static void getUserProfile(String targetUsername, UserProfileListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String username = (String) data.get(USERNAME_KEY);
                        if (username.equals(targetUsername)) {
                            String password = (String) data.get(PASSWORD_KEY);
                            Boolean is_seller = (Boolean) data.get(IS_SELLER_KEY);
                            UserProfile profile = new UserProfile(username, password, is_seller);
                            listener.onComplete(profile);
                            return;
                        }
                    }
                }
                listener.onComplete(null);
            }
        });
    }

    public static void addUserProfile(String username, String password, boolean isSeller){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put(USERNAME_KEY, username);
        user.put(PASSWORD_KEY, password);
        user.put(IS_SELLER_KEY, isSeller);

        db.collection("users").add(user);
    }

    public static void getStoreOrders(String seller_username, StoreOrderListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("current_orders")
                .whereEqualTo("seller_username", seller_username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<SellerOrder> orders = new ArrayList<SellerOrder>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            orders.add(new SellerOrder(
                                    document.getId(),
                                    (String)data.get("customer_username"),
                                    (String)data.get("drink"),
                                    (String)data.get("start_time"),
                                    (String)data.get("seller_username")));
                        }
                        listener.onComplete(orders);
                    } else {
                        listener.onComplete(null);
                    }
                }
        });
    }

    // Removes store order from current_orders and puts it in order history for customer
    public static void completeStoreOrder(SellerOrder order, CompleteOrderListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("current_orders")
                .document(order.doc_id)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onComplete(true);
                        } else {
                            listener.onComplete(false);
                        }
                    }
                }
        );

        Map<String, Object> complete_order = new HashMap<>();
        complete_order.put("seller_username", order.seller_name);
        complete_order.put("customer_username", order.customer_name);
        complete_order.put("drink", order.drink);
        complete_order.put("start_time", order.startTime);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        complete_order.put("end_time", formatter.format(date));

        db.collection("order_histories").add(complete_order);
    }
}
