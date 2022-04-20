package com.ttco.uscdoordrink.database;

import com.google.firebase.firestore.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.*;


import java.text.SimpleDateFormat;
import java.util.Date;

//TODO make query faster by not searching each
public class DatabaseInterface {
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_CURRENT_ORDERS = "current_orders";
    public static final String COLLECTION_ORDER_HISTORIES = "order_histories";
    public static final String COLLECTION_STORES = "stores";
    public static final String COLLECTION_MENU_ITEMS = "menu_items";
    
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String IS_SELLER_KEY = "is_seller";

    public static Map<String, Object> createCurrentOrderEntry(){
        return null;
    }

    // TODO replace string constants with variable constants in OrderHistoryEntry
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

        db.collection(COLLECTION_USERS)
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

        db.collection(COLLECTION_USERS)
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
        db.collection(COLLECTION_USERS).add(user);
    }

    public static void getLoginResult(String username, String password, LoginResultListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS)
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

        db.collection(COLLECTION_CURRENT_ORDERS)
                .whereEqualTo(CurrentOrderEntry.FIELD_SELLER_NAME, seller_username)
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
                        System.out.println("Orders fetched successfully");
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

        db.collection(COLLECTION_ORDER_HISTORIES)
                .whereEqualTo(OrderHistoryEntry.FIELD_CUSTOMER_NAME, customer_username)
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
        db.collection(COLLECTION_CURRENT_ORDERS)
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

    // Adds a order history entry to the current_orders collection
    public static void addCurrentOrder(CurrentOrderEntry current_order){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_CURRENT_ORDERS).add(current_order.toMap());
    }

    // Adds a order history entry to the order_histories collection
    public static void addOrderHistory(Map<String, Object> complete_order){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_ORDER_HISTORIES).add(complete_order);
    }

    // Adds a store entry to the store collection
    public static void addStore(StoreEntry store){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_STORES).add(store.toMap());
    }

    // Adds a menu entry to the menu_items collection
    public static void addMenuItem(MenuEntry menuItem){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_MENU_ITEMS).add(menuItem.toMap());
    }

    //Gets all menu entries linked to a specific seller
    public static void getMenuItems(String seller_name, MenuListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(COLLECTION_MENU_ITEMS)
                .whereEqualTo(MenuEntry.FIELD_OWNER_USERNAME, seller_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<MenuEntry> menu = new ArrayList<MenuEntry>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                menu.add(new MenuEntry(document.getId(), data));
                            }
                            listener.onComplete(menu);
                        } else {
                            listener.onComplete(null);
                        }
                    }
                });
    }

    // Gets all stores in the database (doesn't scale)
    public static void getStores(StoreListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(COLLECTION_STORES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<StoreEntry> stores = new ArrayList<StoreEntry>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                stores.add(new StoreEntry(document.getId(), data));
                            }
                            listener.onComplete(stores);
                        } else {
                            listener.onComplete(null);
                        }
                    }
                });
    }

    // Update username field (NOT SAFE, MUST CHECK IF NEW USERNAME EXISTS FIRST)
    public static void updateUsername(String username, String new_username){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS)
                .whereEqualTo(UserProfile.FIELD_USERNAME, username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection(COLLECTION_USERS)
                                        .document(document.getId())
                                        .update(UserProfile.FIELD_USERNAME, new_username);
                                break;
                            }
                        }
                    }
                });
    }

    // Update password field
    public static void updatePassword(String username, String new_password){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS)
                .whereEqualTo(UserProfile.FIELD_USERNAME, username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection(COLLECTION_USERS)
                                        .document(document.getId())
                                        .update(UserProfile.FIELD_PASSWORD, new_password);
                                break;
                            }
                        }
                    }
                });
    }
}
