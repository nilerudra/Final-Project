package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

public class lecpracschmng extends AppCompatActivity {
    AppCompatButton ap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecpracschmng);

        ap = findViewById(R.id.edit);
        ap.setOnClickListener(view -> nxtpg());
    }

    public void nxtpg()
    {
        startActivity(new Intent(lecpracschmng.this,lecprac.class));
    }
}