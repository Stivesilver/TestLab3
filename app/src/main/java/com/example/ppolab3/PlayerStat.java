package com.example.ppolab3;

public class PlayerStat{
    static String username, email, passwd, victory, defeat, avatarPath;

    PlayerStat(String username, String email, String passwd, String victory, String defeat, String avatarPath){
        PlayerStat.username = username;
        PlayerStat.email = email;
        PlayerStat.passwd = passwd;
        PlayerStat.victory = victory;
        PlayerStat.defeat = defeat;
        PlayerStat.avatarPath = avatarPath;
    }

    PlayerStat(String username, String passwd){
        PlayerStat.username = username;
        PlayerStat.passwd = passwd;
    }
    public String getUsername(){
        return PlayerStat.username;
    }
    public String getEmail(){
        return PlayerStat.email;
    }
    public String getPasswd(){
        return PlayerStat.passwd;
    }
    public String getVictory(){
        return PlayerStat.victory;
    }
    public String getDefeat(){
        return PlayerStat.defeat;
    }
    public String getAvatarPath(){
        return PlayerStat.avatarPath;
    }

    public void setUsername(){
        PlayerStat.username = username;
    }
    public void setEmail(){
        PlayerStat.email = email;
    }
    public void setPasswd(){
        PlayerStat.passwd = passwd;
    }
    public void setVictory(){
        PlayerStat.victory = victory;
    }
    public void setDefeat(){
        PlayerStat.defeat = defeat;
    }
    public void setAvatarPath(){
        PlayerStat.avatarPath = avatarPath;
    }

}