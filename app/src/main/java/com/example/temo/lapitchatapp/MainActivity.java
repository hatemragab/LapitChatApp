package com.example.temo.lapitchatapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TabLayout tabLayout;
    ViewPager viewPager;
    MFragmetAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.TollMain);
        toolbar.setTitle("Main Activity");
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabe);
        viewPager = findViewById(R.id.view);
        adapter = new MFragmetAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut: {
                FirebaseAuth.getInstance().signOut();
                sendMeToStartActivity();

                break;
            }
            case R.id.sitting:
            {
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            }
            case R.id.AllUsers:
            {
                startActivity(new Intent(MainActivity.this,AllUsers.class));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            sendMeToStartActivity();


        }
    }

    private void sendMeToStartActivity() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

}
