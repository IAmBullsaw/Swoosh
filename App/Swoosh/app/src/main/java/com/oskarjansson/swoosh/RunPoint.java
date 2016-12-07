package com.oskarjansson.swoosh;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by oskja067 on 2016-11-29.
 */

public class RunPoint implements Parcelable{

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

    public RunPoint (Parcel parcel) {
        this.speed = parcel.readFloat();
        this.time = parcel.readLong();
        this.realTime = parcel.readLong();
        this.lat = parcel.readDouble();
        this.lng = parcel.readDouble();
        this.altitude = parcel.readDouble();
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

    // Keeping it like this since I am 99% sure I am not using it...
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(speed);
        parcel.writeLong(time);
        parcel.writeLong(realTime);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeDouble(altitude);
    }

    public static final Parcelable.Creator<RunPoint> CREATOR = new Parcelable.Creator<RunPoint>() {
        public RunPoint createFromParcel(Parcel parcel) {
            return new RunPoint(parcel);
        }

        public RunPoint[] newArray(int size) {
            return new RunPoint[size];
        }
    };

}
