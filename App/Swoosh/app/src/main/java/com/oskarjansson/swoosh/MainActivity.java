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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private int RC_SIGN_IN;

    private LevelRequirements levelRequirements = new LevelRequirements();
    private ArrayList<Achievement> achievements = new ArrayList<Achievement>();
    private SwooshUser swooshUser = new SwooshUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String swooshUserId;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                swooshUserId = null;
            } else {
                swooshUserId = extras.getString(Constants.SWOOSH_USER_UID);
            }
        } else {
            swooshUserId = (String) savedInstanceState.getSerializable(Constants.SWOOSH_USER_UID);
        }
        if (swooshUserId == null ) {
            // TODO: Crash and burn
            Toast.makeText(this,"THOU SHALT NOT BE HERE",Toast.LENGTH_LONG);
        }
        swooshUser.setuID(swooshUserId);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // Get references to level and achievements
        DatabaseReference levelReference = firebaseDatabase.getReference( Constants.DB_LEVELREQUIREMENTS );
        DatabaseReference achievementReference = firebaseDatabase.getReference( Constants.DB_ACHIEVEMENTS );
        final DatabaseReference dataReference = firebaseDatabase.getReference( Constants.UserDataDatabaseReference(swooshUser.getuID()) );

        dataReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if ( key.equals( Constants.DB_USER_DATA_XP ) ) {
                    int xp = dataSnapshot.getValue(int.class);
                    Log.d("MainActivity","xp is added: " + xp);
                    swooshUser.setXp(xp);

                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SWOOSH_SHARED_PREFS, MODE_PRIVATE);
                    sharedPreferences.edit().putInt(Constants.SWOOSH_USER_XP,xp).apply();

                    if (levelRequirements.isFilled()) {
                        int level = levelRequirements.GetLevel(xp);
                        String title = levelRequirements.GetTitle(xp);
                        swooshUser.setLevel(level);
                        swooshUser.setTitle(title);
                        dataReference.child(Constants.DB_USER_DATA_LEVEL).setValue(level);
                        dataReference.child(Constants.DB_USER_DATA_TITLE).setValue(title);
                    }
                } else if (key.equals(Constants.DB_USER_DATA_REQUIREMENTS)) {
                    Log.d("MainActivity","Requirements added");
                    HashMap<String,Integer> map = swooshUser.getRequirements();
                    for ( DataSnapshot child: dataSnapshot.getChildren()
                         ) {
                        // This also overwrites the data
                        Log.d("MainActivity","Requirement: " + child.getKey() + " <-> " + child.getValue().toString());
                        map.put(child.getKey(),child.getValue(int.class));
                    }
                } else if ( key.equals(Constants.DB_USER_DATA_NAME) ) {
                    Log.d("MainActivity","SwooshUserName added");
                    swooshUser.setName(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if ( key.equals(Constants.DB_USER_DATA_XP) ) {
                    int xp = dataSnapshot.getValue(int.class);
                    Log.d("MainActivity","xp is changed: " + xp);
                    swooshUser.setXp(xp);

                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SWOOSH_SHARED_PREFS, MODE_PRIVATE);
                    sharedPreferences.edit().putInt(Constants.SWOOSH_USER_XP,xp).apply();


                    if (levelRequirements.isFilled()) {
                        int level = levelRequirements.GetLevel(xp);
                        String title = levelRequirements.GetTitle(xp);
                        swooshUser.setLevel(level);
                        swooshUser.setTitle(title);
                        dataReference.child(Constants.DB_USER_DATA_LEVEL).setValue(level);
                        dataReference.child(Constants.DB_USER_DATA_TITLE).setValue(title);
                    }
                } else if (key.equals(Constants.DB_USER_DATA_REQUIREMENTS)) {
                    Log.d("MainActivity","Requirements Changed");
                    HashMap<String,Integer> map = swooshUser.getRequirements();
                    for ( DataSnapshot child: dataSnapshot.getChildren()
                            ) {
                        // This also overwrites data
                        Log.d("MainActivity","Requirement: " + child.getKey() + " <-> " + child.getValue().toString());
                        map.put(child.getKey(),child.getValue(int.class));
                    }
                }
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
        });

        // Get all levelrequirements
        LevelrequirementsEventListener lrEventlistener = new LevelrequirementsEventListener();
        lrEventlistener.setLevelRequirements( levelRequirements );
        levelReference.addValueEventListener(lrEventlistener);

        // Get all achievements
        AchievementsEventListener aEventListener = new AchievementsEventListener();
        aEventListener.setAchievements( achievements );
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
        currentFragment = new MainFragment();
        // Set arguments for MainFragment
        Bundle bundle = new Bundle();
        Log.d("RunActivity","currentXP: "+swooshUser.getXp());
        bundle.putString(Constants.SWOOSH_USER_UID,swooshUser.getuID());
        bundle.putInt(Constants.SWOOSH_USER_XP,swooshUser.getXp());
        bundle.putParcelable(Constants.SWOOSH_USER, swooshUser);
        currentFragment.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, currentFragment).commit();

        // Load spinner with data
        loadSpinner();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity","onDestroy()");
        super.onDestroy();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        this.currentFragment = null;
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
            this.currentFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the swooshUserUid for the love of god!!!
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.SWOOSH_USER, swooshUser);
        bundle.putString(Constants.SWOOSH_USER_UID,swooshUser.getuID());
        bundle.putInt(Constants.SWOOSH_USER_XP,swooshUser.getXp());
        bundle.putSerializable(Constants.DB_ACHIEVEMENTS, achievements);
        currentFragment.setArguments(bundle);

        // Insert the fragment by replacing any existing fragment
        this.fragmentManager.beginTransaction().replace(R.id.flContent, currentFragment).commit();

        // Close drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
