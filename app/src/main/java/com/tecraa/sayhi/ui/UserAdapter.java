package com.tecraa.sayhi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.model.User;
import com.tecraa.sayhi.utils.UserListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    Context context;
    List<User> userList;

    UserListener userListener;

    public UserAdapter(Context context, List<User> userList,UserListener userListener) {
        this.context = context;
        this.userList = userList;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        holder.contactProfileName.setText(userList.get(position).getUserName());
        Glide.with(context).load(userList.get(position).getProfileImage()).placeholder(R.drawable.img_profile_male_avatar).into(holder.contactProfileImg);

        holder.contactMassageIcon.setOnClickListener(v->{
            userListener.ItemClick(userList.get(position));

//            Intent intent = new Intent(context, ChatActivity.class);
//            intent.putExtra("userID",userList.get(position).getUserId());
//            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
