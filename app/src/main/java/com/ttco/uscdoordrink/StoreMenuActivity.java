package com.ttco.uscdoordrink;

import static com.ttco.uscdoordrink.database.DatabaseInterface.getMenuItems;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    List<MenuEntry> fullMenuList;
    StoreEntry store = (StoreEntry) MapsActivity.lastClickedMarker.getTag();
    StoreMenuActivity context = this;

    View.OnClickListener menuItemClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            // TODO: Update this to make order correctly
            Button btn = (Button) view;

            MenuEntry selection = (MenuEntry) btn.getTag();

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

            LinearLayout fullMenuLayout = findViewById(R.id.full_store_menu);
            LinearLayout discountMenuLayout = findViewById(R.id.discount_store_menu);

            fullMenuList = new ArrayList<MenuEntry>(menu);

            for(MenuEntry menuItem : menu){

                addItemToMenuLayout(menuItem, fullMenuLayout);

                if(!menuItem.discount.equalsIgnoreCase("0"))
                    addItemToMenuLayout(menuItem, discountMenuLayout);

            }
        }

        private void addItemToMenuLayout(MenuEntry menuItem, LinearLayout menuLayout) {
            // TODO: Align Text Vertically, and Button to the Right

            // Parent Layout

            LinearLayout parent = new LinearLayout(context);

            parent.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            parent.setOrientation(LinearLayout.HORIZONTAL);

            // Item Name - Text View (Children #1)

            TextView itemName = new TextView(context);
            itemName.setText(menuItem.drinkName);
            itemName.setGravity(Gravity.CENTER_VERTICAL);

            // Item Caffeine - Text View (Children #2)

            TextView itemCaff = new TextView(context);

            String decaf = menuItem.isCaffeinated ? "Decaffeinated" : "Caffeinated";
            itemCaff.setText(" (" + decaf + ") ");
            itemCaff.setGravity(Gravity.CENTER_VERTICAL);

            // Item Price - Text View (Children #2)

            TextView itemPrice = new TextView(context);
            TextView discountedPrice = new TextView(context);
            TextView discountedText = new TextView(context);

            double price = Double.parseDouble(menuItem.price);
            itemPrice.setText("$" + String.format("%.2f", price));

            if(!menuItem.discount.equals("0")){
                itemPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                itemPrice.setTextColor(Color.RED);

                double discount = Double.parseDouble(menuItem.price)
                        * (Double.parseDouble(menuItem.discount)/100);

                double finalPrice = Double.parseDouble(menuItem.price) - discount;

                discountedPrice.setText("$" +
                        String.format("%.2f", finalPrice) +
                        "  ");
                discountedPrice.setTextColor(Color.GREEN);

                discountedText.setText(
                        ("\n" +
                                Integer.parseInt(menuItem.discount)
                                + "% OFF!"));
            }


            // Order button (Children #4)

            Button itemButton = new Button(context);
            itemButton.setText("Order");

            itemButton.setOnClickListener(menuItemClick);

            itemButton.setTag(menuItem);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            itemButton.setLayoutParams(params);


            parent.addView(itemName);
            parent.addView(itemCaff);
            parent.addView(discountedPrice);
            parent.addView(itemPrice);
            parent.addView(discountedText);
            parent.addView(itemButton);
            menuLayout.addView(parent);
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

    public void onClickDiscountBtn(View view){

        LinearLayout parent = findViewById(R.id.parent_layout);
        ScrollView scroll = findViewById(R.id.scroll_layout);
        LinearLayout fullMenu = findViewById(R.id.full_store_menu);
        LinearLayout discountMenu = findViewById(R.id.discount_store_menu);

        if(discountMenu.getVisibility() == View.GONE){
            parent.removeView(discountMenu);
            scroll.removeView(fullMenu);

            parent.addView(fullMenu);
            scroll.addView(discountMenu);

            discountMenu.setVisibility(View.VISIBLE);
            fullMenu.setVisibility(View.GONE);
        }
    }

    public void onClickMenuBtn(View view){

        LinearLayout parent = findViewById(R.id.parent_layout);
        ScrollView scroll = findViewById(R.id.scroll_layout);
        LinearLayout fullMenu = findViewById(R.id.full_store_menu);
        LinearLayout discountMenu = findViewById(R.id.discount_store_menu);

        if(fullMenu.getVisibility() == View.GONE){

            parent.removeView(fullMenu);
            scroll.removeView(discountMenu);

            parent.addView(discountMenu);
            scroll.addView(fullMenu);

            discountMenu.setVisibility(View.GONE);
            fullMenu.setVisibility(View.VISIBLE);
        }
    }

}