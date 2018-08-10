package com.example.temo.lapitchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
    }

    public void newAccount(View view) {

        startActivity(new Intent(this, RegisterActivity.class));
finish();
    }

    public void oldAccount(View view) {

        startActivity(new Intent(this, Login.class));
finish();
    }
}
