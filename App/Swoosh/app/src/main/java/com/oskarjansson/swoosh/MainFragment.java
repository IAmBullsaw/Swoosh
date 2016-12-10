package com.oskarjansson.swoosh;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements SwooshFragment {

    private int swooshUserLevel;
    private String swooshUserTitle;
    private int swooshUserXP;
    private SwooshUser swooshUser;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button button = (Button) view.findViewById(R.id.main_Button_Startmission);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button","main_Button_Startmission CLICKED :D");

                Intent intent = (Intent) new Intent(view.getContext() , RunActivity.class );
                intent.putExtra(Constants.SWOOSH_USER_XP,swooshUserXP);
                startActivity(intent);
            }
        });

        String swooshUserUid;
        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if(extras == null) {
                swooshUserUid = null;
            } else {
                swooshUser = extras.getParcelable(Constants.SWOOSH_USER);
                swooshUserUid = extras.getString(Constants.SWOOSH_USER_UID);
                swooshUserXP = extras.getInt(Constants.SWOOSH_USER_XP);
                Log.d("MainFragment"," SwooshUser: " + swooshUser.getXp());
            }
        } else {
            swooshUserUid = (String) savedInstanceState.getSerializable(Constants.SWOOSH_USER_UID);
            int test = savedInstanceState.getInt(Constants.SWOOSH_USER_XP);
            swooshUserXP = test;
            swooshUser = savedInstanceState.getParcelable(Constants.SWOOSH_USER);
            Log.d("MainFragment"," SwooshUser: " + swooshUser.getXp());
        }

        Log.d("MainFragment","SwooshuserUid: " + swooshUserUid);
        if (swooshUserUid == null ) {
            // TODO: Crash and burn
            Log.d("MainFragment","SwooshUserUid is null");
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get reference to data
        // Set childlisteners to update Title and Level
        DatabaseReference dataReference = firebaseDatabase.getReference("user/"+swooshUserUid+"/data");
        dataReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if ( key.equals("level") ) {
                    swooshUserLevel = dataSnapshot.getValue(int.class);
                    updateLevel();
                } else if (key.equals("title")) {
                    swooshUserTitle = dataSnapshot.getValue(String.class);
                    updateTitle();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if ( key.equals("level") ) {
                    swooshUserLevel = dataSnapshot.getValue(int.class);
                    updateLevel();
                } else if (key.equals("title")) {
                    swooshUserTitle = dataSnapshot.getValue(String.class);
                    updateTitle();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateLevel();
        updateTitle();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLevel();
        updateTitle();
    }

    public void updateLevel() {
        View view = getView();
        TextView mainLevel = (TextView) view.findViewById(R.id.mainLevel);
        mainLevel.setText(String.valueOf(swooshUserLevel));
    }

    private void updateTitle() {
        View view = getView();
        TextView mainTitle = (TextView) view.findViewById(R.id.mainTitle);
        mainTitle.setText(swooshUserTitle);
    }

    @Override
    public void setSwooshUserTitle(String title) {
        swooshUserTitle = title;
        updateTitle();
    }

    @Override
    public void setSwooshUserLevel(int level) {
        swooshUserLevel = level;
        updateLevel();
    }
}
