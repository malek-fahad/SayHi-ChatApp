package com.tecraa.sayhi.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecraa.sayhi.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    ImageView chatProfileImg;
    public TextView chatProfileName;
    TextView chatLastMessage;
    TextView chatLastTime;
    TextView chatUnreadMessageCount;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        chatProfileImg = itemView.findViewById(R.id.chatProfileImg);
        chatProfileName = itemView.findViewById(R.id.chatProfileName);
        chatLastMessage = itemView.findViewById(R.id.chatLastMessage);
        chatLastTime = itemView.findViewById(R.id.chatLastTime);
        chatUnreadMessageCount = itemView.findViewById(R.id.chatUnreadMessageCount);
    }
}
