package com.example.ecotrack_v1;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ReportModel {

    String reportedUser;
    List<String> trashType = new ArrayList<>();
    String trashSize;
    boolean isCleaned;
    String imageUri;
    String place;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ReportModel() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    double longitude;
    double latitude;

    public ReportModel(String reportedUser, String trashSize, double longitude, double latitude, boolean isCleaned)
    {
        this.reportedUser = reportedUser;
        this.trashSize = trashSize;
        trashType = new ArrayList<>();
        this.latitude = latitude;
        this.longitude = longitude;
        this.isCleaned = isCleaned;
    }

    public void setReportedUser(String reportedUser)
    {
        this.reportedUser = reportedUser;
    }

    public String getReportedUser()
    {
        return this.reportedUser;
    }
    public void setTrashSize(String trashSize)
    {
        this.trashSize = trashSize;
    }
    public String getTrashSize()
    {
        return this.trashSize;
    }
    public void addTrashType(String type)
    {
        this.trashType.add(type);
    }
    public void removeTrashType(String type)
    {
        int index = this.trashType.indexOf(type);
        if(index != -1) {
            this.trashType.remove(index);
        }
    }

    public List<String> getTrashType()
    {
        return this.trashType;
    }
    public void setImageUri(String imageUri)
    {
        this.imageUri = imageUri;
    }
    public Uri getImageUri()
    {
        return Uri.parse(this.imageUri);
    }

    public void setIsCleaned(boolean isCleaned)
    {
        this.isCleaned = isCleaned;
    }
    public boolean getIsCleaned()
    {
        return this.isCleaned;
    }

}
