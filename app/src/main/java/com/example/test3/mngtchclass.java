package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class mngtchclass extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ImageView imageView;
    BottomNavigationView bottomNavigationView;
    Toolbar t;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    String myString;
    public static String subId,sub_name;
    SharedPreferences sharedPreferences;

    public mngtchclass() throws IOException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mngtchclass);
        t = findViewById(R.id.toolbarmt);
        setSupportActionBar(t);
        ta = new Tasks();
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.task);

        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);

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

        myString = sharedPreferences.getString("myStringKey", "not found");

        Intent intent = getIntent();
        subId = intent.getStringExtra("sub_id");
        sub_name = intent.getStringExtra("name");
        Toast.makeText(mngtchclass.this,"" + subId,Toast.LENGTH_SHORT).show();
    }

    Attendance at = new Attendance();
    Tasks ta;
    People pe = new People();

    Scan_QRCode sc = new Scan_QRCode();
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
                if(myString.equals("Teacher"))
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, at)
                            .commit();
                }
                else if (myString.equals("Student"))
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, sc)
                            .commit();
                }

                return true;

            case R.id.people:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, pe)
                        .commit();
        }
        return false;
    }
}