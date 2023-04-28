package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class stud_register extends AppCompatActivity {
    GoogleSignInOptions gso1;
    GoogleSignInClient gsc1;
    TextView tt1,tt2,tt3;
    Button btn;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_register);

        tt1 = findViewById(R.id.FullName);
        tt2 = findViewById(R.id.emailID);
        tt3 = findViewById(R.id.MobileNo);
        btn = findViewById(R.id.RegisterButton);
        gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc1 = GoogleSignIn.getClient(this,gso1);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            String s1 = acct.getEmail();
            tt2.setText(s1);
        }

        btn.setOnClickListener(view -> nextPg());
       /* bt.setOnClickListener(view -> signOut());*/
    }

    public void nextPg()
    {
        String name = tt1.getText().toString().trim();
        String email = tt2.getText().toString().trim();
        String phone = tt3.getText().toString().trim();

        if(!name.isEmpty() && !email.isEmpty() && isValidMobileNumber(phone)) {
            Intent intent = getIntent();
            String id = intent.getStringExtra("id").toString();
            UserInfo u = new UserInfo(id,name,phone,email,"",id +"_0");

            String[] key = {"id", "name", "phone", "email", "gender", "identity"};
            String s = u.getIdentity();
            String[] value = {u.getId(), u.getName(), u.getPhone(), u.getEmail(), u.getGender(), s};

            for(int i = 0; i < 6; i++){
                databaseReference.child("Users").child(id).child(key[i]).setValue(value[i]);
            }

            Intent i = new Intent(stud_register.this, studui.class);
            i.putExtra("id",u.getId());
            startActivity(i);
            finish();

        } else if (!isValidMobileNumber(phone)) {
            tt3.setError("Please enter a valid phone number");
            Toast.makeText(stud_register.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(stud_register.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidMobileNumber(String mobileNumber) {
        String mobilePattern = "^[1-9]\\d{9}$"; // Define the pattern for a valid 10-digit mobile number
        return mobileNumber.matches(mobilePattern); // Check if the input matches the pattern
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