package com.oskarjansson.swoosh;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d("ProfileFragment","OnCreateView()" );
        // Get swooshUser
        SwooshUser swooshUser = new SwooshUser();
        ArrayList<Achievement> achievements = new ArrayList<Achievement>();
        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if(extras == null) {
                swooshUser = null;
                achievements = null;
            } else {
                swooshUser = extras.getParcelable(Constants.SWOOSH_USER);
                achievements = extras.getParcelableArrayList(Constants.DB_ACHIEVEMENTS);
            }
        } else {
            super.onSaveInstanceState(savedInstanceState);
            swooshUser = savedInstanceState.getParcelable(Constants.SWOOSH_USER);
            achievements = savedInstanceState.getParcelableArrayList(Constants.DB_ACHIEVEMENTS);
        }
        if (swooshUser != null ) {
            Log.d("ProfileFragment", "Bundled: " + swooshUser.toString());
        } else { Log.d("ProfileFragment", "SwooshUser was null!");}

        if (achievements != null ) {
            Log.d("ProfileFragment", "Bundled: " + achievements.toString());
        } else { Log.d("ProfileFragment", "Achievements was null!");}
        HashMap<String,Integer> userData = swooshUser.getRequirements();
        for (Achievement achievement: achievements
             ) {
            achievement.updateAchieved(userData);
        }

        final AchievementAdapter achievementAdapter = new AchievementAdapter(this.getContext(), achievements);

        GridView gridView = (GridView) view.findViewById(R.id.profile_achievements_grid);

        gridView.setAdapter(achievementAdapter);

        return view;
    }



}
