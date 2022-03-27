package com.ttco.uscdoordrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.MenuEntry;
import com.ttco.uscdoordrink.database.StoreEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StoreEditor extends AppCompatActivity {
    ListView menu;
    boolean type;
    ArrayList<MenuEntry> newMenu;
    ArrayList<String> display;
    EditText storeName;
    EditText storeLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_editor);
        newMenu = new ArrayList<MenuEntry>();
        display = new ArrayList<String>();
        menu = (ListView) findViewById(R.id.menu);


    }


    public void addItem(View view){
        EditText ItemName = (EditText) findViewById(R.id.ItemName);
        EditText ItemPrice = (EditText) findViewById(R.id.ItemPrice);
        EditText ItemDiscount = (EditText) findViewById(R.id.ItemDiscount);
        String Name = ItemName.getText().toString();
        String Price = ItemPrice.getText().toString();
        String Discount = ItemDiscount.getText().toString();
        MenuEntry newItem = new MenuEntry("0", Name, type, Price, Discount, LoginActivity.user.name);
        newMenu.add(newItem);
        String tempType = "";
        if(type) {
         tempType = "True";
        }
        else{
            tempType = "False";
        }
        display.add("Item name: " + Name + "\n" + "Price: " +  Price + "\n" + "Discount: " + Discount + "\n" + "Is caffeinated: " + tempType);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, display);
        menu.setAdapter(arrayAdapter);


    }


    public void caffiene(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Yes:
                if (checked)
                    type = true;
                break;
            case R.id.No:
                if (checked)
                    type = false;
                break;
        }
    }

    public void submit(View view) {
        EditText storeName = (EditText) findViewById(R.id.storeName);
        EditText storeLocation = (EditText) findViewById(R.id.storeLocation);
        String name = storeName.getText().toString();
        String location = storeName.getText().toString();
        StoreEntry store = new StoreEntry("0", name, location, LoginActivity.user.name);
        for(int i = 0; i < newMenu.size(); i ++) {
            DatabaseInterface.addMenuItem(newMenu.get(i));
        }
        DatabaseInterface.addStore(store);

    }
}