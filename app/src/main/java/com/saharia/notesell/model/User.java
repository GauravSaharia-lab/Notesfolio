package com.saharia.notesell.model;

public class User {

    private String uid, name,location,profileImage;

    public User() {
    }

    public User(String uid, String name, String location, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.location = location;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
