package com.oskarjansson.swoosh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private int RC_SIGN_IN;
    private boolean LOGGED_IN;

    private LevelRequirements levelRequirements = new LevelRequirements();
    private int userLevel = 0;
    private String userTitle = "Runner";
    private String userName = "null";
    private int userXP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // See if we are logged in
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
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ValueEventListener: ", dataSnapshot.getValue().toString() );
                Log.e("ValueEventListener" ,"Count: "+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    levelRequirements.Add(postSnapshot.getValue(LevelRequirement.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ValueEventListener: ","onCancelled");
            }
        };
        levelReference.addValueEventListener(valueEventListener);

        // Get all achievements
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ValueEventListener1: ", dataSnapshot.getValue().toString() );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ValueEventListener1: ","onCancelled");
            }
        };
        achievementReference.addValueEventListener(valueEventListener1);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Load main fragment on start
        this.fragment = new MainFragment();
        this.fragmentManager = getSupportFragmentManager();
        this.fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        loadSpinner();

    }

    private void loadSpinner() {

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
/*    @Override
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

        try {
            MainFragment mainFragment = (MainFragment) fragment;
            mainFragment.updateLevelAndTitle(userTitle,userLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        this.fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        // Uncomment this if you'd like.
        //setTitle(item.getTitle());

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

    protected void UpdateSwooshUser() {
        userLevel = levelRequirements.GetLevel( userXP );
        userTitle = levelRequirements.GetTitle( userXP );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RC_SIGN_IN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                user = firebaseAuth.getCurrentUser();

                DatabaseReference userReference = firebaseDatabase.getReference("user/"+user.getUid()+"/data");
                // Get user meta data
                ValueEventListener valueEventListener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("ValueEventListener2: ", dataSnapshot.getValue().toString() );
                        Log.d("ValueEventListener2","name: " + dataSnapshot.child("name"));
                        userName = (String) dataSnapshot.child("name").getValue();
                        userXP = Integer.parseInt( dataSnapshot.child("xp").getValue().toString() );

                        SharedPreferences sharedPreferences = getSharedPreferences("SwooshApp", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("SwooshApp.userName",userName).commit();
                        sharedPreferences.edit().putString("SwooshApp.userXP",userName).commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("ValueEventListener2: ","onCancelled");
                    }
                };
                userReference.addValueEventListener(valueEventListener2);
                // Do something with the contact here (bigger example below)
            }
        }
    }

}
