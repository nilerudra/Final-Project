package com.example.test3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    String myString,dynamicurl;
    public static String subId,sub_name,descp,hours,teacher_id;
    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences5;

    public mngtchclass() throws IOException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mngtchclass);
        t = findViewById(R.id.toolbarmt);
        setSupportActionBar(t);
        ta = new Tasks();
        //std_dashboard = new std_dashboard();
        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        String s = sharedPreferences.getString("myStringKey", "not found");
        myString = sharedPreferences.getString("myStringKey", "not found");

        sharedPreferences1 = getSharedPreferences("sub_name", Context.MODE_PRIVATE);


        Intent intent = getIntent();
        subId = intent.getStringExtra("sub_id");
        sub_name = intent.getStringExtra("name");
        descp = intent.getStringExtra("dsc");
        hours = intent.getStringExtra("lec");
        teacher_id = intent.getStringExtra("tid");

        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("sub", sub_name);
        editor.apply();

        editor.putString("subid", subId);
        editor.apply();

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        if(s.equals("Teacher")) {
            bottomNavigationView
                    .setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.task);
        } else if (s.equals("Student")) {
            //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);
            Menu menu = bottomNavigationView.getMenu();
            menu.clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu_std);


            bottomNavigationView
                    .setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.dashboard);
        }
        else
        {
            bottomNavigationView
                    .setOnNavigationItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.task);
        }


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

        imageView.setOnClickListener(view -> show_profile());

        sharedPreferences5 = getSharedPreferences("dynamicurl", Context.MODE_PRIVATE);
        dynamicurl = sharedPreferences5.getString("urldyn", "not found");
        if(!dynamicurl.equals("not found"))
        {
            startActivity(new Intent(this,uploadstdmaterial.class));
        }
    }

    private void show_profile() {
        Intent i = new Intent(mngtchclass.this, Profile_page.class);
        startActivity(i);
    }

    Attendance at = new Attendance();
    Tasks ta;
    std_dashboard std_dashboard = new std_dashboard();
    People pe = new People();

    Scan_QRCode sc = new Scan_QRCode();
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (myString.equals("Teacher")) {
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
                    
            }
        } else if (myString.equals("Student")) {
            switch (item.getItemId()) {
                case R.id.dashboard:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, std_dashboard)
                            .commit();
                    return true;


                case R.id.attendance:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.flFragment, sc)
                                .commit();
                    return true;

                case R.id.people:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, pe)
                            .commit();

            }
        }
        return false;
    }

    private void profile_details()
    {

    }
}