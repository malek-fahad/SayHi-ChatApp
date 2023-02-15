package com.tecraa.sayhi.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecraa.sayhi.ChatAdapter;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.databinding.ActivityChatBinding;
import com.tecraa.sayhi.model.Chat;
import com.tecraa.sayhi.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    Intent intent;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    String remoteUserId;

    FirebaseUser firebaseUser;

    List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        remoteUserId = intent.getStringExtra("userID");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        chatList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatList.setLayoutManager(linearLayoutManager);
        
        getRemoteUser(remoteUserId);

        getAllChat();


        binding.toolbar.chatBackBtn.setOnClickListener(v -> {
            finish();
        });

        binding.sendBtn.setOnClickListener(v -> {
            String senderId = firebaseUser.getUid();
            String receiverId = remoteUserId;
            String message = binding.chatInput.getText().toString().trim();
            String messageId = databaseReference.push().getKey();
            String messageType = "text";
            String messageSeen = "1";

            sendMessage(senderId,receiverId,message,messageId,messageType,messageSeen);

        });

    }

    private void getAllChat() {
        databaseReference.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getSenderId().equals(firebaseUser.getUid())&&chat.getReceiverId().equals(remoteUserId)
                    || chat.getSenderId().equals(remoteUserId)&&chat.getReceiverId().equals(firebaseUser.getUid())){
                        chatList.add(chat);
                    }
                }
                ChatAdapter adapter = new ChatAdapter(chatList,firebaseUser.getUid());
                binding.chatList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String senderId, String receiverId, String message, String messageId, String messageType, String messageSeen) {

        Chat chat = new Chat(senderId,receiverId,message,messageId,messageType,messageSeen);

        databaseReference.child("chat").child(messageId).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ChatActivity.this, "send", Toast.LENGTH_SHORT).show();
                    binding.chatInput.setText("");
                }
            }
        });

    }

    private void getRemoteUser(String remoteUserId) {

        databaseReference.child("user").child(remoteUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User remoteUser = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(remoteUser.getProfileImage()).placeholder(R.drawable.img_profile_male_avatar).into(binding.toolbar.chatRemoteUserImg);
                binding.toolbar.chatRemoteUserName.setText(remoteUser.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}