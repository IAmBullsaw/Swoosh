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

        // Get userID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName;
        if (user != null) {
            userName = user.getUid();
        } else {
            throw new RuntimeException("Profile Fragment: No user.getUid(). What gives? This shan't happen!");
        }

        final AchievementAdapter achievementAdapter = new AchievementAdapter(this.getContext(),new ArrayList<AchievementContainer>());

        // TODO: Push some achievements to be able to pull them to be able to show them :D

        // Get User Progress
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user/" + userName + "/achievements");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ProfileFragment","onChildAdded " + dataSnapshot.getKey());
                AchievementContainer achievementContainer = dataSnapshot.getValue(AchievementContainer.class);
                achievementAdapter.add(achievementContainer);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        };
        databaseReference.addChildEventListener(childEventListener);

        GridView gridView = (GridView) view.findViewById(R.id.profile_achievements_grid);

        gridView.setAdapter(achievementAdapter);

        return view;
    }



}
