package com.example.mazigrampanchayat;

public class Finance {
    private String transactionId;
    private String description;
    private double amount;
    private String date;

    public Finance() {
    }

    public Finance(String transactionId, String description, double amount, String date) {
        this.transactionId = transactionId;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
