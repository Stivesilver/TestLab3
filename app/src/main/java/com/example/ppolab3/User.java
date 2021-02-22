package com.example.ppolab3;

public class User {

    String username, imageUrl, ready = "false";
    public User() {
    }
    public User(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        UserHelper.username = username;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        UserHelper.imageUrl = imageUrl;
    }
    public String getReady(){return ready;}
    public void setReady(String ready) {
        this.ready = ready;
    }
}
