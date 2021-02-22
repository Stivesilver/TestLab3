package com.example.ppolab3;

public class UserHelper {
    static String username, email, password, victories, imageUrl;
    public UserHelper() {
    }
    public UserHelper(String username, String email, String password) {
        UserHelper.username = username;
        UserHelper.email = email;
        UserHelper.password = password;
        UserHelper.victories = "0";
        UserHelper.imageUrl = "";
    }
    public UserHelper(String username, String email, String password, String victories, String imageUrl) {
        UserHelper.username = username;
        UserHelper.email = email;
        UserHelper.password = password;
        UserHelper.victories = victories;
        UserHelper.imageUrl = imageUrl;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        UserHelper.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        UserHelper.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        UserHelper.password = password;
    }
    public String getVictories() {
        return victories;
    }
    public void setVictories(String victories) {
        UserHelper.victories = victories;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        UserHelper.imageUrl = imageUrl;
    }
}
