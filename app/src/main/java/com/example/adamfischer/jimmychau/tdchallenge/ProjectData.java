package com.example.adamfischer.jimmychau.tdchallenge;

import java.io.Serializable;

/**
 * Class to represent a project from the DB
 */
public class ProjectData implements Serializable {
    private long ID;
    private long userID;
    private String name;
    private String type;
    private String blurb;
    private String date;
    private long goal;
    private long donated;

    public ProjectData(long ID, long userID, String name, String type, String blurb, String date, long goal, long donated) {
        this.ID = ID;
        this.userID = userID;
        this.name = name;
        this.type = type;
        this.blurb = blurb;
        this.date = date;
        this.goal = goal;
        this.donated = donated;
    }

    public long getID() {
        return ID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public long getDonated() {
        return donated;
    }

    public void setDonated(long goal) {
        this.donated = donated;
    }

    @Override
    public String toString() {
        return name;
    }
}
