package com.tecraa.sayhi.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.tecraa.sayhi.R;
import com.tecraa.sayhi.databinding.ActivityMainBinding;
import com.tecraa.sayhi.ui.fragments.HomeFragment;
import com.tecraa.sayhi.ui.fragments.ProfileFragment;
import com.tecraa.sayhi.ui.fragments.UserFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;

    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        fragmentManager  = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.fragmentContainer, new HomeFragment(),"").commit();


        Toast.makeText(MainActivity.this,"Welcome To Home",Toast.LENGTH_SHORT).show();

//        binding.logoutBtn.setOnClickListener(v->{

//        });


        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.homeMenu:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new HomeFragment(),"").commit();
                        break;
                    case R.id.userMenu:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new UserFragment(),"").commit();
                        break;
                    case R.id.profileMenu:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment(),"").commit();
                        break;
                }


                return true;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.top_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.homeTopMenu:
                Toast.makeText(this,"Home clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.userTopMenu:
                Toast.makeText(this,"User clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.profileTopMenu:
                Toast.makeText(this,"Profile clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.logoutTopMenu:
                logout();
                break;


        }



        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        firebaseAuth.signOut();
        Toast.makeText(MainActivity.this,"Successfully Logout",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }
}