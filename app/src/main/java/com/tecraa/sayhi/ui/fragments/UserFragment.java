package com.tecraa.sayhi.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecraa.sayhi.databinding.FragmentUserBinding;
import com.tecraa.sayhi.model.User;
import com.tecraa.sayhi.ui.UserAdapter;
import com.tecraa.sayhi.ui.activities.ChatActivity;
import com.tecraa.sayhi.utils.UserListener;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment implements UserListener {


    public UserFragment() {
        // Required empty public constructor
    }

    FragmentUserBinding binding;
    DatabaseReference databaseReference;
    List<User> userList;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentUserBinding.inflate(inflater, container, false);

        userList = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        firebaseAuth = FirebaseAuth.getInstance();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);

                    if (!firebaseAuth.getUid().equals(user.getUserId())){
                        userList.add(user);
                    }

                }


                UserAdapter adapter = new UserAdapter(getContext(),userList,UserFragment.this);
                binding.userListRV.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void ItemClick(User user) {

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("userID",user.getUserId());
        startActivity(intent);

    }
}