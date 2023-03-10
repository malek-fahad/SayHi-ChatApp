package com.tecraa.sayhi.ui.activities;

import static com.tecraa.sayhi.utils.CustomAlertDialog.showAlert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.databinding.ActivityLoginBinding;
import com.tecraa.sayhi.model.User;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");



        binding.registrationTV.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this, SingUpActivity.class));
            finish();
        });


        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.emailET.getText().toString();
            String password = binding.passwordET.getText().toString();

            loginUser(email,password);

        });


    }

    private void loginUser(String email, String password) {



        if (email.equals("")){
            showAlert(LoginActivity.this,"Enter your email address", R.drawable.icon_warning);
        }else if (password.equals("")){
            showAlert(LoginActivity.this,"Enter your password",R.drawable.icon_warning);
        }else {
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                progressDialog.dismiss();
                                if (user.getProfileImage().equals("img")){
                                    startActivity(new Intent(LoginActivity.this,ProfileUpdateActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    showAlert(LoginActivity.this,e.getMessage(),R.drawable.icon_warning);
                }
            });
        }

    }
}