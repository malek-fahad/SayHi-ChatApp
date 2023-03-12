package com.tecraa.sayhi.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    StorageReference storageReference;
    Toolbar toolbar;
    String remoteUserId;

    FirebaseUser firebaseUser;

    List<Chat> chatList;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("chat");

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
            binding.chatInput.setText("");

        });

        binding.imageSend.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start(1000);
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
                ChatAdapter adapter = new ChatAdapter(ChatActivity.this,chatList,firebaseUser.getUid());
                binding.chatList.setAdapter(adapter);
                binding.progressbar.setVisibility(View.GONE);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1000 && resultCode == Activity.RESULT_OK){
            imageUri = data.getData();
            sendImageDatabase(imageUri);

        }

    }

    private void sendImageDatabase(Uri imageUri) {
        String messageId = databaseReference.push().getKey();

        StorageReference uploadRef = storageReference.child("SayHi-"+firebaseUser.getUid()+messageId);
        uploadRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String senderId = firebaseUser.getUid();
                            String receiverId = remoteUserId;
                            String message = String.valueOf(uri);
                            String messageType = "img";
                            String messageSeen = "1";

                            sendMessage(senderId,receiverId,message,messageId,messageType,messageSeen);


                        }
                    });

                }

            }
        });

    }
}