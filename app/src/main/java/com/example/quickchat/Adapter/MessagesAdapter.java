package com.example.quickchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quickchat.Activity.ChatActivity;
import com.example.quickchat.ModelClass.Messages;
import com.example.quickchat.R;
import com.example.quickchat.SettingActivity;
import com.example.quickchat.ShowDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.quickchat.Activity.ChatActivity.Rid;
import static com.example.quickchat.Activity.ChatActivity.id;
import static com.example.quickchat.Activity.ChatActivity.rImage;
import static com.example.quickchat.Activity.ChatActivity.sImage;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_Rec = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        if(holder.getClass() == SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
            
            Picasso.get().load(sImage).into(viewHolder.circleImageView);
            ((SenderViewHolder) holder).circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SettingActivity.class);
                   // intent.putExtra("id",id);
                    context.startActivity(intent);
                }
            });
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());

            Picasso.get().load(rImage).into(viewHolder.circleImageView);

            ((ReceiverViewHolder) holder).circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowDetailsActivity.class);
                    intent.putExtra("id",Rid);
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSendId()))
        {
            return ITEM_SEND;
        }else{
           return ITEM_Rec;
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }
    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtmessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            txtmessage = itemView.findViewById(R.id.txtMessages);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtmessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);
            txtmessage = itemView.findViewById(R.id.txtMessages);
        }
    }
}
