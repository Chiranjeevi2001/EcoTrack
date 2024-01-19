package com.example.ecotrack_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Address;

import android.location.Location;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ecotrack_v1.databinding.ActivityMapsReportBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsReportActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FloatingActionButton accept;
    GPSTracker gps;
    double latitude, longitude;
    String place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_report);

        accept = findViewById(R.id.btn_accept_location);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnModifiedLocation(latitude, longitude);
            }
        });

    }
    private void returnModifiedLocation(double modifiedLatitude, double modifiedLongitude) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("MODIFIED_LATITUDE", modifiedLatitude);
        resultIntent.putExtra("MODIFIED_LONGITUDE", modifiedLongitude);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(getBaseContext(), "Location Fetched", Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    try {
                        getPlaceName(latitude, longitude);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    mapFragment.getMapAsync(MapsReportActivity.this);

                }
            }
        });
    }

    private void getPlaceName(double latitude, double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        place = addresses.get(0).getAddressLine(0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else {
                Toast.makeText(MapsReportActivity.this,"Location Permission is denied", Toast.LENGTH_SHORT).show();
            }
        }


    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                LatLng updatedLatLng = marker.getPosition();
                latitude = updatedLatLng.latitude;
                longitude = updatedLatLng.longitude;
                try {
                    getPlaceName(latitude,longitude);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(place).draggable(true);
        mMap.addMarker(markerOptions).setDraggable(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
        mMap.addCircle(new CircleOptions().center(latLng)
                .radius(500)
                .fillColor(Color.TRANSPARENT)
                .strokeColor(Color.DKGRAY));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }
}