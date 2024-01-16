package com.example.ecotrack_v1;


import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;

    double longitude;
    double latitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1; //1/2 min updates

    protected LocationManager locationManager;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                //Do Nothing
            } else {

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.v("Network", "Network Enabling");

                    if(locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }
                    if(location != null)
                    {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        this.canGetLocation = true;
                    }
                }
            }
            if(isGPSEnabled)
            {
                if(location == null)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if(locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                    if(location != null)
                    {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }
    public double getLatitude() {
        if(location!=null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }
    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Location Settings for AppDemo");

        alertDialog.setMessage("GPS is not enabled on your device. Please turn it on.");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(it);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onStatusChanged(@NonNull String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
}

