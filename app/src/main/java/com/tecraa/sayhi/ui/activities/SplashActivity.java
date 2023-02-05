package com.tecraa.sayhi.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().hide();

        Glide.with(SplashActivity.this).load(R.drawable.img_placholder_smilemonkey).into(binding.test);

        new Handler().postDelayed(() -> nextPage(),2000);


    }

    private void nextPage() {
        if (firebaseUser != null){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }
    }
}