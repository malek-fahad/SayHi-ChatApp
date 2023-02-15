package com.tecraa.sayhi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tecraa.sayhi.model.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private Context context;
    private List<Chat> chatList;

    private String currentUserID;
    private final static int RIGHT= 0;
    private final static int LEFT= 1;

    public ChatAdapter(Context context, List<Chat> chatList, String currentUserID) {
        this.context = context;
        this.chatList = chatList;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==RIGHT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_chat_ui,parent,false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_chat_ui,parent,false);
        }
         return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        Chat chat = chatList.get(position);
        if (chat.getMessageType().equals("text")){
            holder.chatMessageTv.setVisibility(View.VISIBLE);
            holder.chatMessageTv.setText(chat.getMessage());
        }else if (chat.getMessageType().equals("img")){
            holder.chatMessageImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(chat.getMessage()).into(holder.chatMessageImg);
        }



    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (chatList.get(position).getSenderId().equals(currentUserID)){
            return LEFT;
        }else {
            return RIGHT;
        }
    }
}
