package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.UsernameExistenceListener;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Boolean HasClicked;
    Boolean type;
    Boolean finalType;
    String Fullname;
    String Password;

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
        createNotificationChannel();
    }

    // Needed for notifications
    public static final String CHANNEL_NAME = "notification_channel";
    public static final String CHANNEL_DESCRIPTION = "A channel for notifications.";
    public static final String CHANNEL_ID = "0";
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Test
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
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
