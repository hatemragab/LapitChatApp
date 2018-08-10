package com.example.temo.lapitchatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Status_Change extends AppCompatActivity {
    Toolbar toolbar;
    EditText editText;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String key;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status__change);
        toolbar = findViewById(R.id.toolStatus);
        editText = findViewById(R.id.editS);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("change Status");
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        key = mUser.getUid();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(key).child("status");

    }

    public void changeS(View view) {
        String newSt=editText.getText().toString().trim();
        reference.setValue(newSt).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Status_Change.this, "done", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
