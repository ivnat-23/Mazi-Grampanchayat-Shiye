package com.example.mazigrampanchayat;

public class Income {
    private String स्रोत;
    private String रक्कम;
    private String दिनांक;

    public Income() {
    }

    public Income(String स्रोत, String रक्कम, String दिनांक) {
        this.स्रोत = स्रोत;
        this.रक्कम = रक्कम;
        this.दिनांक = दिनांक;
    }

    public String getस्रोत() {
        return स्रोत;
    }

    public String getरक्कम() {
        return रक्कम;
    }

    public String getदिनांक() {
        return दिनांक;
    }
}
