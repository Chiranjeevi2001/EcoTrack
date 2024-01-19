package com.example.ecotrack_v1;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ReportModel {

    private String reportedUser;
    private List<String> trashType;
    private String trashSize;
    String imageUri;
    LatLng location;


    public ReportModel()
    {
        this.trashSize = "small";
        trashType = new ArrayList<>();

    }
    public ReportModel(String reportedUser, String trashSize, LatLng location)
    {
        this.reportedUser = reportedUser;
        this.trashSize = trashSize;
        trashType = new ArrayList<>();
        this.location = location;
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

    public void setLocation(LatLng location)
    {
        this.location = location;
    }
    public LatLng getLocation()
    {
        return this.location;
    }

    public void setImageUri(String imageUri)
    {
        this.imageUri = imageUri;
    }
    public Uri getImageUri()
    {
        return Uri.parse(this.imageUri);
    }

}
