package com.example.ecotrack_v1.ui.login;

import java.util.ArrayList;
import java.util.List;

public class ReportModel {

    private String reportedUser;
    private List<String> trashType;
    private String trashSize;


    public ReportModel()
    {
        this.trashSize = "small";
        trashType = new ArrayList<>();

    }
    public ReportModel(String reportedUser, String trashSize)
    {
        this.reportedUser = reportedUser;
        this.trashSize = trashSize;
        trashType = new ArrayList<>();
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

}
