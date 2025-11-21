package com.example.mazigrampanchayat;
public class User {
    private String name;
    private String birthDate;
    private String mobileNo;
    private String password;

    public User() {
    }

    public User(String name, String birthDate, String mobileNo, String password) {
        this.name = name;
        this.birthDate = birthDate;
        this.mobileNo = mobileNo;
        this.password = password;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
