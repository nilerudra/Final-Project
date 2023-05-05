package com.example.test3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class splashScreen extends AppCompatActivity {
    private TextView name;
    private ImageView logo;
    private View topview1, topview2, topview3;
    private View bottomview1, bottomview2,  bottomview3;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setContentView(R.layout.activity_splash_screen);

        name = findViewById(R.id.TVName);
        logo = findViewById(R.id.IVLogo);
        topview1 = findViewById(R.id.topview1);
        topview2 = findViewById(R.id.topview2);
        topview3 = findViewById(R.id.topview3);
        bottomview1 = findViewById(R.id.bottomview1);
        bottomview2 = findViewById(R.id.bottomview2);
        bottomview3 = findViewById(R.id.bottomview3);

        Animation logoAnimation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.zoom_animation);
        Animation nameAnimation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.zoom_animation);

        Animation topview1Animation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.topviews_animation);
        Animation topview2Animation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.topviews_animation);
        Animation topview3Animation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.topviews_animation);

        Animation bottomview1Animation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.bottomviews_animation);
        Animation bottomview2Animation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.bottomviews_animation);
        Animation bottomview3Animation = AnimationUtils.loadAnimation(splashScreen.this, R.anim.bottomviews_animation);

        topview1.startAnimation(topview1Animation);
        bottomview1.startAnimation(bottomview1Animation);

        topview1Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topview2.setVisibility(View.VISIBLE);
                bottomview2.setVisibility(View.VISIBLE);

                topview2.startAnimation(topview2Animation);
                bottomview2.startAnimation(bottomview2Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topview2Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topview3.setVisibility(View.VISIBLE);
                bottomview3.setVisibility(View.VISIBLE);

                topview3.startAnimation(topview3Animation);
                bottomview3.startAnimation(bottomview3Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        topview3Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(logoAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                name.setVisibility(View.VISIBLE);
                final String animateTxt = name.getText().toString();
                name.setText("");
                count = 0;

                new CountDownTimer(animateTxt.length() * 100L, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        name.setText(name.getText().toString()+animateTxt.charAt(count));
                        count++;
                    }
                    @Override
                    public void onFinish() {
                        Intent i = new Intent(splashScreen.this, page2.class);
                        startActivity(i);
                        finish();
                    }
                }.start();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}