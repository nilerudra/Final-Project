package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class studui extends AppCompatActivity {

    AppCompatButton ap,ap2;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView imageView;
    Dialog d;
    EditText e;
    int i = 0;
    LinearLayout l;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> ls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studui);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        Uri photoUrl = signInAccount.getPhotoUrl();

        imageView = findViewById(R.id.imgpro);
        Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .circleCrop()
                .into(imageView);

        l = findViewById(R.id.cc);

        d = new Dialog(this);
        d.setContentView(R.layout.joinclass);
        e = d.findViewById(R.id.clscode);
        ap2 = d.findViewById(R.id.join);
        ap2.setOnClickListener(view -> join());

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Subject");

        ap = findViewById(R.id.joinclass);
        ap.setOnClickListener(view -> joinClass());
        ls = new ArrayList();
        loadSubjects();

    }

    public void loadSubjects(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Subject");
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("SubjectConnectsStudent");
        Query query = r.orderByChild("student_id").equalTo(getIntent().getStringExtra("id"));


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ls.clear();
                l.removeAllViews();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String subId = childSnapshot.child("subject_id").getValue(String.class);
                    ls.add(subId);
                    Log.w("Firebase", "" + subId);
                    // Do something with the sub ID
                }
                for(int i = 0; i < ls.size(); i++){
                    int finalI = i;
                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if(Objects.equals(childSnapshot.child("subject_id").getValue(String.class), ls.get(finalI))){
                                    String name = childSnapshot.child("name").getValue(String.class);
                                    String desc = childSnapshot.child("description").getValue(String.class);
                                    addClass(name,desc,ls.get(finalI));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    public void checkSubject(String sub_code){
        ref.orderByChild("subject_id").equalTo(sub_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    connectWithSubject(sub_code);
                }else{
                    Toast.makeText(studui.this, "Enter Correct Code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }

    public void connectWithSubject(String subject_id){
        String stud_id = getIntent().getStringExtra("id");
        Map<String, Object> data = new HashMap<>();
        data.put("student_id", stud_id);
        data.put("subject_id", subject_id);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("SubjectConnectsStudent").push().setValue(data);

        Toast.makeText(studui.this, "Subject Added", Toast.LENGTH_SHORT).show();
        e.setText("");

        loadSubjects();
    }

    public void join(){
        String s = e.getText().toString().trim();
        checkSubject(s);
    }

    public void addClass(String name, String description, String sub_id){
        TextView ed = new TextView(studui.this);
        ed.setText(String.format("%s\n\n%s",name,description));
        ed.setBackgroundResource(R.drawable.fortui);
        ed.setTextSize(20);
        ed.setPadding(40, 25, 40, 150);
        ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ed.getLayoutParams();
        int leftMargin = 20;
        int topMargin = 20;
        int rightMargin = 20;
        int bottomMargin = 0;
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        ed.setLayoutParams(layoutParams);
        ed.setOnClickListener(view -> mngPage(name, sub_id));
        l.addView(ed);
        d.hide();
    }

    public void joinClass()
    {
        d.show();
    }

    public void mngPage(String name, String sub_id){
        Intent i = new Intent(studui.this, mngtchclass.class);
        i.putExtra("sub_id", sub_id);
        i.putExtra("name",name);
        startActivity(i);
    }
}