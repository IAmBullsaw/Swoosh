package com.oskarjansson.swoosh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by oskja067 on 2016-11-30.
 */

public class RunContainerAdapter extends ArrayAdapter<RunContainer> {

    public RunContainerAdapter(Context context, ArrayList<RunContainer> runContainers) {
        super(context,0,runContainers);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RunContainer runContainer = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_run_list_view_box, parent, false);
        }

        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.history_run_list_view_box_title);
        TextView date = (TextView) convertView.findViewById(R.id.history_run_list_view_box_date);

        title.setText(runContainer.getRunId());
        date.setText(runContainer.getDate().toString());

        // Return the completed view to render on screen
        return convertView;
    }
}
