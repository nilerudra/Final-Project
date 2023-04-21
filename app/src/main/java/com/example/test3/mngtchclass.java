package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mngtchclass extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ImageView imageView;
    BottomNavigationView bottomNavigationView;
    Toolbar t;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mngtchclass);
        t = findViewById(R.id.toolbarmt);
        setSupportActionBar(t);
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.task);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        Uri photoUrl = signInAccount.getPhotoUrl();

        imageView = findViewById(R.id.imgpromt);
        Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .circleCrop()
                .into(imageView);
    }

    Attendance at = new Attendance();
    Tasks ta = new Tasks();
    People pe = new People();
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.task:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, ta)
                        .commit();
                return true;

            case R.id.attendance:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, at)
                        .commit();
                return true;

            case R.id.people:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, pe)
                        .commit();
                return true;
        }
        return false;
    }
}