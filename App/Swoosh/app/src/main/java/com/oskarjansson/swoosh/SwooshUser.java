package com.oskarjansson.swoosh;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by oskja067 on 2016-12-06.
 */

public class SwooshUser implements Parcelable {
    private String title;
    private String name;
    private int level;
    private int xp;
    private String uID;
    private HashMap<String,Number> requirements;

    // Let's just keep it this way, shall we not ?
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(name);
        parcel.writeInt(level);
        parcel.writeInt(xp);
        parcel.writeString(uID);
        Bundle bundle = new Bundle();
        bundle.putSerializable("requirements",requirements);
        parcel.writeBundle(bundle);
    }

    public SwooshUser(Parcel parcel) {
        title = parcel.readString();
        name = parcel.readString();
        level = parcel.readInt();
        xp = parcel.readInt();
        uID = parcel.readString();
        Bundle bundle = parcel.readBundle();
        //noinspection unchecked
        requirements = (HashMap<String, Number>) bundle.getSerializable("requirements");
    }

    public static final Parcelable.Creator<SwooshUser> CREATOR = new Parcelable.Creator<SwooshUser>() {
        public SwooshUser createFromParcel(Parcel parcel) {
            return new SwooshUser(parcel);
        }

        public SwooshUser[] newArray(int size) {
            return new SwooshUser[size];
        }
    };


    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public SwooshUser() {
        this.level = -1;
        this.title = "null";
        this.xp = -1;
        this.requirements = new HashMap<String, Number>();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Number> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<String, Number> requirements) {
        this.requirements = requirements;
    }

}
