package com.oskarjansson.swoosh;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by oskja067 on 2016-12-05.
 */

public class UserDataEventListener implements ValueEventListener {

    private SharedPreferences prefs;

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("UserDataEventListener: ","onCancelled: " + databaseError);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {Log.d("onActivityResult: ", dataSnapshot.getValue().toString() );
        Log.d("UserDataEventListener","xp: " + dataSnapshot.child("xp").getValue() );
        String userName = (String) dataSnapshot.child("name").getValue();
        int userXP = Integer.parseInt( dataSnapshot.child("xp").getValue().toString() );
        Log.d("UserDataEventListener","userXP: " + userXP);

        prefs.edit().putString(MainActivity.KEY_PREF_USERNAME,userName).apply();
        prefs.edit().putInt(MainActivity.KEY_PREF_USERXP,userXP).apply();
    }
}
