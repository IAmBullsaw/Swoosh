package com.oskarjansson.swoosh;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by oskja067 on 2016-11-29.
 */

public class RunPoint {

    private double lat;
    private double lng;
    private double altitude;
    private float speed;
    private long time;
    private long realTime;

    public RunPoint () {
        this.altitude = -1;
        this.lat = -1;
        this.lng = -1;
        this.realTime = -1;
        this.speed = -1;
        this.time = -1;
    }

    public RunPoint(double altitude, double lat, double lng, long realTime, float speed, long time) {
        this.altitude = altitude;
        this.lat = lat;
        this.lng = lng;
        this.realTime = realTime;
        this.speed = speed;
        this.time = time;
    }

    public RunPoint(Location l ) {
        this.lat = l.getLatitude();
        this.lng = l.getLongitude();
        this.altitude = l.getAltitude();
        this.speed = l.getSpeed();
        this.time = l.getTime();
        this.realTime = l.getElapsedRealtimeNanos();
    }

    public long getRealTime() {
        return realTime;
    }

    public void setRealTime(long realTime) {
        this.realTime = realTime;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public LatLng toLatLng() {
        return new LatLng(this.lat,this.lng);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return String.format(Locale.UK,"[RunPoint (%f:%f) ]",lat,lng);
    }


}
