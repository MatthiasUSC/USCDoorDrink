package com.ttco.uscdoordrink.database;
import com.google.firebase.firestore.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.*;


import java.text.SimpleDateFormat;
import java.util.Date;

//TODO make query faster by not searching each
public class DatabaseInterface {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String IS_SELLER_KEY = "is_seller";

    public static Map<String, Object> createCurrentOrderEntry(){
        return null;
    }

    public static Map<String, Object> createOrderHistoryEntryFromCurrentOrderEntry(CurrentOrderEntry order){
        Map<String, Object> complete_order = new HashMap<>();
        complete_order.put("seller_username", order.seller_name);
        complete_order.put("customer_username", order.customer_name);
        complete_order.put("drink", order.drink);
        complete_order.put("start_time", order.startTime);
        complete_order.put("restaurant_name", order.restaurant_name);
        complete_order.put("order_location", order.order_location);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        complete_order.put("end_time", formatter.format(date));
        return complete_order;
    }

    public static Map<String, Object> createUsersEntry(String username, String password, boolean isSeller){
        Map<String, Object> user = new HashMap<>();
        user.put(USERNAME_KEY, username);
        user.put(PASSWORD_KEY, password);
        user.put(IS_SELLER_KEY, isSeller);
        return user;
    }

    public static Map<String, Object> createStoresEntry(){
        return null;
    }

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

    // TODO make more efficient
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

        Map<String, Object> user = createUsersEntry(username, password, isSeller);
        db.collection("users").add(user);
    }

    public static void getLoginResult(String username, String password, LoginResultListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .whereEqualTo(USERNAME_KEY, username)
            .whereEqualTo(PASSWORD_KEY, password)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if(task.getResult().size() > 0){
                            listener.onComplete(true);
                        } else {
                            listener.onComplete(false);
                        }
                    } else {
                        listener.onComplete(null);
                    }
                }
            });
    }

    // Gets all current orders for a given seller
    public static void getCurrentOrders(String seller_username, StoreOrderListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("current_orders")
                .whereEqualTo("seller_username", seller_username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<CurrentOrderEntry> orders = new ArrayList<CurrentOrderEntry>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            orders.add(new CurrentOrderEntry(document.getId(), data));
                        }
                        listener.onComplete(orders);
                    } else {
                        listener.onComplete(null);
                    }
                }
        });
    }

    // Gets all orders in order history for a given customer
    public static void getCustomerOrderHistory(String customer_username, CustomerOrderListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("order_histories")
                .whereEqualTo("customer_username", customer_username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<OrderHistoryEntry> orders = new ArrayList<OrderHistoryEntry>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                orders.add(new OrderHistoryEntry(document.getId(), data));
                            }
                            listener.onComplete(orders);
                        } else {
                            listener.onComplete(null);
                        }
                    }
                });
    }

    // Removes store order from current_orders and puts it in order history for customer
    public static void completeStoreOrder(CurrentOrderEntry order, CompleteOrderListener listener){
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

        // Add completed order to order history.
        addOrderHistory(createOrderHistoryEntryFromCurrentOrderEntry(order));
    }

    // Adds a order history entry to the store collection
    public static void addOrderHistory(Map<String, Object> complete_order){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("order_histories").add(complete_order);
    }

    // Adds a store entry to the store collection
    public static void addStore(StoreEntry store){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("stores").add(store.toMap());
    }

    // Adds a menu entry to the store collection
    public static void addMenuItem(MenuEntry menuItem){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("menu_items").add(menuItem.toMap());
    }
}
