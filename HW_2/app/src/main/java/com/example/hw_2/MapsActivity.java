package com.example.hw_2;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapLongClickListener,
        SensorEventListener {

    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 101;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    Marker gpsMarker = null;
    List<Marker> markerList;
    List<String>positionList;
    private final String DATA_JSON_FILE = "listJson.json";
    private TextView  accelerometer_info;
    static public SensorManager mSensorManager;
    Sensor accelerometer;
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ( )
                .findFragmentById (R.id.map);
        mapFragment.getMapAsync (this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient (this);
        markerList=new ArrayList<> ();
        positionList=new ArrayList<> ();
        mSensorManager = (SensorManager) getSystemService (Context.SENSOR_SERVICE);
        accelerometer=mSensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener (MapsActivity.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        accelerometer_info=(TextView) findViewById (R.id.acceleration_text);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        restoreDataLogsFromJson();
    }

    private void saveDataLogsToJson(){
        Gson gson = new Gson();
        String listJson = gson.toJson(positionList);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(DATA_JSON_FILE,MODE_PRIVATE);
            FileWriter writer = new FileWriter(outputStream.getFD());
            writer.write(listJson);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMarkers(String position){
        positionList.add(position);
        double x = Double.parseDouble(position.substring(0,position.indexOf(",")));
        double y = Double.parseDouble(position.substring(position.indexOf(",")+1));

        Marker marker= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(x,y))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .alpha(0.8f)
                .title(String.format("Position: (%.2f,%.2f)",x,y)));
        markerList.add(marker);
    }

    private void restoreDataLogsFromJson() {
        FileInputStream inputStream;
        int DEFAULT_BUFFER_SIZE=100000;
        Gson gson=new Gson();
        String readJson;
        try {
            inputStream = openFileInput(DATA_JSON_FILE);
            FileReader reader = new FileReader(inputStream.getFD());
            char[] buf = new char[DEFAULT_BUFFER_SIZE];
            int n;
            StringBuilder builder = new StringBuilder();
            while ((n = reader.read(buf))>=0) {
                String tmp = String.valueOf(buf);
                String substring = (n<DEFAULT_BUFFER_SIZE) ? tmp.substring(0,n) : tmp;
                builder.append(substring);
            }
            reader.close();
            readJson = builder.toString();
            Type collectionType = new TypeToken<List<String>>() {}.getType();
            List<String> o = gson.fromJson(readJson,collectionType);
            if (o!=null) {
                markerList.clear();
                positionList.clear();
                for(String position: o) {
               addMarkers (position);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates()
    {
        fusedLocationClient.requestLocationUpdates(mLocationRequest,locationCallback,null);
    }

    private void createLocationCallback()
    {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult != null)
                {
                    if(gpsMarker != null)
                    {
                        gpsMarker.remove();
                        Location location = locationResult.getLastLocation();
                        gpsMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(),location.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                .alpha(0.8f)
                                .title("Current Location"));
                    }
                }
            }
        };
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates()
    {
        if(locationCallback != null)
        {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public void zoomInClick(View v)
    {
        mMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    protected void onDestroy() {
        saveDataLogsToJson();
        super.onDestroy();
    }

    @Override
    public void onMapLoaded() {
        Log.i(MapsActivity.class.getSimpleName(),"MapLoaded");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        Task<Location> lastLocation = fusedLocationClient.getLastLocation();
        lastLocation.addOnSuccessListener(this, new OnSuccessListener<Location> () {
            @Override
            public void onSuccess(Location location) {
                if(location != null && mMap != null)
                {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title("last_known_loc_msg"));
                }
            }
        });
        createLocationRequest();
        createLocationCallback();
        startLocationUpdates();
    }

    @Override
    public void onMapLongClick(LatLng latLng) { //LatLng latLng
        if(markerList.size() > 0) {
            Marker lastMarker = markerList.get(markerList.size() - 1);
        }
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude,latLng.longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .alpha(0.8f)
                .title(String.format("Position: (%.2f, %.2f) ",latLng.latitude,latLng.longitude)));
        markerList.add(marker);
        positionList.add(Double.toString(latLng.latitude) + "," + Double.toString(latLng.longitude));
        saveDataLogsToJson();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraPosition cameraPos = mMap.getCameraPosition();
        if(cameraPos.zoom <14f)
            mMap.moveCamera(CameraUpdateFactory.zoomOut ());

        ImageButton getting_button = findViewById(R.id.getting_button);
        ImageButton hide_button = findViewById(R.id.hide_button);

        hide_button.setVisibility(View.VISIBLE);
        getting_button.setVisibility(View.VISIBLE);

        ObjectAnimator getting_animator;
        ObjectAnimator fade_getting_animator;
        ObjectAnimator hide_animator;
        ObjectAnimator hide_fade_animator;

        //---------------------------------------------button animation functionality - SLIDE UP------------------------------------------------------------------------------------
        getting_animator = ObjectAnimator.ofFloat (getting_button, "translationY", getting_button.getHeight ( ), 0 - (getting_button.getHeight ( )) / 2);
        getting_animator.setDuration (1000);
        fade_getting_animator = ObjectAnimator.ofFloat (getting_button, "alpha", 1.0f);
        fade_getting_animator.setDuration (1000);

        hide_animator = ObjectAnimator.ofFloat (hide_button, "translationY", hide_button.getHeight ( ), 0 - (hide_button.getHeight ( )) / 2);
        hide_animator.setDuration (1000);
        hide_fade_animator = ObjectAnimator.ofFloat (hide_button, "alpha", 1f);
        hide_fade_animator.setDuration (1000);

        AnimatorSet animSet = new AnimatorSet ( );
        animSet.play (getting_animator).with (fade_getting_animator).with (hide_animator).with (hide_fade_animator);
        animSet.start ( );
        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        return false;
    }

    public void getClick(View view) {
        if (flag == false) {
            findViewById (R.id.acceleration_text).setVisibility (View.VISIBLE);
            flag = true;
        }
        else  {
            findViewById (R.id.acceleration_text).setVisibility (View.INVISIBLE);
            flag = false;
        }
    }

    public void hideClick(View view) {

        ImageButton getting_button = findViewById(R.id.getting_button);
        ImageButton hide_button = findViewById(R.id.hide_button);

        hide_button.setVisibility(View.VISIBLE);
        getting_button.setVisibility(View.VISIBLE);

        ObjectAnimator getting_animator;
        ObjectAnimator fade_getting_animator;
        ObjectAnimator hide_animator;
        ObjectAnimator hide_fade_animator;
        //---------------------------------------------button animation functionality - SLIDE DOWN------------------------------------------------------------------------------------
        getting_animator = ObjectAnimator.ofFloat (getting_button, "TranslationY", -getting_button.getHeight ( )/2,-( 0 - (getting_button.getHeight ( )) ));
        getting_animator.setDuration (1000);
        fade_getting_animator = ObjectAnimator.ofFloat (getting_button, "alpha", 0.5f);
        fade_getting_animator.setDuration (2000);

        hide_animator = ObjectAnimator.ofFloat (hide_button, "TranslationY", -hide_button.getHeight ( )/2,-( 0 - (hide_button.getHeight ( )) ));
        hide_animator.setDuration (1000);
        hide_fade_animator = ObjectAnimator.ofFloat (hide_button, "alpha", 0.5f);
        hide_fade_animator.setDuration (2000);

        AnimatorSet animSet = new AnimatorSet ( );
        animSet.play (getting_animator).with (fade_getting_animator).with (hide_animator).with (hide_fade_animator);
        animSet.start ( );
        //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        findViewById (R.id.acceleration_text).setVisibility (View.INVISIBLE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelerometer_info.setTextAlignment (View.TEXT_ALIGNMENT_CENTER);
        accelerometer_info.setText ("Accelerometer \n X: " + sensorEvent.values[0]+ " Y: "+sensorEvent.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void zoomOutClick(View view)
    {
        mMap.moveCamera(CameraUpdateFactory.zoomOut());
    }

    public void clrClick(View view) {
        markerList.clear();
        positionList.clear();
        mMap.clear();
        saveDataLogsToJson();
    }
}