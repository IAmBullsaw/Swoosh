package com.oskarjansson.swoosh;

/**
 * Created by oskja067 on 2016-12-06.
 */

public class SwooshUser {
    private String title;
    private String name;
    private int level;
    private int xp;
    private String uID;

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public SwooshUser() {
        this.level = -1;
        this.title = "null";
        this.xp = -1;
    }

    public SwooshUser(int level, String title, int xp) {
        this.level = level;
        this.title = title;
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
