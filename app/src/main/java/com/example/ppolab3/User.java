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
        PlayerStat.username = username;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        PlayerStat.avatarPath = imageUrl;
    }
    public String getReady(){return ready;}
    public void setReady(String ready) {
        this.ready = ready;
    }
}
