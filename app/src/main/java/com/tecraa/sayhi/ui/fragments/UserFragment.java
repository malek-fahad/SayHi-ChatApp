package com.tecraa.sayhi.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecraa.sayhi.databinding.FragmentUserBinding;
import com.tecraa.sayhi.ui.User;
import com.tecraa.sayhi.ui.UserAdapter;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }

    FragmentUserBinding binding;
    DatabaseReference databaseReference;
    List<User> userList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentUserBinding.inflate(inflater, container, false);

        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);

                    userList.add(user);
                }


                UserAdapter adapter = new UserAdapter(getContext(),userList);
                binding.userListRV.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        return binding.getRoot();
    }
}