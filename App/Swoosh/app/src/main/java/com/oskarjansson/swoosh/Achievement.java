package com.oskarjansson.swoosh;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oskja067 on 2016-12-11.
 */

public class Achievement implements Parcelable {
    private boolean achieved;
    private String description;
    private int mipMapId;
    private HashMap<String, Integer> requirements;
    private int progressPercent;

    public Achievement() {}

    public Achievement(Parcel parcel) {
        description = parcel.readString();
        mipMapId = parcel.readInt();
        Bundle bundle = parcel.readBundle();
        //noinspection unchecked
        requirements = (HashMap<String, Integer>) bundle.getSerializable("requirements");
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeInt(mipMapId);
        Bundle bundle = new Bundle();
        bundle.putSerializable("requirements",requirements);
        parcel.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Achievement> CREATOR = new Parcelable.Creator<Achievement>() {
        public Achievement createFromParcel(Parcel parcel) {
            return new Achievement(parcel);
        }

        public Achievement[] newArray(int size) {
            return new Achievement[size];
        }
    };


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMipMapId() {
        return mipMapId;
    }

    public void setMipMapId(int mipMapId) {
        this.mipMapId = mipMapId;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public void updateAchieved(HashMap<String, Integer> userData) {
        int achieved = 0;

        for (Map.Entry<String,Integer> req: requirements.entrySet()
             ) {
            Number n = userData.get(req.getKey());

            // No entry, this requirement is not met
            if (n == null) {
                continue;
            }

            // An entry, however, is it achieved?
            // Todo: Find a better way to compare
            int a = (int)n;
            int b = req.getValue();
            Log.d(req.getKey()+" Comparing:",""+a+" >= "+b);
            if ( a >= b ) {
                achieved++;
            }
        }
        Log.d("Achievement",description + ", achieved: " + achieved + ", requirements: " + requirements.size() +  ", achieved: " + (achieved == requirements.size()) + ", progress: " + (int) ( achieved*100.0f / requirements.size() ));
        this.achieved =  ( achieved == requirements.size() );
        this.progressPercent = (int) ( achieved*100.0f / requirements.size() );
    }

    public HashMap<String, Integer> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<String, Integer> requirements) {
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return "[Achievement: " + description + ", achieved: " + this.achieved + ", progress: " + progressPercent + ", mipMap: " + mipMapId +"]";
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

}
