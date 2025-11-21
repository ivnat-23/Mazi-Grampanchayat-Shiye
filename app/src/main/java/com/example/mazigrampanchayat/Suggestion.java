package com.example.mazigrampanchayat;

/**
 * Model class for user suggestions/feedback
 * Stores suggestion information including category, text, status, and admin response
 */
public class Suggestion {
    private String userId;
    private String userName;
    private String mobileNumber;
    private String category;
    private String suggestion;
    private String submissionDate;
    private String submissionTime;
    private String status; // "pending", "reviewed", "implemented"
    private String adminResponse;
    private String suggestionId;

    public Suggestion() {
        // Default constructor required for Firebase
    }

    public Suggestion(String userId, String userName, String mobileNumber, String category,
                     String suggestion, String submissionDate, String submissionTime) {
        this.userId = userId;
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.category = category;
        this.suggestion = suggestion;
        this.submissionDate = submissionDate;
        this.submissionTime = submissionTime;
        this.status = "pending";
        this.adminResponse = "";
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public String getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        this.suggestionId = suggestionId;
    }
}

