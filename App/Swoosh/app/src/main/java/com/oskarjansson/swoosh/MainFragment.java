package com.oskarjansson.swoosh;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private int userLevel = 0;
    private String userTitle = "Title";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(MainActivity.KEY_PREF_USERTITLE))
        {
            Log.d("MainFragment","Shared pref changed, update title");
            updateTitle();
        } else if ( s.equals(MainActivity.KEY_PREF_USERLVL)) {
            Log.d("MainFragment","Shared pref changed, update level");
            updateLevel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button button = (Button) view.findViewById(R.id.main_Button_Startmission);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button","main_Button_Startmission CLICKED :D");

                Intent intent = (Intent) new Intent(view.getContext() , RunActivity.class );
                startActivity(intent);
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
        Log.d("MainFragment","onResume()");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.KEY_PREF_SHARED,Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MainFragment","onPause()");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.KEY_PREF_SHARED,Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void updateLevel() {
        if (getContext() == null) {
            return;
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.KEY_PREF_SHARED, Context.MODE_PRIVATE);
        userLevel = sharedPreferences.getInt(MainActivity.KEY_PREF_USERLVL,userLevel);
        Log.d("MainFragment","Userlevel: " + userLevel);
        View view = getView();
        TextView mainLevel = (TextView) view.findViewById(R.id.mainLevel);
        mainLevel.setText(String.valueOf(userLevel));
    }

    private void updateTitle() {
        if (getContext() == null) {
            return;
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.KEY_PREF_SHARED, Context.MODE_PRIVATE);
        userTitle = sharedPreferences.getString(MainActivity.KEY_PREF_USERTITLE, userTitle);
        Log.d("MainFragment","Usertitle: " + userTitle);
        View view = getView();
        TextView mainTitle = (TextView) view.findViewById(R.id.mainTitle);
        mainTitle.setText(userTitle);
    }


}
