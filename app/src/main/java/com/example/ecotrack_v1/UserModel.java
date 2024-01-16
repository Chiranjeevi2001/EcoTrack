package com.example.ecotrack_v1;

public class UserModel {
    private String Email;
    private String FullName;
    private String ProfileType;
    private int greenPoints;


    public UserModel() {
    }

    public UserModel(String Email, String FullName, String ProfileType, int greenPoints) {
        this.Email = Email;
        this.FullName = FullName;
        this.ProfileType = ProfileType;
        this.greenPoints = greenPoints;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
    public String getProfileType() {
        return ProfileType;
    }

    public void setProfileType(String profileType) {
        this.ProfileType = profileType;
    }
    public int getGreenPoints(){
        return greenPoints;
    }
    public void setGreenPoints(int greenPoints)
    {
        this.greenPoints = greenPoints;
    }

    public String getFullname() {
        return FullName;
    }

    public void setFullname(String fullname) {
        this.FullName = fullname;
    }

}
