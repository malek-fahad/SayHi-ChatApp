package com.tecraa.sayhi.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.databinding.FragmentProfileBinding;
import com.tecraa.sayhi.ui.User;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    DatabaseReference databaseReference;

    FirebaseUser firebaseUser;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                binding.userProfileNameTV.setText(user.getUserName());

                Glide.with(getContext()).load(user.getProfileImage()).placeholder(R.drawable.img_profile_male_avater).into(binding.userProfileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return binding.getRoot();
    }
}