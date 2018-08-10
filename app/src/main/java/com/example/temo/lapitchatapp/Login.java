package com.example.temo.lapitchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    EditText editText, editText2;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText = findViewById(R.id.loginEmail);
        editText2 = findViewById(R.id.loginPassword);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
      reference= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void LogIn(View view) {
        String e = editText.getText().toString().trim();
        String p = editText2.getText().toString().trim();
        progressDialog.setMessage("wait while sign in ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        sginInWithEAP(e, p);

    }

    private void sginInWithEAP(String em, String pa) {
        mAuth.signInWithEmailAndPassword(em, pa).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String online_user_id=mAuth.getCurrentUser().getUid();
                    String DivecToken= FirebaseInstanceId.getInstance().getToken();
                    reference.child(online_user_id).child("device_token").setValue(DivecToken)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();


                                    startActivity(new Intent(Login.this,MainActivity.class));
                                    finish();

                                }
                            });



                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "em and pass wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
