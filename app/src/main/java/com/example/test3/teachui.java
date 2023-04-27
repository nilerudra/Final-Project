package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.ValueEventListener;


public class teachui extends AppCompatActivity {
        AppCompatButton ap,ap2;
        //TextView;
        LinearLayout l;
        Toolbar t;
        ImageView imageView;
        GoogleSignInOptions gso;
        GoogleSignInClient gsc;
        Dialog d;
        EditText e,des;
        FirebaseDatabase database;
        DatabaseReference ref;
        //RelativeLayout.LayoutParamsparams;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_teachui);
                t = findViewById(R.id.toolbar);
                setSupportActionBar(t);
//getSupportActionBar().setTitle(null);

                d = new Dialog(this);
                d.setContentView(R.layout.addclass);
                ap2 = d.findViewById(R.id.bt1);
                e = d.findViewById(R.id.cname);
                des = d.findViewById(R.id.cldesc);
                ap2.setOnClickListener(view -> classadd());

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
/*else
{
Stringinitials=getInitials(signInAccount.getDisplayName());
TextDrawabledrawable=TextDrawable.builder().buildRound(initials,Color.GRAY);
imageView.setImageDrawable(drawable);

}*/
                ap = findViewById(R.id.bt1);
                l = findViewById(R.id.cc);
                ap.setOnClickListener(view -> addcp());

                database = FirebaseDatabase.getInstance();
                ref = database.getReference("Subject");

                showClass();
        }

        public void addcp() {
                /*TextView ed = new TextView(teachui.this);
                ed.setText("This is newly added" + i);
                i++;
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
                l.addView(ed);*/
                d.show();
        }
/*privateStringgetInitials(Stringname){
String[]parts=name.split("");
StringBuilderinitials=newStringBuilder();
for(Stringpart:parts){
if(!part.isEmpty()){
initials.append(part.charAt(0));
}
}
returninitials.toString().toUpperCase();
}*/
        public void showClass(){
                // clear the LinearLayout first
                ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                l.removeAllViews();
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        // Get the class ID and create a new Class instance
                                        String classId = childSnapshot.getKey();
                                        Subject newClass = new Subject("", "", "", "");

                                        // Fill the values of the Class instance using the child values in the snapshot
                                        newClass.setName(childSnapshot.child("name").getValue(String.class));
                                        newClass.setDescription(childSnapshot.child("description").getValue(String.class));
                                        newClass.setTeacher_id(childSnapshot.child("teacher_id").getValue(String.class));
                                        newClass.setSubject_id(childSnapshot.child("subject_id").getValue(String.class));

                                        if(newClass.teacher_id.equals(getIntent().getStringExtra("id"))){
                                                addClass(newClass.getName(), newClass.getDescription());
                                        }
                                        else{
                                                Toast.makeText(teachui.this,"hello",Toast.LENGTH_SHORT).show();
                                        }
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                });
        }

        public void addClassToDatabase(){
                String teacher_id = getIntent().getStringExtra("id");
                String name = e.getText().toString();
                String description = des.getText().toString();
                String subject_id = teacher_id + "_" + name;

                Subject sub = new Subject(name,description,teacher_id,subject_id);
                ref.push().setValue(sub);

                e.setText("");
                des.setText("");
        }

        public void addClass(String name, String description){
                TextView ed = new TextView(teachui.this);
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
                ed.setOnClickListener(view ->mngPage());
                l.addView(ed);
                d.hide();
        }

        public void classadd() {
                addClass(e.getText().toString(), des.getText().toString());
                addClassToDatabase();
        }

        public void mngPage(){
                Intent i = new Intent(teachui.this, mngtchclass.class);
                startActivity(i);
        }
}
