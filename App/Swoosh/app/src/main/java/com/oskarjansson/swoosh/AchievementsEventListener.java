package com.oskarjansson.swoosh;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by oskja067 on 2016-12-05.
 */

public class AchievementsEventListener implements ValueEventListener {
    private ArrayList<Achievement> achievements;

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("AchievementsEventList: ", dataSnapshot.getValue().toString() );

        for (DataSnapshot child: dataSnapshot.getChildren()
             ) {
            Log.d("Child: ",""+child.getValue(Achievement.class));
            achievements.add( child.getValue(Achievement.class) );
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("AchievementsEventList: ","onCancelled");
    }
}
