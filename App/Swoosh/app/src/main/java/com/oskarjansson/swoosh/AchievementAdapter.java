package com.oskarjansson.swoosh;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by oskja067 on 2016-11-30.
 */

public class AchievementAdapter extends ArrayAdapter<Achievement>{

    public AchievementAdapter(Context context, ArrayList<Achievement> achievementContainers) {
        super(context,0,achievementContainers);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Achievement achievementContainer = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_achievement_grid_view_box, parent, false);
        }

        // Find all views
        ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_achievements_grid_box_image);
        TextView textView = (TextView) convertView.findViewById(R.id.profile_achievements_grid_box_text);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.profile_achievements_grid_box_progressbar);

        // Inflate it with data
        imageView.setImageResource( achievementContainer.getMipMapId() );
        textView.setText( achievementContainer.getDescription());

        // Update it if it is achieved
        if (achievementContainer.isAchieved()) {
            imageView.setBackgroundColor(Color.rgb(0, 255, 0));
        } else {
            progressBar.setProgress(achievementContainer.getProgressPercent());
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        Log.d("AchievementAdapter","Achievement: " + achievementContainer);


        return convertView;
    }
}
