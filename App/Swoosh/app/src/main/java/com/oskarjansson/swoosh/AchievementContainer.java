package com.oskarjansson.swoosh;

/**
 * Created by oskja067 on 2016-11-30.
 */

public class AchievementContainer {

    private boolean achieved;
    private String description;
    private int progressPercent;
    private int mipMapId;

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
}
