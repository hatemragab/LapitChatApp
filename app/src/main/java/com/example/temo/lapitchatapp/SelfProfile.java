package com.example.temo.lapitchatapp;

import android.service.textservice.SpellCheckerService;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class SelfProfile extends AppCompatActivity {
    ImageView imageView;
    TextView profName, profStatus;
    DatabaseReference friends;
    FirebaseAuth mAuth;
    DatabaseReference notification;
    String recUserId;
    String myId;
    String mCurrentState;
    DatabaseReference friendRequests;
    Button send, cancel;
    DatabaseReference mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_profile);
        initialize();

        cancel.setVisibility(View.INVISIBLE);
        cancel.setEnabled(false);

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String image = dataSnapshot.child("image").getValue().toString();
                String Name = dataSnapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle(Name + "");
                profName.setText(Name);
                profStatus.setText(dataSnapshot.child("status").getValue().toString() + "");

                Picasso.get().load(image).placeholder(R.drawable.person).into(imageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        friendRequests.child(myId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(recUserId)) {
                    String type_req = dataSnapshot.child(recUserId).child("request_type").getValue().toString();
                    if (type_req.equals("receive")) {
                        mCurrentState = "req_received";

                        send.setText("accept Request Friend");
                        cancel.setVisibility(View.VISIBLE);
                        cancel.setEnabled(true);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelFriendRequest();
                            }
                        });


                    } else if (type_req.equals("send")) {
                        mCurrentState = "req_send";

                        send.setText("cancel Request");
                    }


                } else {

                    friends.child(myId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(recUserId)) {
                                mCurrentState = "friends";
                                send.setText("unFriend");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (!myId.equals(recUserId)) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    send.setEnabled(false);

                    if (mCurrentState.equals("not_friend")) {
                        sendRequestToPerson();

                    }


                    if (mCurrentState.equals("req_send")) {
                        cancelFriendRequest();

                    }

                    if (mCurrentState.equals("req_received")) {
                        acceptFriendRequest();

                    }
                    if (mCurrentState.equals("friends")) {
                        deleteFriends();
                    }


                }
            });

        } else {
            send.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
        }

    }

    public void cancleRequest(View view) {


    }


    private void deleteFriends() {
        friends.child(myId).child(recUserId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friends.child(recUserId).child(myId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        send.setEnabled(true);
                                        send.setText("send Request");
                                        mCurrentState = "not_friend";

                                    }
                                });


                    }
                });
    }

    private void acceptFriendRequest() {
        final String date = DateFormat.getDateInstance().format(new Date());

        friends.child(myId).child(recUserId).setValue(date)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friends.child(recUserId).child(myId).setValue(date)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        deleteFriendRequestAfterAccept();

                                    }
                                });


                    }
                });
    }

    private void deleteFriendRequestAfterAccept() {
        friendRequests.child(myId).child(recUserId)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                friendRequests.child(recUserId).child(myId).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mCurrentState = "friends";
                                send.setText("unfriend");
                                send.setEnabled(true);

                            }
                        });

            }
        });

    }

    private void cancelFriendRequest() {
        friendRequests.child(myId).child(recUserId)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                friendRequests.child(recUserId).child(myId).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mCurrentState = "not_friend";

                                send.setText("sendRequest");
                                send.setEnabled(true);

                            }
                        });

            }
        });
    }


    public void initialize() {
        imageView = findViewById(R.id.profImage);
        send = findViewById(R.id.selfSend);
        cancel = findViewById(R.id.selfCancel);
        profName = findViewById(R.id.profName);
        profStatus = findViewById(R.id.profStatus);
        Toolbar toolbar = findViewById(R.id.profTool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recUserId = getIntent().getExtras().get("key").toString();
        mAuth = FirebaseAuth.getInstance();
        myId = mAuth.getCurrentUser().getUid();
        friendRequests = FirebaseDatabase.getInstance().getReference().child("friend_request");
        mCurrentState = "not_friend";
        mData = FirebaseDatabase.getInstance().getReference("Users").child(recUserId);
        notification = FirebaseDatabase.getInstance().getReference().child("notification");
        friends = FirebaseDatabase.getInstance().getReference().child("friends");
    }

    public void sendRequestToPerson() {

        friendRequests.child(myId).child(recUserId).child("request_type").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    friendRequests.child(recUserId).child(myId).child("request_type").setValue("receive").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("from", myId);
                            hashMap.put("type", "request");
                            notification.child(recUserId).push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mCurrentState = "req_send";
                                    send.setText("cancel Request Friend");
                                    send.setEnabled(true);

                                }
                            });

                        }
                    });


                } else {
                    Toast.makeText(SelfProfile.this, "some thing is wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
