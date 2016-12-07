package com.oskarjansson.swoosh;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by oskja067 on 2016-12-05.
 */

public class UserDataEventListener implements ValueEventListener {

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("UserDataEventListener: ","onCancelled: " + databaseError);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String userName = (String) dataSnapshot.child("name").getValue();
        int userXP = Integer.parseInt( dataSnapshot.child("xp").getValue().toString() );

        Log.d("udEventListener - PREF","Setting userXP: " + userXP + " userName: " + userName);
    }
}
