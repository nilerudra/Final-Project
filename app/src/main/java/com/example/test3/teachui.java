package com.example.test3;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class teachui extends AppCompatActivity {
        AppCompatButton ap,ap2;
        //TextView;
        LinearLayout l;
        int i = 0;
        Toolbar t;
        ImageView imageView;
        GoogleSignInOptions gso;
        GoogleSignInClient gsc;
        Dialog d;
        EditText e;
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
                startActivity(new Intent(teachui.this,lecpracschmng.class));
                //d.show();
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

        public void classadd() {
                e = d.findViewById(R.id.cname);
                String s = e.getText().toString();
                TextView ed = new TextView(teachui.this);
                ed.setText(s);
                i++;
                ed.setBackgroundResource(R.drawable.fortui);
                ed.setTextSize(26);
                ed.setTextAppearance(this, R.style.AppTheme);
                ed.setPadding(40, 25, 40, 150);
                ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ed.getLayoutParams();
                int leftMargin = 20;
                int topMargin = 20;
                int rightMargin = 20;
                int bottomMargin = 0;
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                ed.setLayoutParams(layoutParams);
                l.addView(ed);
                d.hide();
        }
}
