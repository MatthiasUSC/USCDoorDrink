package com.ttco.uscdoordrink.database;
import com.google.firebase.firestore.*;
import com.google.android.gms.tasks.*;

import java.util.HashMap;
import java.util.Map;


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

    public static void getStoreOrders
}
