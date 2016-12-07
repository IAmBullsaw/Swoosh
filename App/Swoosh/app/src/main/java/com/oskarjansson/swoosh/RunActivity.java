package com.oskarjansson.swoosh;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RunActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private int MY_PERMISSION_REQUEST_FINE_LOCATION;
    private GoogleMap googleMap;
    private List<Location> locations;
    private List<RunPoint> runPoints;
    private String userName;
    private DatabaseReference userRuns, userData;
    private boolean hasPushedRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locations = new ArrayList<Location>();
        runPoints = new ArrayList<RunPoint>();
        setContentView(R.layout.activity_run);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1)
                .setFastestInterval(1);

        // If we do not have any permission to access maps,
        // we are royally F*cked and should exit all the things...
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_FINE_LOCATION);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userName = user.getUid();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRuns = database.getReference("user/" + userName + "/workouts");
        userData = database.getReference("user/" + userName + "/data/xp");



        ImageButton stopButton = (ImageButton) findViewById(R.id.run_StopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Run", "Pushed stopButton!");
                if (!hasPushedRun) {
                    DatabaseReference run = userRuns.push();
                    RunContainer data = new RunContainer(run.getKey(),runPoints);
                    run.setValue(data);
                    Log.d("Run", "Debug: " + data.getDate().toString());
                    Log.d("Run", "Pushed runPoints to firebase!");
                    hasPushedRun = true;

                    Intent intent = (Intent) new Intent(view.getContext(),MissionCompletedActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Run","onConnected :D");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLocation != null) {

                runPoints.add(new RunPoint(lastLocation));

                // Make Something Else!
                googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 18));
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())));
            }
        }
        else
        {
            Toast.makeText(this,"YOU ARE NOT SUPPOSED TO BE HERE",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this,"WE CHANGED PLACES :D",Toast.LENGTH_SHORT).show();

        runPoints.add(new RunPoint(location));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()) , 18));
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
        }
        else
        {
            Toast.makeText(this,"YOU ARE NOT SUPPOSED TO BE HERE",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("RunActivity","onMapReady - Setting mock location 0,0");
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                getLatLng(), 1));
        googleMap.addMarker(new MarkerOptions()
                .position(getLatLng()));
    }

    private LatLng getLatLng(){
        return new LatLng(0, 0);
    }
}
