package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class LoginActivity extends AppCompatActivity {
    EditText lusername;
    EditText lpassword;
    @Override
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
    }
    public void RegisterPage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}