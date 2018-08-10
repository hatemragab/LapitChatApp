package com.example.temo.lapitchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllUsers extends AppCompatActivity {
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        recyclerView = findViewById(R.id.rec);
        Toolbar toolbar = findViewById(R.id.rectool);
        setSupportActionBar(toolbar);
        toolbar.setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("/Users");
        FirebaseRecyclerAdapter<Users, ViewHolderr> recAdapter = new FirebaseRecyclerAdapter<Users, ViewHolderr>(
                Users.class,
                R.layout.user_item,
                ViewHolderr.class,
                mdatabase
        ) {


            @Override
            protected void populateViewHolder(ViewHolderr viewHolder, final Users model, final int position) {
                viewHolder.textName.setText(model.getName());
                viewHolder.textStatus.setText(model.getStatus());
                String s = model.getImage();
                Picasso.get().load(s).placeholder(R.drawable.person).into(viewHolder.imageView);
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(AllUsers.this, ""+getRef(position).getKey()+position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AllUsers.this,SelfProfile.class);
                        intent.putExtra("key",getRef(position).getKey());
                        startActivity(intent);

                    }
                });


            }
        };


        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public static class ViewHolderr extends RecyclerView.ViewHolder {
        TextView textName, textStatus;
        ImageView imageView;
        View view;
        public ViewHolderr(View itemView) {
            super(itemView);
            view=itemView;
            textName = itemView.findViewById(R.id.recName);
            textStatus = itemView.findViewById(R.id.recStatus);
            imageView = itemView.findViewById(R.id.recImag);
        }
    }

}
