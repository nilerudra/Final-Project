package com.example.test3;



import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class page2 extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    AppCompatButton iv;
    FirebaseDatabase database;
    DatabaseReference ref;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        iv = findViewById(R.id.sb);
        iv.setOnClickListener(view -> signIn());

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");


        //spinner
        Spinner sp = findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        //here checks if user has already signed in. if yes the it does not return null and we go to secondactivity().
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            navigateToSecondActivity();
        }



    }

    void signIn()
    {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);

                Spinner sp = findViewById(R.id.spin);
                String s = sp.getSelectedItem() != null ? sp.getSelectedItem().toString() : "";

               // String myString = sharedPreferences.getString("myStringKey", "not found");

                //if(myString.equals("not found"))
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("myStringKey", s);
                    editor.apply();
                //}

                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "Please select account!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateToSecondActivity() {
        String myString = sharedPreferences.getString("myStringKey", "not found");


        if (myString.equals("Teacher")) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            ref.orderByChild("id").equalTo(acct.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(page2.this, "Welcome", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(page2.this, teachui.class);
                        i.putExtra("id", acct.getId());
                        startActivity(i);
                    } else {
                        finish();
                        Intent in = new Intent(page2.this, teacherlogin.class);
                        in.putExtra("id", acct.getId());
                        startActivity(in);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Firebase", "Failed to read value.", error.toException());
                }
            });
        }

        else if(myString.equals("Student"))
        {

            Intent in = new Intent(page2.this, stud_register.class);
            finish();
            startActivity(in);
        }
    }
}
