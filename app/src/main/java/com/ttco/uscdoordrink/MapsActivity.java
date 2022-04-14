package com.ttco.uscdoordrink;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
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
import com.ttco.uscdoordrink.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final LatLng defaultLocation = new LatLng(34.0251724688, -118.290905569);

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private Polyline lastPolyline = null;
    private Duration lastDuration;
    private TravelMode currentTravelMode = TravelMode.DRIVING;
    public static Marker lastClickedMarker = null;

    public List<StoreEntry> fetchedStores;
    protected MyApp mMyApp ;

    public HashMap<String, Marker> markers;

    private class StoreFetch implements StoreListener {
        @Override
        public void onComplete(List<StoreEntry> stores) {
            /*
            This is were the stores are fetched and added to a list
             */

            markers = new HashMap<>();

            fetchedStores = stores;

            Marker marker;

            for(StoreEntry store : stores){

                String coordString = store.storeLocation;
                double storeLat = Double.parseDouble(coordString.split(",")[0]);
                double storeLng = Double.parseDouble(coordString.split(",")[1]);

                LatLng pos = new LatLng(storeLat, storeLng);
                marker = map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(store.storeName));

                marker.setTag(store);
                markers.put(store.storeName, marker);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: Hide buttons that do not correspond to user

        super.onCreate(savedInstanceState);
        mMyApp = (MyApp) this .getApplicationContext() ;

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        /*
         * binding = ActivityMapsBinding.inflate(getLayoutInflater());
         * setContentView(binding.getRoot());
         */
        setContentView(R.layout.activity_maps);

        genFusionLocationProviderClient();

        setUpMapReadyNotification();

        buildGeoApiContext();
    }

    /**
     * Construct a FusedLocationProviderClient.
     */
    public FusedLocationProviderClient genFusionLocationProviderClient(){

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        return this.fusedLocationProviderClient;

    }

    /**
     * Obtain the SupportMapFragment and get notified when the map is ready to be used.
     */
    public void setUpMapReadyNotification(){
        // Obtain the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // Setup map ready notification
        mapFragment.getMapAsync(this);
    }

    /**
     * Build a GeoApiContext Manager given the Google Maps API Key stored
     * @return
     */
    public GeoApiContext buildGeoApiContext(){
        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_api_key))
                    .build();
        }
        return mGeoApiContext;
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

        // Request the user permission location.
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        // Fetch stores from DB and populate map
        DatabaseInterface.getStores(new StoreFetch());

    }

    private boolean getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */


        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;
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
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
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

        if(lastKnownLocation == null){
            getDeviceLocation();
        }else{
            calculateDirections(marker);
        }

        // TODO: Show open menu hidden button
        return false;
    }

    public void onClickTravelModeBtn(View view){
        Button b = (Button)view;
        String buttonText = b.getText().toString();

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
                    Log.d(TAG,  "run: leg: " + route.legs[0].toString());
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

    public TravelMode getCurrentTravelMode(){
        return this.currentTravelMode;
    }

    public void setLastKnownLocation(Location newLocation){
        this.lastKnownLocation = newLocation;
    }

    protected void onResume () {
        super .onResume() ;
        mMyApp .setCurrentActivity( this ) ;
    }
    protected void onPause () {
        clearReferences() ;
        super .onPause() ;
    }
    protected void onDestroy () {
        clearReferences() ;
        super .onDestroy() ;
    }
    private void clearReferences () {
        Activity currActivity = mMyApp .getCurrentActivity() ;
        if ( this .equals(currActivity))
            mMyApp .setCurrentActivity( null ) ;
    }
}