package com.oskarjansson.swoosh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 0;

    // For Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    // private int called = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // called += 1;
        // Cache all data if we go offline
        // Also, this solves the problem of not pushing data if we go offline.
        // if (called <= 1) { // TODO: Make sure to only call this ONCE and firstly
        //    firebaseDatabase.setPersistenceEnabled(true);
        // }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null ) {
            // User was not signed in, prompt user to sign in
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.SWOOSH_USER_UID,firebaseUser.getUid());
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == RC_SIGN_IN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d("LoginActivity","RESULT_OK");

                firebaseUser = firebaseAuth.getCurrentUser();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.SWOOSH_USER_UID,firebaseUser.getUid());
                startActivity(intent);
                finish();

            } else if ( resultCode == RESULT_CANCELED ) {
                Log.d("LoginActivity","RESULT_CANCELED");
                // Todo: Do something about this"
                finish();
            } else if ( resultCode == RESULT_FIRST_USER) {
                Log.d("LoginActivity","RESULT_FIRST_USER");
            } else {
                Log.d("LoginActivity","Else: " + resultCode);
                finish();
            }

        }
    }
}
