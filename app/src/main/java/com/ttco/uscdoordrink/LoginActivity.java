package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.database.*;
import android.widget.RadioButton;


import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.UserProfile;
import com.ttco.uscdoordrink.database.UserProfileListener;

public class LoginActivity extends AppCompatActivity {
    EditText lusername;
    EditText lpassword;

    private class LoginEvent implements UserProfileListener{
        String fullname;
        String password;
        LoginEvent(String f, String p){
            fullname = f;
            password = p;
        }
        public void onComplete(UserProfile user){
            if(user == null) {

            }
            else {
                LoginActivity hi = new LoginActivity();
                hi.trans();
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
        System.out.println("The fullname is: " + Fullname);
        System.out.println("The password is: " + Password);
        //Put api firebase
        DatabaseInterface.getUserProfile(Fullname, new LoginEvent(Fullname, Password));
    }
    public void RegisterPage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void trans(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
