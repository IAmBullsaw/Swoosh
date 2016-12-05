package com.oskarjansson.swoosh;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by oskja067 on 2016-12-05.
 */

public class AchievementsEventListener implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("AchievementsEventList: ", dataSnapshot.getValue().toString() );
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("AchievementsEventList: ","onCancelled");
    }
}
