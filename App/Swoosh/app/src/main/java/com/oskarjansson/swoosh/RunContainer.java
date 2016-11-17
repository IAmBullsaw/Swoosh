package com.oskarjansson.swoosh;

import android.location.Location;

import java.util.Date;
import java.util.List;

/**
 * Created by oskja067 on 2016-11-17.
 */

public class RunContainer {

    private List<Location> route;
    private Date date;
    private String runId;

    public RunContainer() {
        this.route = null;
        this.date = null;
        this.runId = null;
    }

    public RunContainer(String runId, List<Location> l) {
        setRunId(runId);
        setRoutes(l);
        updateDate();
    }

    public void updateDate(){
        if (this.route != null) {
            this.date = new Date(this.route.get(0).getTime() * 1000);
        } else {
            throw new RuntimeException("route is null");
        }
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getRunId() {
        return this.runId;
    }

    public void setRoutes(List<Location> l) {
        this.route = l;
    }

    public List<Location> getRoute() {
        return this.route;
    }

    public void setDate(Date d) {
        this.date = d;
    }

    public Date getDate() {
        return this.date;
    }


}
