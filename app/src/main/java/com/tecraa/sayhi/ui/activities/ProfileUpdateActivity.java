package com.tecraa.sayhi.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.databinding.ActivityProfileUpdateBinding;

import java.net.URI;
import java.util.HashMap;

public class ProfileUpdateActivity extends AppCompatActivity {


    ActivityProfileUpdateBinding binding;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    Uri profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(ProfileUpdateActivity.this);
        progressDialog.setMessage("Please wait...");

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.updateProfileImageBtn.setOnClickListener(v -> {

            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start(101);

        });

        binding.profileInfoUpdateBtn.setOnClickListener(v->{

            progressDialog.show();

            StorageReference uploadRef = storageReference.child("profile-img-"+firebaseUser.getUid());
            uploadRef.putFile(profileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()){

                        uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String imageUri = String.valueOf(uri);
                                HashMap<String,Object> userMap = new HashMap<>();
                                userMap.put("profileImage",imageUri);

                                databaseReference.child(firebaseUser.getUid()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(ProfileUpdateActivity.this,MainActivity.class));
                                        finish();
                                    }
                                });

                            }
                        });
                    }

                }
            });
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101 && resultCode == Activity.RESULT_OK){
            profileUri = data.getData();
            binding.updateProfileImage.setImageURI(profileUri);
            binding.profileInfoUpdateBtn.setVisibility(View.VISIBLE);
        }



    }
}