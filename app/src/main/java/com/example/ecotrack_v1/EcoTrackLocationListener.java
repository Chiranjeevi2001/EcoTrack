package com.example.ecotrack_v1;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class EcoTrackLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
