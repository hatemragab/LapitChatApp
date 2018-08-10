package com.example.temo.lapitchatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecAdapter extends FirebaseRecyclerAdapter<Users,RecAdapter.ViewHolderr> {

    public RecAdapter( Query ref) {
        super(Users.class, R.layout.user_item, ViewHolderr.class, ref);
    }

    @Override
    protected void populateViewHolder(ViewHolderr viewHolder, Users model, int position) {

        viewHolder.textName.setText(model.getName());
        viewHolder.textStatus.setText(model.getStatus());

        String  s = model.getImage();
        Picasso.get().load(s).into(viewHolder.imageView);

    }

    public static class ViewHolderr extends RecyclerView.ViewHolder {
        TextView textName,textStatus;
        ImageView imageView;
        public ViewHolderr(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.recName);
            textStatus=itemView.findViewById(R.id.recStatus);
            imageView = itemView.findViewById(R.id.recImag);
        }
    }

}
