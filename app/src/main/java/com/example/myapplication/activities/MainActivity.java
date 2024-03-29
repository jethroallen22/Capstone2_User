package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;


import com.example.myapplication.models.CoordModel;
import com.example.myapplication.models.WeatherModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String ONESIGNAL_APP_ID = "941829df-43af-4a89-b2b3-67f4db6e572a";
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    final static int PERMISSIONS_ALL = 1;
    LocationManager locationManager;
    double curLong;
    double curLat;
    WeatherModel weatherModel;
    CoordModel coordModel;
    private String weather;
    Location location;
    private final String weatherURL = "https://api.openweathermap.org/data/2.5/weather";
    private final String appId = "d7484fc39538bf509fe729a4bbb0a90f";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(PERMISSIONS, PERMISSIONS_ALL);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        curLat = location.getLatitude();
        curLong = location.getLongitude();
        requestWeather();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestLocation();
                    handler2.postDelayed(this,1000);
                }
            },1000);

        }
    }
    @SuppressLint("ServiceCast")
    public void requestLocation(){
        if (locationManager == null){
            locationManager = (LocationManager) getSystemService(LOCALE_SERVICE);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1000, this);
            }
        }
    }

    public void requestWeather(){
        String tempWeatherURL = weatherURL + "?lat=" + curLat + "&lon=" + curLong + "&appid=" + appId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempWeatherURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("weather", response);
                    Gson gson = new Gson();
                    String jsonString = jsonObject.getString("main");
                    String jsonCoord = jsonObject.getString("coord");
                    weatherModel = gson.fromJson(jsonString, WeatherModel.class);
                    coordModel = gson.fromJson(jsonCoord, CoordModel.class);
                    Log.d("weather", String.valueOf(weatherModel.getTemp()));
                    weather(weatherModel);
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("weather",weather);
                    intent.putExtra("lat",coordModel.getLat());
                    intent.putExtra("long",coordModel.getLon());
                    Log.d("weatherLat", String.valueOf(coordModel.getLat()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(intent);
                }catch (JSONException e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void weather(WeatherModel weatherModel){
        float celsius = 0.0F;
        celsius = convertToCelsius(weatherModel.getTemp());
        Log.d("celsius", String.valueOf(celsius));
        if(celsius > 26.6)
            this.weather = "hot";
        else if (celsius < 26.6)
            this.weather = "cold";
        Log.d("weather", weather);
    }

    public float convertToCelsius(Double temp){
        float celsius = 0.0F;
        double kelvin = 0.0F;
        kelvin = temp;
        celsius = (float) (kelvin - 273.15);
        Log.d("celsius", String.valueOf(celsius));
        return celsius;
    }
}