package com.example.ramin.passenger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ramin.passenger.Adapter.RecyclerSearchLocationAdapter;
import com.example.ramin.passenger.Model.DirectionModel.Leg;
import com.example.ramin.passenger.Model.DirectionModel.LegsDistance;
import com.example.ramin.passenger.Model.DirectionModel.NeshanDirection;
import com.example.ramin.passenger.Model.DirectionModel.Route;
import com.example.ramin.passenger.Model.DirectionModel.Step;
import com.example.ramin.passenger.Model.SearchMap.Item;
import com.example.ramin.passenger.Model.SearchMap.NeshanSearch;
import com.example.ramin.passenger.Model.TripsModel;
import com.example.ramin.passenger.Network.GetDataService;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitClientInstance;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.neshan.core.Bounds;
import org.neshan.core.LngLat;
import org.neshan.core.LngLatVector;
import org.neshan.core.Range;
import org.neshan.core.ViewportBounds;
import org.neshan.core.ViewportPosition;
import org.neshan.geometry.LineGeom;
import org.neshan.graphics.ARGB;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.LineStyle;
import org.neshan.styles.LineStyleCreator;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Line;
import org.neshan.vectorelements.Marker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NavigationActivity extends AppCompatActivity implements RecyclerSearchLocationAdapter.OnSearchItemListener{

    private static final String TAG ="TAG";
    final int BASE_MAP_INDEX = 0;
    final int REQUEST_CODE = 123;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static double originLng;
    public static double originLat;
    public static double destinationLng;
    public static double destinationLat;
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    List<Item> items;
    List<Route> routes;
    List<Leg> legs;
    List<PolylineEncoding.LatLng> decodedStepByStepPath;
    String lastUpdateTime,distance,pathTrip;
    Boolean mRequestingLocationUpdates;
    MapView map;
    VectorElementLayer userMarkerLayer,markerLayer,lineLayer;
    FloatingActionButton fab;
    Toolbar navigationToolbar;
    RecyclerSearchLocationAdapter adapter;
    RecyclerView searchLocationRecycler;
    EditText etOriginSearchLocation,etDestinationSearchLocation;
    Button btnSendToInsertTrip,btnDirection;
    boolean overview = false;
    TripsModel modelll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // everything related to ui is initialized here
        initLayoutReferences();
        initLocation();
        startReceivingLocationUpdates();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdate();
    }

    // Initializing layout references (views, map and map events)
    private void initLayoutReferences(){
        //initialising views

        initViews();
        // Initializing mapView element

        initMap();

        etOriginSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchLocationRecycler.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(s.toString());
            }
        });

        etDestinationSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchLocationRecycler.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(s.toString());
            }
        });
    }

    // We use findViewByID for every element in our layout file here
    private void initViews() {

        map = findViewById(R.id.map);

        fab = findViewById(R.id.location_fab);

        navigationToolbar = findViewById(R.id.navigation_toolbar);
        etOriginSearchLocation = navigationToolbar.findViewById(R.id.et_origin_search_location);
        etDestinationSearchLocation = navigationToolbar.findViewById(R.id.et_destination_search_location);
        btnSendToInsertTrip = navigationToolbar.findViewById(R.id.btn_send_to_insert_trip);
        btnDirection = navigationToolbar.findViewById(R.id.btn_direction);
        setSupportActionBar(navigationToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchLocationRecycler = findViewById(R.id.search_location_recycler);
        items = new ArrayList<>();
        adapter = new RecyclerSearchLocationAdapter(items,NavigationActivity.this);
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(NavigationActivity.this);
        searchLocationRecycler.setLayoutManager(regLayoutManager);
        searchLocationRecycler.setItemAnimator(new DefaultItemAnimator());
        searchLocationRecycler.setHasFixedSize(true);
        searchLocationRecycler.setAdapter(adapter);
        searchLocationRecycler.setVisibility(View.GONE);

    }

    // Initializing map
    private void initMap() {
        // Creating a VectorElementLayer(called markerLayer) to add all markers to it and adding it to map's layers
        //userMarkerLayer = NeshanServices.createVectorElementLayer();
        markerLayer = NeshanServices.createVectorElementLayer();
        lineLayer = NeshanServices.createVectorElementLayer();
        map.getLayers().add(markerLayer);
        map.getLayers().add(lineLayer);
        //map.getLayers().add(userMarkerLayer);

        // add Standard_day map to layer BASE_MAP_INDEX
        map.getOptions().setZoomRange(new Range(4.5f,18f));
        map.getLayers().insert(BASE_MAP_INDEX,NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

        // Setting map focal position to a fixed position and setting camera zoom
        map.setFocalPointPosition(new LngLat(51.330743, 35.767234),0);
        map.setZoom(14,0);
    }

    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                userLocation = locationResult.getLastLocation();
                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                onLocationChange();
            }
        };

        mRequestingLocationUpdates = false;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdate() {
        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG,"All location settings are satisfied.");

                        //noinspection MissingPermission
                        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());

                        onLocationChange();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(NavigationActivity.this, REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(NavigationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        onLocationChange();
                    }
                });
    }

    public void stopLocationUpdate() {
        // Removing location updates
        fusedLocationClient
                .removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void startReceivingLocationUpdates() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdate();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // This method gets a LngLat as input and adds a marker on that position
    /*private void addUserMarker(LngLat loc) {
        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(48f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker)));
        MarkerStyle markSt = markStCr.buildStyle();

        // Creating user marker
        Marker marker = new Marker(loc, markSt);

        // Clearing userMarkerLayer
        userMarkerLayer.clear();

        // Adding user marker to userMarkerLayer, or showing marker on map!
        userMarkerLayer.add(marker);
    }*/

    private void onLocationChange() {
        if (userLocation != null) {
            addMarker(new LngLat(userLocation.getLongitude(),userLocation.getLatitude()),30f);
        }
    }

    public void focusOnUserLocation(View view) {
        if(userLocation != null) {
            //userMarkerLayer.clear();
            markerLayer.clear();
            map.setFocalPointPosition(
                    new LngLat(userLocation.getLongitude(), userLocation.getLatitude()), 0.25f);
            map.setZoom(20, 0.25f);
        }
    }

    private void doSearch(String term) {
        final double lat = map.getFocalPointPosition().getY();
        final double lng = map.getFocalPointPosition().getX();
        final String requestURL = "https://api.neshan.org/v1/search?term=" + term + "&lat=" + lat + "&lng=" + lng;

        GetDataService api = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NeshanSearch> call = api.getNeshanSearch(requestURL);

        call.enqueue(new Callback<NeshanSearch>() {
            @Override
            public void onResponse(Call<NeshanSearch> call, Response<NeshanSearch> response) {
                if (response.isSuccessful()) {
                    NeshanSearch neshanSearch = response.body();
                    items = neshanSearch.getItems();
                    adapter.updateList(items);
                } else {
                    Log.i(TAG, "onResponse: " + response.code() + " " + response.message());
                    Toast.makeText(NavigationActivity.this, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NeshanSearch> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(NavigationActivity.this, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void direction() {

        final String requestURL = "https://api.neshan.org/v2/direction?origin="+ originLng + "," + originLat + "&destination="+ destinationLng + "," + destinationLat;

        GetDataService api = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NeshanDirection> call = api.getNeshanDirection(requestURL);

        call.enqueue(new Callback<NeshanDirection>() {
            @Override
            public void onResponse(Call<NeshanDirection> call, Response<NeshanDirection> response) {
                if (response.isSuccessful()) {
                    NeshanDirection neshanDirection = response.body();
                    routes = neshanDirection.getRoutes();
                }
                else {
                    Log.i(TAG, "onDirectionResponse: " + response.code() + " " + response.message());
                    Toast.makeText(NavigationActivity.this, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NeshanDirection> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(NavigationActivity.this, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMarker(LngLat lngLat, float size) {
        Marker marker = new Marker(lngLat, getMarkerStyle(size));
        markerLayer.add(marker);
    }

    private MarkerStyle getMarkerStyle(float size) {
        MarkerStyleCreator styleCreator = new MarkerStyleCreator();
        styleCreator.setSize(size);
        styleCreator.setBitmap
                (BitmapUtils.createBitmapFromAndroidBitmap
                        (BitmapFactory.decodeResource(getResources(), R.drawable.marker)));
        return styleCreator.buildStyle();
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onSearchItemClick(double lat, double lng, String title) {
        closeKeyBoard();

        if (this.getCurrentFocus().getId() == etOriginSearchLocation.getId()) {
            etOriginSearchLocation.setText(title);
            originLng = lng;
            originLat = lat;
        } else {
            etDestinationSearchLocation.setText(title);
            destinationLng = lng;
            destinationLat = lat;
        }
        searchLocationRecycler.setVisibility(View.GONE);

        markerLayer.clear();
        adapter.updateList(new ArrayList<Item>());
        LngLat lngLat = new LngLat(lat,lng);
        map.setFocalPointPosition(lngLat, 0);
        map.setZoom(10f, 0);
        addMarker(lngLat, 30f);
        direction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public void btnSendToInsertTripOnclick(View v) {

        String origin = etOriginSearchLocation.getText().toString();
        String destination = etDestinationSearchLocation.getText().toString();
        /*Bundle sendData = new Bundle();
        sendData.putString("origin",origin);
        sendData.putString("destination",destination);
        Intent mainActivity = new Intent(NavigationActivity.this,MainActivity.class);
        mainActivity.putExtras(sendData);
        onBackPressed();
        this.finish();*/
        Intent intent = new Intent();
        intent.putExtra("origin",origin);
        intent.putExtra("destination",destination);
        setResult(RESULT_OK,intent);
        finish();

    }

    public void brnDirectionOnclick(View v){
        direction();
        for (int i=0; i<routes.size(); i++) {
            List<Leg> leg ;
            leg = routes.get(i).getLegs();
            for (int j=0; j<leg.size(); j++) {
                List<Step> steps ;
                steps = leg.get(i).getSteps();
                for (int l=0; l<steps.size(); l++) {
                    Toast.makeText(getApplicationContext(),steps.get(i).getPolyline(),Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void getTripPath() {
        Bundle get = getIntent().getExtras();
        int tId = get.getInt("tripId");

        GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
        Call<TripsModel> call = api.getTripPath(tId);
        call.enqueue(new Callback<TripsModel>() {
            @Override
            public void onResponse(Call<TripsModel> call, Response<TripsModel> response) {
                if (response.isSuccessful()) {
                    modelll = response.body();
                    pathTrip = modelll.getPath();
                    decodedStepByStepPath = new ArrayList<>();
                    String[] path = pathTrip.split("PumkiN");
                    for (String s :path) {
                        Log.i("ramin",s);
                        //List<PolylineEncoding.LatLng> decodedEachStep = PolylineEncoding.decode(s);
                       // decodedStepByStepPath.addAll(decodedEachStep);

                    }
                    //drawLineGeom(decodedStepByStepPath);
                }
            }

            @Override
            public void onFailure(Call<TripsModel> call, Throwable t) {

            }
        });
    }

    private void mapSetPosition(boolean overview) {
        double centerFirstMarkerX = originLat;
        double centerFirstMarkerY = originLng;
        if (overview) {
            double centerFocalPositionX = (centerFirstMarkerX) / 2;
            double centerFocalPositionY = (centerFirstMarkerY ) / 2;
            map.setFocalPointPosition(new LngLat(centerFocalPositionX, centerFocalPositionY),0.5f );
            map.setZoom(14,0.5f);
        } else {

            double minLat = Double.MAX_VALUE;
            double minLng = Double.MAX_VALUE;
            double maxLat = Double.MIN_VALUE;
            double maxLng = Double.MIN_VALUE;
            minLat = Math.min(originLng, minLat);
            minLng = Math.min(originLat, minLng);
            maxLat = Math.max(destinationLng, maxLat);
            maxLng = Math.max(destinationLat, maxLng);
            //map.setFocalPointPosition(new LngLat(centerFirstMarkerX, centerFirstMarkerY),0.5f );
            map.moveToCameraBounds(new Bounds(new LngLat(modelll.getOriginMinLng(), modelll.getOriginMinLat()), new LngLat(modelll.getDestinationMaxLng(), modelll.getDestinationMaxLat())),new ViewportBounds(new ViewportPosition(0, 0), new ViewportPosition(map.getWidth(), map.getHeight())),true, 0.5f);
            // map.setZoom(8,0.5f);
            markerLayer.clear();
            //destinationMarkerLayer.clear();
            addMarker(new LngLat(originLat,originLng),50f);
            addMarker(new LngLat(destinationLat,destinationLng),50f);
        }

    }

    public LineGeom drawLineGeom(List<PolylineEncoding.LatLng> paths) {
        // we clear every line that is currently on map
        lineLayer.clear();
        // Adding some LngLat points to a LngLatVector
        LngLatVector lngLatVector = new LngLatVector();
        for (PolylineEncoding.LatLng path : paths) {
            lngLatVector.add(new LngLat(path.lng, path.lat));
        }

        // Creating a lineGeom from LngLatVector
        LineGeom lineGeom = new LineGeom(lngLatVector);
        // Creating a line from LineGeom. here we use getLineStyle() method to define line styles
        Line line = new Line(lineGeom, getLineStyle());
        // adding the created line to lineLayer, showing it on map
        lineLayer.add(line);
        // focusing camera on first point of drawn line
        mapSetPosition(overview);
        return lineGeom;
    }

    private LineStyle getLineStyle(){
        LineStyleCreator lineStCr = new LineStyleCreator();
        lineStCr.setColor(new ARGB((short) 2, (short) 119, (short) 189, (short)190));
        lineStCr.setWidth(10f);
        lineStCr.setStretchFactor(0f);
        return lineStCr.buildStyle();
    }
}
