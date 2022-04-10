package com.ttco.uscdoordrink;

import static com.ttco.uscdoordrink.database.DatabaseInterface.getMenuItems;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class StoreMenuActivity extends AppCompatActivity {

    List<MenuEntry> fullMenu;
    StoreEntry store = (StoreEntry) MapsActivity.lastClickedMarker.getTag();
    StoreMenuActivity context = this;

    View.OnClickListener menuItemClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Button btn = (Button) view;
            String drinkName = btn.getText().toString();

            MenuEntry selection = null;

            for(MenuEntry item : fullMenu){
                if(item.drinkName.equalsIgnoreCase(drinkName)){
                    selection = item;
                    break;
                }
            }

            if(selection == null) return;

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            // LoginActivity.user.name
            // Code here executes on main thread after user presses button
            CurrentOrderEntry currentOrder = new CurrentOrderEntry("0",
                    LoginActivity.user.name,
                    selection.drinkName,
                    formatter.format(date),
                    selection.ownerUsername,
                    store.storeName,
                    store.storeLocation,
                    selection.isCaffeinated);

            DatabaseInterface.addCurrentOrder(currentOrder);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Order created succesfully!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };


    private class GetMenuEvent implements MenuListener {
        Context context;
        GetMenuEvent(Context c){
            context = c;
        }
        @Override
        public void onComplete(List<MenuEntry> menu) {
            LinearLayout storeMenu = findViewById(R.id.store_menu);

            fullMenu = new ArrayList<MenuEntry>(menu);

            for(MenuEntry menuItem : menu){
                Button itemButton = new Button(context);
                String decaf = menuItem.isCaffeinated ? "Decaffeinated" : "Caffeinated";
                itemButton.setText(menuItem.drinkName + " (" + decaf + ") " + menuItem.price);

                itemButton.setOnClickListener(menuItemClick);

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
        getMenuItems(store.ownerUsername, new GetMenuEvent(this));

        TextView storeName = findViewById(R.id.store_name);
        storeName.setText(store.storeName);
    }

    public void onClickReturnBtn(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}