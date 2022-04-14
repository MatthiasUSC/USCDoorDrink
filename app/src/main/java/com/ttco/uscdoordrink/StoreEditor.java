package com.ttco.uscdoordrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.ttco.uscdoordrink.database.StoreEntry;
import com.google.android.libraries.places.R.id;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class StoreEditor extends AppCompatActivity {
    ListView menu;
    boolean type;
    ArrayList<MenuEntry> newMenu;
    ArrayList<String> display;
    EditText storeName;
    EditText storeLocation;
    public Place selectedPlace;
    private static final String TAG = StoreEditor.class.getSimpleName();
    private PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_editor);
        newMenu = new ArrayList<MenuEntry>();
        display = new ArrayList<String>();
        menu = (ListView) findViewById(R.id.menu);
        storeName = (EditText) findViewById(R.id.storeName);

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

    public void submit(View view){

        String name = storeName.getText().toString();

        LatLng placeCoords = selectedPlace.getLatLng();
        String location = placeCoords.latitude + "," + placeCoords.longitude;

        StoreEntry store = new StoreEntry("0", name, location, LoginActivity.user.name);
        for(int i = 0; i < newMenu.size(); i ++) {
            DatabaseInterface.addMenuItem(newMenu.get(i));
        }
        DatabaseInterface.addStore(store);

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

}