package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.OrderHistoryEntry;
import com.ttco.uscdoordrink.database.UsernameExistenceListener;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Boolean HasClicked;
    Boolean type;
    Boolean finalType;
    String Fullname;
    String Password;
    static boolean newUser;
    private class UserHandler implements UsernameExistenceListener{
        String fullname;
        String password;
        Boolean type;
        UserHandler(String full, String pass, Boolean type){
            this.fullname = full;
            this.password = pass;
            this.type = type;
        }
        public void onComplete(Boolean doesUsernameExist) {
            if(doesUsernameExist == null){
                //say try again
            }
            else if(doesUsernameExist == true){

            }
            else{
                System.out.println("Adding in information");
                DatabaseInterface.addUserProfile(fullname, password, type);
            }
        }
    }
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        //populate_order_history();
    }

    // Creates a lot of mock order histories
    void populate_order_history(){
        for(int i = 0; i < 10; i++) {
            OrderHistoryEntry testEntry = new OrderHistoryEntry("0", "hello", "water", "27/03/2022 20:15:59", "chipole" + String.valueOf(i), "bob2", "27/03/2022 20:15:59", true, "here");
            DatabaseInterface.addOrderHistory(testEntry.toHashMap());
        }
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
        Fullname = username.getText().toString();
        Password = password.getText().toString();
        //System.out.println("The fullname is: " + Fullname);
        //System.out.println("The password is: " + Password);
        finalType = type;

        //System.out.println("Type: " + type);
        //Put api firebase

        DatabaseInterface.doesUsernameExist(Fullname, new UserHandler(Fullname, Password, finalType));
    }

    public void LoginPage(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
