package com.bouvet.sandvika.myfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bouvet.sandvika.myfriends.services.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static String REGISTER_USER_URL = "http://192.168.1.124:8080/user";
    private final static String REGISTER_LOCATION_URL = "http://192.168.1.124:8080/user/kris/updatePosition";

    private GoogleApiClient googleApiClient;
    private RequestQueue requestQueue;
    private BroadcastReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        requestQueue = Volley.newRequestQueue(this);

        createUser();

        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("onreceive");

                createUser();
            }
        };

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

    }

    private void createUser() {

        JsonObjectRequest createUserRequest = new JsonObjectRequest(Method.POST, REGISTER_USER_URL, getUserAsJSON(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("ok");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("failed");
            }
        });

        requestQueue.add(createUserRequest);

    }

    private JSONObject getUserAsJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", "kris");
            jsonObject.put("firstName", "kristoffer");
            jsonObject.put("lastName", "mysen");
            jsonObject.put("regId", "982");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("MESSAGE"));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            System.out.println("hmm");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        JsonObjectRequest postLocationRequest = new JsonObjectRequest(Method.POST, REGISTER_LOCATION_URL, getLocationAsJSON(location), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("something");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("something else");
                error.printStackTrace();
            }
        });

        requestQueue.add(postLocationRequest);

    }

    private String getLocationAsJSON(Location location) {
        // Her burde man bruke et JSON bibliotek!
        return "{\n" +
                "\"location\":\"" + location.getLatitude() + "," + location.getLongitude() + "\"\n" +
                "}";

    }



}
