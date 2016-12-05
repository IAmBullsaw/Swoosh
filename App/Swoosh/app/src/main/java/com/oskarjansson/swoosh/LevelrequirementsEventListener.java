package com.oskarjansson.swoosh;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by oskja067 on 2016-12-05.
 */

public class LevelrequirementsEventListener implements ValueEventListener {
    private LevelRequirements levelRequirements;

    public LevelRequirements getLevelRequirements() {
        return levelRequirements;
    }

    public void setLevelRequirements(LevelRequirements levelRequirements) {
        this.levelRequirements = levelRequirements;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("LevelreqEventListener: ","onCancelled");
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("ValueEventListener: ", dataSnapshot.getValue().toString() );
        Log.d("LevelreqEventListener" ,"Count: "+dataSnapshot.getChildrenCount());
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            levelRequirements.Add(postSnapshot.getValue(LevelRequirement.class));
        }
    }
}
