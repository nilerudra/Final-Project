package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class stud_register extends AppCompatActivity {
    GoogleSignInOptions gso1;
    GoogleSignInClient gsc1;
    TextView tt1,tt2;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_register);

        tt1 = findViewById(R.id.FullName);
        tt2 = findViewById(R.id.emailID);
        btn = findViewById(R.id.RegisterButton);
        gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc1 = GoogleSignIn.getClient(this,gso1);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            String s = acct.getId();
            String s1 = acct.getEmail();

            tt1.setText(s);
            tt2.setText(s1);
        }

        btn.setOnClickListener(view -> nextPg());
       /* bt.setOnClickListener(view -> signOut());*/
    }

    public void nextPg()
    {
        startActivity(new Intent(stud_register.this,mngtchclass.class));
        finish();
    }
    /*void signOut()
    {
        gsc.signOut().addOnCompleteListener(task -> {

            *//*SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("myStringKey");
            editor.apply();*//*

            finish();
            startActivity(new Intent(stud_register.this,page2.class));
        });
    }*/
}