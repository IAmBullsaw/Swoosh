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
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
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


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SwooshApp", Context.MODE_PRIVATE);
        String test = sharedPreferences.getString("SwooshApp.userName","");


        TextView title = (TextView) view.findViewById(R.id.mainTitle);
        title.setText(test);


        return view;
    }

    public void updateLevelAndTitle(String userTitle, int userLevel ) {
        TextView title = (TextView) getView().findViewById(R.id.mainTitle);
        TextView level = (TextView) getView().findViewById(R.id.mainLevel);
        title.setText(userTitle);
        level.setText(userLevel);
    }

}
