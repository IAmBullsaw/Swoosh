package com.oskarjansson.swoosh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private int RC_SIGN_IN;

    private LevelRequirements levelRequirements = new LevelRequirements();
    private int userLevel = 0;
    private String userTitle = "Runner";
    private String userName = "null";
    private int userXP = 0;

    public static final String KEY_PREF_SHARED = "SwooshApp";
    public static final String KEY_PREF_USERNAME = "SwooshApp.user.Name";
    public static final String KEY_PREF_USERXP = "SwooshApp.user.XP";
    public static final String KEY_PREF_USERLVL = "SwooshApp.user.Level";
    public static final String KEY_PREF_USERTITLE = "SwooshApp.user.Title";
    public static final String KEY_PREF_USER_LOGGEDIN = "SwooshApp.user.LoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firstly, check if user is logged in:
        RC_SIGN_IN = 0;
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);
        }

        // Get level data and achievement data
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference levelReference = firebaseDatabase.getReference("levelrequirements");
        DatabaseReference achievementReference = firebaseDatabase.getReference("achievements");

        // Get all levelrequirements
        LevelrequirementsEventListener lrEventlistener = new LevelrequirementsEventListener();
        lrEventlistener.setLevelRequirements( levelRequirements );
        levelReference.addValueEventListener(lrEventlistener);

        // Get all achievements
        AchievementsEventListener aEventListener = new AchievementsEventListener();
        achievementReference.addValueEventListener(aEventListener);

        // Set the contentview
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Load main fragment on start
        this.fragment = new MainFragment();
        this.fragmentManager = getSupportFragmentManager();
        this.fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Subscribe to shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_PREF_SHARED, MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        sharedPreferences.edit().putInt(MainActivity.KEY_PREF_USERXP,0).apply();

        // Load spinner with data
        loadSpinner();
    }

    private void loadSpinner() {
        // TODO: Load up spinner with missions from Firebase database :)
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // TODO: FIX KEBAB MENU or ERASE
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("MainActivity","Pref changed: " + s);
        if (s.equals(KEY_PREF_USERXP)) {
            int xp = sharedPreferences.getInt(KEY_PREF_USERXP,0);
            int level = levelRequirements.GetLevel(xp);
            String title = levelRequirements.GetTitle(xp);
            Log.d("MainActivity", "Pref changed: updated level and title: " + level + " " + title);
            if (level != -1) {
                sharedPreferences.edit().putInt(KEY_PREF_USERLVL, level).apply();
            }
            if (title != "Nope") {
                sharedPreferences.edit().putString(KEY_PREF_USERTITLE, title).apply();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume()");
        // TODO: Realize why you need 2 subscribes and 1 unsubscribe...............................
        getSharedPreferences(KEY_PREF_SHARED,MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
        getSharedPreferences(KEY_PREF_SHARED,MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause()");
        getSharedPreferences(KEY_PREF_SHARED,MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        this.fragment = null;
        Class fragmentClass = MainFragment.class;
        int id = item.getItemId();
        if (id == R.id.nav_run) {
            Log.d("Navigating","run");
            // Do nothing
            // It is already set as default
        } else if (id == R.id.nav_profile) {
            Log.d("Navigating","profile");
            fragmentClass = ProfileFragment.class;
        } else if (id == R.id.nav_history) {
            Log.d("Navigating","history");
            fragmentClass = HistoryFragment.class;
        } else if (id == R.id.nav_settings) {
            Log.d("Navigating","settings");
            fragmentClass = SettingsFragment.class;
        } else if (id == R.id.nav_share) {
            Log.d("Navigating","share");
        }

        try {
            this.fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        this.fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        //setTitle(item.getTitle());

        // Close drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Swoosh","Logged Out");
                    }
                });
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == RC_SIGN_IN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d("onActivityResult: ","RESULT_OK");

                user = firebaseAuth.getCurrentUser();
                DatabaseReference userReference = firebaseDatabase.getReference("user/"+user.getUid()+"/data");
                // Get user meta data
                UserDataEventListener udEventListener = new UserDataEventListener();

                // UserDataEventListener requires the SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(KEY_PREF_SHARED, Context.MODE_PRIVATE);
                udEventListener.setPrefs(sharedPreferences);

                userReference.addValueEventListener(udEventListener);

                // Set logged in :)
                sharedPreferences.edit().putBoolean(KEY_PREF_USER_LOGGEDIN, true).apply();

            } else if ( resultCode == RESULT_CANCELED ) {
                SharedPreferences sharedPreferences = getSharedPreferences(KEY_PREF_SHARED, Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(KEY_PREF_USER_LOGGEDIN, false).apply();
                Log.d("onActivityResult: ","RESULT_CANCELED");
            } else if ( resultCode == RESULT_FIRST_USER) {
                Log.d("onActivityResult: ","RESULT_FIRST_USER");
                SharedPreferences sharedPreferences = getSharedPreferences(KEY_PREF_SHARED, Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(KEY_PREF_USER_LOGGEDIN, false).apply();
            }

        }
    }



}
