package com.example.mazigrampanchayat;

/**
 * Model class for Notice Board notices
 * Stores notice information including title, image, date, and priority
 */
public class Notice {
    private String title;
    private String imageBase64;
    private String date;
    private String priority; // "high", "medium", "low"
    private Boolean isActive; // Changed to Boolean to handle null values from Firebase
    private String noticeId;

    public Notice() {
        // Default constructor required for Firebase
    }

    public Notice(String title, String imageBase64, String date, String priority, boolean isActive) {
        this.title = title;
        this.imageBase64 = imageBase64;
        this.date = date;
        this.priority = priority;
        this.isActive = isActive; // boolean will be auto-boxed to Boolean
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isActive() {
        // Return true by default if isActive is null (for backward compatibility)
        return isActive != null && isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }
}

