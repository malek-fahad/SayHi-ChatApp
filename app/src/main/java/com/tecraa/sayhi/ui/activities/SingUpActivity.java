package com.tecraa.sayhi.ui.activities;

import static com.tecraa.sayhi.utils.CustomAlertDialog.showAlert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.databinding.ActivitySingupBinding;

import java.util.HashMap;

public class SingUpActivity extends AppCompatActivity {

    ActivitySingupBinding binding;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SingUpActivity.this);
        progressDialog.setMessage("Please wait...");
        databaseReference = FirebaseDatabase.getInstance().getReference("user");



        binding.loginTV.setOnClickListener(v-> {
            startActivity(new Intent(SingUpActivity.this,LoginActivity.class));
            finish();
        });

        binding.singUpBtn.setOnClickListener(v->{
            String fullName = binding.fullNameET.getText().toString();
            String email = binding.emailET.getText().toString();
            String phoneNumber = binding.phoneNumberET.getText().toString();
            String password = binding.passwordET.getText().toString();
            String confirmPassword = binding.confirmPasswordET.getText().toString();


            registerNewUser(fullName,email,phoneNumber,password,confirmPassword);

        });


    }

    private void registerNewUser(String fullName, String email, String phoneNumber, String password, String confirmPassword) {

        if (fullName.equals("")){
            showAlert(SingUpActivity.this,"Enter your Full Name", R.drawable.icon_warning);
        }else if(email.equals("")){
            showAlert(SingUpActivity.this,"Enter your Email Address",R.drawable.icon_warning);
        }else if (phoneNumber.equals("")){
            showAlert(SingUpActivity.this,"Enter your Phone number",R.drawable.icon_warning);
        }else if(password.equals("")){
            showAlert(SingUpActivity.this,"Enter your Password",R.drawable.icon_warning);
        }else if (!password.equals(confirmPassword)){
            showAlert(SingUpActivity.this,"Password dosen't Match",R.drawable.icon_warning);
        }else {
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){


                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        String userId = firebaseUser.getUid();

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("userName",fullName);
                        userMap.put("userEmail",email);
                        userMap.put("userPhone",phoneNumber);
                        userMap.put("userId",userId);
                        userMap.put("profileImage","img");
                        userMap.put("coverImage","img");
                        databaseReference.child(userId).setValue(userMap).addOnSuccessListener(unused -> {
                            progressDialog.dismiss();
                            startActivity(new Intent(SingUpActivity.this,ProfileUpdateActivity.class));
                            finish();
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            showAlert(SingUpActivity.this,e.getMessage(),R.drawable.icon_warning);
                        });



                    }

                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                showAlert(SingUpActivity.this,e.getMessage(),R.drawable.icon_warning);
            });
        }

    }
}