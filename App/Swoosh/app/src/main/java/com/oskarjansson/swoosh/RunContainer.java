package com.oskarjansson.swoosh;

import android.location.Location;
import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by oskja067 on 2016-11-17.
 */

public class RunContainer {

    private List<RunPoint> route;
    private Date date;
    private String runId;
    private int xp;
    private double length;

    public RunContainer() {
        this.route = null;
        this.date = null;
        this.runId = null;
        this.xp = -1;
        this.length = -1;
    }

    public RunContainer(String runId, List<RunPoint> l) {
        this.route = l;
        this.runId = runId;
        this.date = new Date( l.get(0).getTime() );
        this.length = calculateLength();
        this.xp = calculateXP();
    }

    private int calculateXP() {
        int xpGained = 0;

        xpGained += (int) length / 100 ;

        return xpGained;
    }

    public double calculateLength() {
        double sum = 0;
        for (int i = 0; i< route.size()-1; i++) {
            RunPoint p1 = route.get(i);
            RunPoint p2 = route.get(i+1);
            double d = getDistance(p1,p2);
            sum += d;
        }
        Log.d("RunContainer","Calculated length to: " + sum + " for " + route.size() + " RunPoints");
        return sum;
    }

    private double getDistance(RunPoint p1, RunPoint p2) {
        // Haversine Formula, bitches!
        int r = 6378137; // Earth's mean radius in metres
        double dLat = Math.toRadians(p2.getLat() - p1.getLat());
        double dLong = Math.toRadians(p2.getLng() - p1.getLng());

        double a;
        a = (Math.sin(dLat / 2) * Math.sin(dLat / 2))
                + (Math.cos(Math.toRadians(p1.getLat()))
                * Math.cos(Math.toRadians(p2.getLat()))
                * Math.sin(dLong / 2) * Math.sin(dLong / 2));
        double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt( 1 - a )  );
        double d = r * c;

        return d;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<RunPoint> getRoute() {
        return route;
    }

    public void setRoute(List<RunPoint> route) {
        this.route = route;
        this.length = calculateLength();
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public void setDateFromLocation(Location l){
            this.date = new Date(l.getTime() );
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "[Run: " + this.runId + " Routesize: " + route.size() + " Date: " + date.toString() + "]";
    }
}
