package com.example.test3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class progressbar extends AppCompatActivity {

    String s = "50";
    Button calc;
    ProgressBar progressBar;
    EditText prog;
    int i = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);
        progressBar = findViewById(R.id.progressBar);
        prog = findViewById(R.id.progr);
        progressBar.setMax(100);
        calc = findViewById(R.id.cal);
        calc.setOnClickListener(view -> setpro());


        progressBar.setProgress(50);
    }

    public void setpro()
    {
        int a = Integer.parseInt(prog.getText().toString().trim());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 if(i <= a)
                 {
                    progressBar.setProgress(i);
                    i++;
                    handler.postDelayed(this,22);
                 }

            }
        }, 22);
        i = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        s = data.getStringExtra("1");
    }
}