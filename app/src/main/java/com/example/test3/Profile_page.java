package com.example.test3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile_page extends AppCompatActivity {

    ImageView imageView;
    TextView t1;
    GoogleSignInOptions gso1;
    GoogleSignInClient gsc1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc1 = GoogleSignIn.getClient(this,gso1);

        t1 = findViewById(R.id.btnlogout);
        t1.setOnClickListener(view -> logout());
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        Uri photoUrl = signInAccount.getPhotoUrl();

        imageView = findViewById(R.id.profileImage);
        Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .circleCrop()
                .into(imageView);

        ((TextView) findViewById(R.id.TVtitlename)).setText(signInAccount.getGivenName());
        ((TextView) findViewById(R.id.TVusername)).setText(signInAccount.getEmail());
        ((EditText) findViewById(R.id.edtName)).setText(signInAccount.getGivenName());
        ((EditText) findViewById(R.id.edt_email)).setText(signInAccount.getEmail());

        DatabaseReference usersData = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = usersData.orderByChild("email").equalTo(signInAccount.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersnap : snapshot.getChildren())
                {
                    ((EditText) findViewById(R.id.edt_phone)).setText(usersnap.child("phone").getValue(String.class));
                    ((EditText) findViewById(R.id.edt_enr)).setText(usersnap.child("enr_no").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void logout()
    {
        gsc1.signOut().addOnCompleteListener(task -> {
          /*  SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("myStringKey");
            editor.apply();*/
            Intent intent = new Intent(this,page2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        });
    }
}