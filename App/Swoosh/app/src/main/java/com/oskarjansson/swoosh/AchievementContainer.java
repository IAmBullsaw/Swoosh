package com.oskarjansson.swoosh;

import java.util.HashMap;

/**
 * Created by oskja067 on 2016-11-30.
 */

public class AchievementContainer {

    private boolean achieved;
    private String description;
    private int progressPercent;
    private int mipMapId;
    private HashMap<String,Integer> requirements;

    public AchievementContainer() {}

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMipMapId() {
        return mipMapId;
    }

    public void setMipMapId(int mipMapId) {
        this.mipMapId = mipMapId;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public HashMap<String, Integer> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<String, Integer> requirements) {
        this.requirements = requirements;
    }
}
