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

    public RunContainer() {
        this.route = null;
        this.date = null;
        this.runId = null;
    }

    public RunContainer(String runId, List<RunPoint> l) {
        this.route = l;
        this.runId = runId;
        this.date = new Date( l.get(0).getTime() );
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

    @Override
    public String toString() {
        return "[Run: " + this.runId + " Routesize: " + route.size() + " Date: " + date.toString() + "]";
    }
}
