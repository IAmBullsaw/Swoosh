package com.oskarjansson.swoosh;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HistoryFragment extends Fragment {


    private String userName;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userName = user.getUid();
        } else {
            throw new RuntimeException("History Fragment: No user.getUid(). What gives? This shan't happen!");
        }

        final ListView listView = (ListView) this.getView().findViewById(R.id.history_ListView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRuns = database.getReference("user/" + userName + "/workouts");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("HistoryFragment","onChildAdded " + dataSnapshot.getKey());
                RunContainer run = dataSnapshot.getValue(RunContainer.class);

                Log.d("HistoryFragment","");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("HistoryFragment","onChildChanged");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("HistoryFragment","onChildRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("HistoryFragment","onChildMoved");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("HistoryFragment","onCancelled");

            }
        };
        userRuns.addChildEventListener(childEventListener);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}
