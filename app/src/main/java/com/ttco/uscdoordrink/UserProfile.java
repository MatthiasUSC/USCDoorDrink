package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.UserProfileListener;

import java.util.Map;

public class UserProfile extends AppCompatActivity {
    TextView username;
    TextView password;
    TextView type;
    EditText newPassword;
//    EditText newUsername;
    Boolean currType = false;
    String currPassword = "";
    String currUsername = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.editPassword);
//        newUsername = (EditText) findViewById(R.id.editUsername);

        DatabaseInterface.getUserProfile(LoginActivity.user.name, new UserProfileListener() {
            @Override
            public void onComplete(com.ttco.uscdoordrink.database.UserProfile userProfile) {
                if(userProfile == null){}
                else {
                    type = (TextView) findViewById(R.id.type);
                    username.setText(userProfile.username);
                    password.setText(userProfile.password);
                    currUsername = userProfile.username;
                    currPassword = userProfile.password;
                    currType = userProfile.isSeller;
                    if(userProfile.isSeller){
                        type.setText("Seller");
                    }
                    else {
                        type.setText("Customer");
                    }


                }

            }
        }); //DatabaseInterface.getCustomerOrderHistory(fullname, new OrderHistoryHandler(this));
    }



    public void ChangePassword(View view) {
        currPassword = newPassword.getText().toString();
        DatabaseInterface.updatePassword(currUsername, currPassword);
    }

//    public void ChangeUsername(View view) {
//        String newUsername = username.getText().toString();
//        LoginActivity.user.name = newUsername;
//        DatabaseInterface.updateUsername(currUsername, newUsername);
//    }
}