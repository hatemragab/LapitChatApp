package com.example.temo.lapitchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {
    EditText emailTxtView, passTxtView;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup layout
        setContentView(R.layout.activity_login);

        // setup layout components views
        emailTxtView = findViewById(R.id.login_email_txt);
        passTxtView = findViewById(R.id.login_pass_txt);

        // setup firebase
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        // configuring the progress dialog
        dialog = ProgressDialog.show(this, "SignIn",
                getString(R.string.login_waiting_msg), true, false);

        // get the email & password as Strings
        final String email = emailTxtView.getText().toString().trim();
        final String password = passTxtView.getText().toString();// password may contains space characters


        // sign in using the email and password
        signIn(email, password);
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // get the user information
                            final String online_user_id = firebaseAuth.getCurrentUser().getUid();
                            final String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            // get reference to users table
                            final DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference().child("Users");

                            // store device token
                            reference.child(online_user_id).child("device_token").setValue(deviceToken)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // close progress dialog
                                            dialog.dismiss();

                                            // start main activity
                                            startActivity(
                                                    new Intent(Login.this, MainActivity.class));

                                            // finish login activity
                                            finish();
                                        }
                                    });
                        } else {
                            // close progress dialog
                            dialog.dismiss();
                            Toast.makeText(Login.this, R.string.login_failed_msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
