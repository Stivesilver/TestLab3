package com.example.ppolab3;

import java.util.UUID;

public class Game {
    public static int LineWidth = 10;
    public static User userFirst;
    public static User userSecond;
    public static String Id;
    public static boolean isReadyMe, isReadySecond;
    public static String userPath, myUserPath;
    public static boolean[][] fieldMy = new boolean[Game.LineWidth][Game.LineWidth];
    public static boolean[][] fieldEnemy = new boolean[Game.LineWidth][Game.LineWidth];
    public static UserEnum myUser;
    public static String myUsername;


    Game() {

    }

    public Game(User user) {
        userFirst = user;
        Id = String.valueOf((int)(Math.random()*1000));
    }

    public Game(String id, User userF, User userS) {
        Id = id;
        userFirst = userF;
        userSecond = userS;
    }

    public void Connect(User user) {
        userSecond = user;
    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public User getUserFirst() {
        return userFirst;
    }

    public User getUserSecond() {
        return userSecond;
    }

    public void setUserFirst(User userFirst) {
        Game.userFirst = userFirst;
    }

    public void setUserSecond(User userSecond) {
        Game.userSecond = userSecond;
    }

}
