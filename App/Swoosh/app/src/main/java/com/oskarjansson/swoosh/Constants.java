package com.oskarjansson.swoosh;

/**
 * Created by oskja067 on 2016-12-07.
 */

public abstract class Constants {
    public static final String SWOOSH_USER_UID = "Swoosh.User.UID";
    public static final String SWOOSH_USER_TITLE = "Swoosh.User.TITLE";
    public static final String SWOOSH_USER_XP = "Swoosh.User.XP";
    public static final String SWOOSH_USER_LEVEL = "Swoosh.User.LEVEL";
    public static final String SWOOSH_USER_RUN = "Swoosh.User.RUN";
    public static final String SWOOSH_SHARED_PREFS = "Swoosh.User.PREFS";
    public static final String SWOOSH_USER = "Swoosh.User";

    public static final String DB_MISSIONS = "missions";
    public static final String DB_LEVELREQUIREMENTS = "levelrequirements";
    public static final String DB_ACHIEVEMENTS = "achievements";

    public static final String DB_USER_DATA_LEVEL = "level";
    public static final String DB_USER_DATA_NAME = "name";
    public static final String DB_USER_DATA_REQUIREMENTS = "requirements";
    public static final String DB_USER_DATA_TITLE = "title";
    public static final String DB_USER_DATA_XP = "xp";

    public static final String DB_USER_DATA_REQUIREMENTS_DISTANCE_ONE = "distance_one";
    public static final String DB_USER_DATA_REQUIREMENTS_DISTANCE_TOTALT = "distance_total";
    public static final String DB_USER_DATA_REQUIREMENTS_FASTEST_1K = "fastest_1k";
    public static final String DB_USER_DATA_REQUIREMENTS_WORKOUTS = "workouts";


    public static String UserDataDatabaseReference(String Uid) {
        return "user/"+Uid+"/data";
    }

    public static String UserWorkoutsDatabaseReference(String Uid) {
        return "user/"+Uid+"/workouts";
    }
}
