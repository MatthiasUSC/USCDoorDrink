package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Boolean HasClicked;
    Boolean type;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);

    }
    public void RadioButtonhandler(View view){
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Seller:
                if (checked)
                    type = true;
                    break;
            case R.id.Buyer:
                if (checked)
                    type = false;
                    break;
        }
    }
    public void Registering(View view){
        String Fullname = username.getText().toString();
        String Password = password.getText().toString();
        //System.out.println("The fullname is: " + Fullname);
        //System.out.println("The password is: " + Password);
        System.out.println("Type: " + type);
        //Put api firebase
    }
    public void LoginPage(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}