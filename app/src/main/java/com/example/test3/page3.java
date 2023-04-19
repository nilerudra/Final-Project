package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class page3 extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView t1,t2;
    AppCompatButton bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);

        t1 = findViewById(R.id.name);
        t2 = findViewById(R.id.email);
        bt = findViewById(R.id.bt);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            String s = acct.getId();
            String s1 = acct.getEmail();

            t1.setText(s);
            t2.setText(s1);
        }




        bt.setOnClickListener(view -> signOut());


    }



    void signOut()
    {
        gsc.signOut().addOnCompleteListener(task -> {

            /*SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("myStringKey");
            editor.apply();*/

            finish();
            startActivity(new Intent(page3.this,page2.class));
        });
    }

}