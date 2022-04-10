package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.LoginResultListener;
import com.ttco.uscdoordrink.database.UserProfile;
import com.ttco.uscdoordrink.database.UserProfileListener;

public class LoginActivity extends AppCompatActivity {
    EditText lusername;
    EditText lpassword;
    static User user;
    private class UserEvent implements UserProfileListener {
        Context context;
        UserEvent(Context c){
            context = c;
        }
        @Override
        public void onComplete(UserProfile userProfile) {
            if (userProfile == null) {

            } else {
                user = new User(userProfile.username, userProfile.password, userProfile.isSeller);
                System.out.println("Reached point of logging in");
                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);

            }
        }
    }


        private class LoginEvent implements LoginResultListener {
            String fullname;
            Context context;
            LoginEvent(String f, Context c){
                fullname = f;
                context = c;
            }
            public void onComplete(Boolean isSuccessful) {
                if(isSuccessful){
                    DatabaseInterface.getUserProfile(fullname, new UserEvent(context));
                }
            }
        }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lusername = (EditText) findViewById(R.id.name);
        lpassword = (EditText) findViewById(R.id.password);
    }

    public void Loggingin(View view){
        String Fullname = lusername.getText().toString();
        String Password = lpassword.getText().toString();
        //System.out.println("The fullname is: " + Fullname);
        //System.out.println("The password is: " + Password);
        //Put api firebase
        DatabaseInterface.getLoginResult(Fullname, Password, new LoginEvent(Fullname, this));

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }
    public void RegisterPage(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void trans(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}
