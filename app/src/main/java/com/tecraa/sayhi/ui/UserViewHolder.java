package com.tecraa.sayhi.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecraa.sayhi.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    ImageView contactProfileImg,contactMassageIcon;
    public TextView contactProfileName,contactLastMessage;;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        contactProfileImg = itemView.findViewById(R.id.contactProfileImg);
        contactMassageIcon = itemView.findViewById(R.id.contactMassageIcon);
        contactProfileName = itemView.findViewById(R.id.contactProfileName);
        contactLastMessage = itemView.findViewById(R.id.contactLastMessage);
    }
}
