package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ttco.uscdoordrink.database.MenuEntry;

import java.util.ArrayList;

public class StoreMenuActivity extends AppCompatActivity {



    ArrayList<MenuEntry> cart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_menu);

        LinearLayout storeMenu = findViewById(R.id.store_menu);


        for(int i = 0; i < 20; i++){
            Button menuItem = new Button(this);
            menuItem.setText("pipisote " + i);

            menuItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // Code here executes on main thread after user presses button


                }
            });

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            menuItem.setLayoutParams(params2);
            storeMenu.addView(menuItem);
        }
    }
}