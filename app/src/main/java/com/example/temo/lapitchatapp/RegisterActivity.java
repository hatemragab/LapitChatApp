package com.example.temo.lapitchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, password;
    FirebaseAuth mAuth;
    ProgressDialog dialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        Toolbar t = findViewById(R.id.regTool);
        t.setTitle("register activity");
        setSupportActionBar(t);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }
    HashMap<String ,String >hashUser;
    public void getAcount(View view) {
        String nname = name.getText().toString().trim();
        String nemail = email.getText().toString().trim();
        String npass = password.getText().toString().trim();
        regUser(nname, nemail, npass);

    }

    private void regUser(final String DisName, String email, String password) {
        dialog.setCancelable(false);
        dialog.setMessage("wait will register your account !");
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {

                    String DivecToken= FirebaseInstanceId.getInstance().getToken();


                    String  UserID=  mAuth.getCurrentUser().getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
                    Users users = new Users(name.getText().toString().trim(),"status","image");
                    databaseReference.child("device_token").setValue(DivecToken);
                    databaseReference.setValue(users);

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "wrong" + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
