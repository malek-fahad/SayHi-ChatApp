package com.tecraa.sayhi;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ChatViewHolder extends RecyclerView.ViewHolder {

    TextView chatMessageTv;
    ImageView chatMessageImg,chatUserImage;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        chatMessageTv = itemView.findViewById(R.id.chatMessageTv);
        chatMessageImg = itemView.findViewById(R.id.chatMessageImg);
        chatUserImage = itemView.findViewById(R.id.chatUserImage);

    }
}
