package com.oskarjansson.swoosh;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MissionCompletedActivity extends AppCompatActivity {

    private int swooshUserXP;
    private ArrayList<RunPoint> swooshUserRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_completed);

        ArrayList<RunPoint> fetchedRun;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                fetchedRun = null;
            } else {
                fetchedRun = extras.getParcelableArrayList(Constants.SWOOSH_USER_RUN);
            }
        } else {
            fetchedRun = (ArrayList<RunPoint>) savedInstanceState.getSerializable(Constants.SWOOSH_USER_RUN);
        }
        swooshUserRun = fetchedRun;
        // Get XP
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SWOOSH_SHARED_PREFS,MODE_PRIVATE);
        swooshUserXP = sharedPreferences.getInt(Constants.SWOOSH_USER_XP,-1);
        Log.d("MissionCompleted","XP: " + swooshUserXP);

        TextView textViewXP = (TextView) findViewById(R.id.missionCompleted_swooshUserXP);
        textViewXP.setText(""+swooshUserXP);

    }
}
