package com.ttco.uscdoordrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.MenuEntry;
import com.ttco.uscdoordrink.database.MenuListener;
import com.ttco.uscdoordrink.database.SingleStoreListener;
import com.ttco.uscdoordrink.database.StoreEntry;
import com.google.android.libraries.places.R.id;
import com.ttco.uscdoordrink.database.TriggerListener;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class StoreEditor extends AppCompatActivity {
    ListView menu;
    ListView menu1;
    boolean type;
    ArrayList<MenuEntry> newMenu;
    ArrayList<String> display;
    EditText storeName;
    EditText storeLocation;
    public Place selectedPlace;
    Context context;
    EditText removeIndex;
    Button removeButton;
    String storeName_now;
    String storeLocation_now;
    private static final String TAG = StoreEditor.class.getSimpleName();
    private PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_editor);
        newMenu = new ArrayList<MenuEntry>();
        display = new ArrayList<String>();
        removeIndex = (EditText) findViewById(R.id.removeIndex);
        menu = (ListView) findViewById(R.id.menu);
        menu1 = (ListView) findViewById(R.id.menu);
        removeButton = (Button) findViewById(R.id.removeButton);
        storeName = (EditText) findViewById(R.id.storeName);
        context = this;
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        }

        placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.storeLocation);

        autocompleteFragment.setHint("Location");
        View fView = autocompleteFragment.getView();

        EditText etTextInput = fView.findViewById(id.places_autocomplete_search_input);
        etTextInput.setTextSize(18.0f);
        etTextInput.setPadding(0,0,0,0);


        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place Picked in Store Editor, name: " +
                        place.getName() + ", ID: " + place.getId() +
                        "coordinates:" + place.getLatLng());
                selectedPlace = place;
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        DatabaseInterface.getStoreOfSeller(LoginActivity.user.name, new SingleStoreListener() {
            @Override
            public void onComplete(StoreEntry store) {
//                String id = store.id;

//                String ownerUsername = store.ownerUsername;
                //System.out.println("reached on complete");
                System.out.println("This is the store name: " + store.storeName);

                if(store != null) {
                    storeName_now = store.storeName;
                    storeLocation_now = store.storeLocation;
                    System.out.println(store.storeLocation);
                    DatabaseInterface.getMenuItems(LoginActivity.user.name, new MenuListener() {
                        @Override
                        public void onComplete(List<MenuEntry> menu) {
                            String id;
                            String drinkName;
                            Boolean isCaffeinated;
                            String price;
                            String discount;
                            String ownerUsername;

                            for (int i = 0; i < menu.size(); i++) {
                                display.add("Item name: " + menu.get(i).drinkName + "\n" + "Price: " + menu.get(i).price + "\n" + "Discount: " + menu.get(i).discount + "\n" + "Is caffeinated: " + menu.get(i).isCaffeinated);
                            }
                            newMenu = (ArrayList<MenuEntry>) menu;
                            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, display);
                            menu1.setAdapter(arrayAdapter);

                            storeName.setText(storeName_now);
//                            storeLocation.setText(storeLocation_now);
                           // System.out.println(" reached");

                        }
                    });
                }
            }
        });
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

    public void removeItem(View view){
        int index = Integer.parseInt(removeIndex.getText().toString());
        if(index < display.size()) {
            display.set(index, "");
            newMenu.set(index, null);
        }

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

    public void submit(View view){

        String name = storeName.getText().toString();

        LatLng placeCoords = selectedPlace.getLatLng();
        String location = placeCoords.latitude + "," + placeCoords.longitude;

        StoreEntry store = new StoreEntry("0", name, location, LoginActivity.user.name);
//        for(int i = 0; i < newMenu.size(); i ++) {
//            DatabaseInterface.addMenuItem(newMenu.get(i));
//        }
        DatabaseInterface.clearMenuItems(LoginActivity.user.name, new TriggerListener() {
            @Override
            public void onComplete() {
                for(int i = 0; i < newMenu.size(); i ++) {
                    if(newMenu.get(i) != null) {
                        DatabaseInterface.addMenuItem(newMenu.get(i));
                    }
                }
                DatabaseInterface.addStore(store);
            }
        });





        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

}