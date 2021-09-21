package com.example.quickchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quickchat.Activity.ChatActivity;
import com.example.quickchat.Activity.HomeActivity;
import com.example.quickchat.ModelClass.Users;
import com.example.quickchat.R;
import com.example.quickchat.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context searchActivity;
    ArrayList<Users> usersArrayList;
    public SearchAdapter(SearchActivity searchActivity, ArrayList<Users> usersArrayList) {
        this.searchActivity = searchActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(searchActivity).inflate(R.layout.item_user_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users users = usersArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            if(usersArrayList.get(position).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                usersArrayList.remove(usersArrayList.get(position));

            }
        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);
        Picasso.get().load(users.imageUri).into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchActivity, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("Uid",users.getUid());
                searchActivity.startActivity(intent);
            }
        });



    }



    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_profile = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);

        }
    }
}
