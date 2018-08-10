package com.example.temo.lapitchatapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {

    TextView disName, statName;
    ImageView imageView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference reference;
    Uri uriImag;
    StorageReference mStorge;
    Bitmap thumb_bitmab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        disName = findViewById(R.id.disName);
        statName = findViewById(R.id.statName);
        imageView = findViewById(R.id.settingImg);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorge = FirebaseStorage.getInstance().getReference("Photos");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String q = dataSnapshot.child("name").getValue().toString();
                String w = dataSnapshot.child("status").getValue().toString();
                final   String imge= dataSnapshot.child("image").getValue().toString();
                disName.setText(q + "");
                statName.setText(w + "");
                if (imge.equals("image")) {

                    Picasso.get().load(R.drawable.person).placeholder(R.drawable.person).into(imageView);
                } else {
                    Picasso.get().load(imge).networkPolicy(NetworkPolicy.OFFLINE) .placeholder(R.drawable.person).into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(imge).placeholder(R.drawable.person).into(imageView);
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void changeIMG(View view) {


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "choose"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImag = data.getData();

            Picasso.get().load(uriImag).into(imageView);


            StorageReference fileRef = mStorge.child(mUser.getUid() + "." + getExtention(uriImag));
            fileRef.putFile(uriImag).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        DatabaseReference reference1 = reference.child("image");
                        reference1.setValue(task.getResult().getDownloadUrl().toString());


                        Toast.makeText(SettingActivity.this, "succesed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public void ChangeStatus(View view) {
        startActivity(new Intent(SettingActivity.this, Status_Change.class));
    }

    public String getExtention(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

}
