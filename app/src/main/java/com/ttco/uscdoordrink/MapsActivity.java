package com.ttco.uscdoordrink;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;
import com.ttco.uscdoordrink.database.DatabaseInterface;
import com.ttco.uscdoordrink.database.StoreEntry;
import com.ttco.uscdoordrink.database.StoreListener;
import com.ttco.uscdoordrink.database.UserProfile;
import com.ttco.uscdoordrink.database.UserProfileListener;
import com.ttco.uscdoordrink.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.android.gms.maps.model.Polyline;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private ActivityMapsBinding binding;

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    private GeoApiContext mGeoApiContext = null;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    private static final int DEFAULT_ZOOM = 15;
    private final LatLng defaultLocation = new LatLng(34.022165, -118.285112);

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private Polyline lastPolyline = null;
    private Duration lastDuration;
    private TravelMode currentTravelMode = TravelMode.DRIVING;
    public static Marker lastClickedMarker = null;

    private class StoreFetch implements StoreListener {
        @Override
        public void onComplete(List<StoreEntry> stores) {
            /*
            This is were the stores are fetched and added to a list
             */

            // TODO: Change to actual coord of stores
            double lat = 34.019709;
            double lng = -118.291449;
            int i = 0;
            System.out.println("\n\n\n+++++++++++\n" + stores.toString() + "\n\n\n+++++++++++\n");
            for(StoreEntry store : stores){
                System.out.println("\n\n\n+++++++++++\n" + store.toString() + "\n\n\n+++++++++++\n");
                LatLng pos = new LatLng(lat + i*0.001, lng + i*0.002);
                map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(store.storeName))
                        .setTag(store);
                i++;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        /*
         * binding = ActivityMapsBinding.inflate(getLayoutInflater());
         * setContentView(binding.getRoot());
         */
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_api_key))
                    .build();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.map.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // TODO: Populate map from DB
        /*
        double lat = 34.019709;
        double lng = -118.291449;

        for(int i = 0; i < 6; i++){
            LatLng pos = new LatLng(lat + i*0.001, lng + i*0.002);
            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("Store #" + i));
        }
        */

        DatabaseInterface.getStores(new StoreFetch());


        /*
        double lat = lastKnownLocation.getLatitude();
        double lng = lastKnownLocation.getLongitude();
        LatLng location = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(location).title("Marker in Joe Momma"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        */
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    // [END maps_current_place_on_request_permissions_result]

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }else{
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(defaultLocation.latitude,
                                                defaultLocation.longitude), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        lastClickedMarker = marker;
        calculateDirections(marker);
        // TODO: Show hidden buttons
        return false;
    }

    public void onClickTravelModeBtn(View view){
        Button b = (Button)view;
        String buttonText = b.getText().toString();

        // TODO: Highlight last clicked button

        if(buttonText.equalsIgnoreCase("Driving")){
            currentTravelMode = TravelMode.DRIVING;
        }else if(buttonText.equalsIgnoreCase("Walking")){
            currentTravelMode = TravelMode.WALKING;
        }else if(buttonText.equalsIgnoreCase("Cycling")) {
            currentTravelMode = TravelMode.BICYCLING;
        }

        if(lastClickedMarker != null){
            calculateDirections(lastClickedMarker);
        }
    }

    public void onClickToSellerOrders(View view){
        if(LoginActivity.user.type == true){ // If seller, then go to seller page
            Intent intent = new Intent(this.getApplicationContext(), SellerOrdersActivity.class);
            startActivity(intent);
        }
    }

    public void onClickToOrderHistory(View view){
        if(LoginActivity.user.type == false){ // If not seller
            Intent intent = new Intent(this.getApplicationContext(), OrderDetails.class);
            startActivity(intent);
        }
    }

    public void onClickToStoreEditor(View view){
        if(LoginActivity.user.type == true){ // If seller
            Intent intent = new Intent(this.getApplicationContext(), StoreEditor.class);
            startActivity(intent);
        }
    }

    private void calculateDirections(Marker marker){
        Log.d(TAG, "calculateDirections: calculating directions.");
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(false);
        if(lastKnownLocation != null){
            directions.origin(
                    new com.google.maps.model.LatLng(
                            lastKnownLocation.getLatitude(),
                            lastKnownLocation.getLongitude()
                    )
            );
        }else{
            directions.origin(
                    new com.google.maps.model.LatLng(
                            defaultLocation.latitude,
                            defaultLocation.longitude
                    ));
        }

        directions.mode(currentTravelMode);

        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
                Log.d(TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "onFailure: " + e.getMessage() );

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                if(lastPolyline != null){
                    lastPolyline.remove();
                }

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }

                    Polyline polyline = map.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(Color.CYAN);
                    polyline.setClickable(true);

                    lastDuration = route.legs[0].duration;
                    lastPolyline = polyline;

                    lastClickedMarker.setSnippet("Delivery Time (" + currentTravelMode.toString() + "): "
                            + lastDuration);
                    lastClickedMarker.showInfoWindow();
                }
            }
        });
    }

    public void onClickOpenMenuBtn(View view) {
        if(lastClickedMarker != null){
            Intent intent = new Intent(this, StoreMenuActivity.class);
            startActivity(intent);
        }
    }
}