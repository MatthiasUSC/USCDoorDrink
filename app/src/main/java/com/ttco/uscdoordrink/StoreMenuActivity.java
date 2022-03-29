package com.ttco.uscdoordrink;

import static com.ttco.uscdoordrink.database.DatabaseInterface.getMenuItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ttco.uscdoordrink.database.CurrentOrderEntry;
import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.MenuEntry;
import com.ttco.uscdoordrink.database.MenuListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ClickListener implements View.OnClickListener {
    public MenuEntry menuEntry;

    public ClickListener(MenuEntry menuEntry) {
        this.menuEntry = menuEntry;
    }

    public void onClick(View v){
            SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            // Code here executes on main thread after user presses button
            CurrentOrderEntry currentOrder=new CurrentOrderEntry("0",
            LoginActivity.user.name,
            menuEntry.drinkName,
            formatter.format(date),
            menuEntry.ownerUsername,
            "TODO",
            "TODO");
            DatabaseInterface.addCurrentOrder(currentOrder);
    }
}

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

                menuItem.setOnClickListener(new ClickListener(menu.get(i)));

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