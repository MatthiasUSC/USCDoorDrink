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
import com.ttco.uscdoordrink.database.StoreEntry;

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
            LinearLayout storeMenu = findViewById(R.id.store_menu);
            for(MenuEntry menuItem : menu){
                Button itemButton = new Button(context);
                itemButton.setText(menuItem.drinkName);

                itemButton.setOnClickListener(new ClickListener(menuItem));

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                itemButton.setLayoutParams(params2);
                storeMenu.addView(itemButton);

            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_menu);
        StoreEntry store = (StoreEntry) MapsActivity.lastClickedMarker.getTag();
        System.out.println("++++++++++++++++++++++ STORE USERNAME \n"
                            + store.ownerUsername
                            + "++++++++++++++++++++++ STORE USERNAME \n");
        getMenuItems(store.ownerUsername, new GetMenuEvent(this));



    }
}