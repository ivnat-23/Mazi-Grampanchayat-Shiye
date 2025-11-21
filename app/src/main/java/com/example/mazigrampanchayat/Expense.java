package com.example.mazigrampanchayat;

public class Expense {
    private String उद्दिष्ट;
    private String रक्कम;
    private String दिनांक;

    public Expense() {
    }

    public Expense(String उद्दिष्ट, String रक्कम, String दिनांक) {
        this.उद्दिष्ट = उद्दिष्ट;
        this.रक्कम = रक्कम;
        this.दिनांक = दिनांक;
    }

    public String getउद्दिष्ट() {
        return उद्दिष्ट;
    }

    public String getरक्कम() {
        return रक्कम;
    }

    public String getदिनांक() {
        return दिनांक;
    }
}
