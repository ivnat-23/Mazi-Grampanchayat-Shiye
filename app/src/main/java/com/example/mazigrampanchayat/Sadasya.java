package com.example.mazigrampanchayat;

public class Sadasya {
    private String name;
    private String number;
    private String photoUrl;

    public Sadasya() {
    }

    public Sadasya(String name, String number, String photoUrl) {
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
