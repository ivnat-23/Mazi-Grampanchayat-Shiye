package com.example.mazigrampanchayat;

public class News {
    private String headline;
    private String imageUrl;
    private String url;

    public News() {
    }

    public News(String headline, String imageUrl, String url) {
        this.headline = headline;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
