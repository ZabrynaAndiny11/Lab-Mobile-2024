package com.example.praktikum_4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<Instagram> instagrams;
    public SearchAdapter(ArrayList<Instagram> instagrams) {
        this.instagrams = instagrams;
    }

    public void setFilteredList(ArrayList<Instagram> filteredList){
        this.instagrams = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Instagram instagram1 = instagrams.get(position);

        holder.tv_username.setText(instagram1.getUsername());
        holder.tv_name.setText(instagram1.getName());
        holder.ivProfile.setImageResource(instagram1.getFotoProfile());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, ProfileActivity.class);
                intent.putExtra("instagram", instagram1);
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return instagrams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tv_username, tv_name;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.TV_username);
            tv_name = itemView.findViewById(R.id.TV_name);
            ivProfile = itemView.findViewById(R.id.IV_Profile);
            context = itemView.getContext();
        }
    }
}