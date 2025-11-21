package com.example.mazigrampanchayat;

public class Sarpanch {
    private String name;
    private String number;
    private String photoUrl;

    public Sarpanch() {
    }

    public Sarpanch(String name, String number, String photoUrl) {
        this.name = name;
        this.number = number;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
