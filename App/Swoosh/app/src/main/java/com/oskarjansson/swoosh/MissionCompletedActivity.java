package com.oskarjansson.swoosh;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = null;
        if (user != null) {
            userUid = user.getUid();
        }else {
            //TODO: We should do something about this....
        }
        // Get references to firebase
        DatabaseReference dbWorkouts, swooshUserData;
        dbWorkouts = database.getReference( Constants.UserWorkoutsDatabaseReference(userUid));
        swooshUserData = database.getReference( Constants.UserDataDatabaseReference(userUid));

        DatabaseReference workoutReference = dbWorkouts.push();
        RunContainer data = new RunContainer(workoutReference.getKey(),swooshUserRun);

        workoutReference.setValue(data);
        Log.d("MissionCompleted", "Pushed RunContainer to firebase! " + workoutReference.getKey());

        // Get XP
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SWOOSH_SHARED_PREFS,MODE_PRIVATE);
        swooshUserXP = sharedPreferences.getInt(Constants.SWOOSH_USER_XP,-1);
        Log.d("MissionCompleted","Got shared XP: " + swooshUserXP);

        // Get Views
        TextView textViewXP = (TextView) findViewById(R.id.missionCompleted_swooshUserXP);
        TextView textViewLength = (TextView) findViewById(R.id.missionCompleted_workout_length);

        // Set data
        textViewXP.setText(""+data.getXp());
        textViewLength.setText(""+data.getLength());

        // Push new xp to firebase
        swooshUserData.child("xp").setValue(swooshUserXP + data.getXp());
        Log.d("MissionCompleted", "Pushed xp to firebase! " + swooshUserXP + data.getXp());

        Button done = (Button) findViewById(R.id.missionCompleted_button_Done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
