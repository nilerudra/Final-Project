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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class stud_register extends AppCompatActivity {
    GoogleSignInOptions gso1;
    GoogleSignInClient gsc1;
    TextView tt1,tt2,tt3;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_register);

        tt1 = findViewById(R.id.FullName);
        tt3 = findViewById(R.id.emailID);
        tt2 = findViewById(R.id.MobileNo);
        btn = findViewById(R.id.RegisterButton);
        gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc1 = GoogleSignIn.getClient(this,gso1);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            /*String s = acct.getId();*/
            String s1 = acct.getEmail();

           /* tt1.setText(s);*/
            tt3.setText(s1);
        }

        btn.setOnClickListener(view -> nextPg());
       /* bt.setOnClickListener(view -> signOut());*/
    }

    public void nextPg()
    {
        String a1 = tt1.getText().toString();
        String a2 = tt2.getText().toString();
        String a3 = tt3.getText().toString();

        if(!a1.isEmpty() && !a2.isEmpty() && isValidMobileNumber(a2) && isValidEmail(a3)) {
            startActivity(new Intent(stud_register.this,mngtchclass.class));
            finish();
        } else if (!isValidMobileNumber(a2)) {
            tt2.setError("Please enter a valid phone number");
            Toast.makeText(stud_register.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(a3)) {
            tt3.setError("Please enter a valid Email Address");
            Toast.makeText(stud_register.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(stud_register.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        }

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

    public boolean isValidMobileNumber(String mobileNumber) {
        String mobilePattern = "^[1-9]\\d{9}$"; // Define the pattern for a valid 10-digit mobile number
        return mobileNumber.matches(mobilePattern); // Check if the input matches the pattern
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}