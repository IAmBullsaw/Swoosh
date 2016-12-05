package com.oskarjansson.swoosh;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by oskja067 on 2016-12-02.
 */

public class LevelRequirements {
    private ArrayList<LevelRequirement> levelRequirements;
    private int size;

    public LevelRequirements() {
        levelRequirements = new ArrayList<LevelRequirement>();
        size = 0;
    }

    public ArrayList getLevelRequirements() {
        return levelRequirements;
    }

    public void setLevelRequirements(ArrayList levelRequirements) {
        this.levelRequirements = levelRequirements;
        updateSize();
    }

    private void updateSize() {
        size = levelRequirements.size();
    }

    public void Add(LevelRequirement levelRequirement) {
        levelRequirements.add(levelRequirement);
        updateSize();
    }

    public int GetLevel(int xp) {
        int level = -1;

        for (int i = 0; i < size; i++) {
            LevelRequirement levelRequirement = levelRequirements.get(i);
            int req = levelRequirement.getXp();

            if (xp <= req) {
                if (i != 0) {
                    level = i - 1;
                } else {
                    level = i;
               }
               break;
            }
        }
        return level;
    }

    public String GetTitle(int xp) {
        String title = "Nope";

        for (int i = 0; i < size; i++) {
            LevelRequirement levelRequirement = levelRequirements.get(i);
            int req = levelRequirement.getXp();
            if (xp <= req) {
                if (i != 0) {
                    title = levelRequirements.get(i - 1).getTitle();
                } else {
                    title = levelRequirements.get(i).getTitle();
                }
                break;
            }
        }

        return title;
    }

}
