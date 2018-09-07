package com.example.temo.lapitchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup the layout
        setContentView(R.layout.start_activity);
    }

    public void signUp(View view) {
        // if need new account move to register activity
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public void signIn(View view) {
        // if already have account button clicked move to login activity
        startActivity(new Intent(this, Login.class));
//        finish(); we need the ability to get back to the start activity
    }
}
