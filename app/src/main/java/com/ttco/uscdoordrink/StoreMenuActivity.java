package com.ttco.uscdoordrink;

import static com.ttco.uscdoordrink.database.DatabaseInterface.getMenuItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ttco.uscdoordrink.database.MenuEntry;
import com.ttco.uscdoordrink.database.MenuListener;

import java.util.ArrayList;
import java.util.List;

public class StoreMenuActivity extends AppCompatActivity {



    ArrayList<MenuEntry> cart;
    private class GetMenuEvent implements MenuListener {
        Context context;
        GetMenuEvent(Context c){
            context = c;
        }
        @Override
        public void onComplete(List<MenuEntry> menu) {

            for(int i = 0; i < menu.size(); i++){
                Button menuItem = new Button(context);
                menuItem.setText(menu.get(i).drinkName);

                int finalI = i;
                menuItem.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cart.add(menu.get(finalI));
                        // Code here executes on main thread after user presses button


                    }
                });

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                menuItem.setLayoutParams(params2);


            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_menu);
        getMenuItems(LoginActivity.user.name, new GetMenuEvent(this));
        LinearLayout storeMenu = findViewById(R.id.store_menu);


    }
}