package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class lecpracschmng extends AppCompatActivity {
    AppCompatButton ap;
    LinearLayout l1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecpracschmng);

        l1 = findViewById(R.id.schdlecprac);

        ap = findViewById(R.id.edit);
        ap.setOnClickListener(view -> nxtpg());
    }

    public void nxtpg()
    {

        Intent intent = new Intent(lecpracschmng.this,lecprac.class);

        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value = "value not get";
        String time = "nope";
        String date = "nope";
        String occurence = "Occured";
        if (resultCode == 1) {
            Toast.makeText(this, "entered occurence", Toast.LENGTH_SHORT).show();
            value = data.getStringExtra("1");
            date = data.getStringExtra("2");
            time = data.getStringExtra("3");
            occurence = data.getStringExtra("4");
            addClass(value,date,time,occurence);
        }

    }


    public void addClass(String name,String date, String description, String sub_id){
        TextView ed = new TextView(lecpracschmng.this);
        ed.setText(String.format("%s\n%s\n%s\n%s",name,date,description,sub_id));
        ed.setBackgroundResource(R.drawable.fortui);
        ed.setTextSize(15);
        ed.setTextColor(Color.WHITE);
        ed.setTextAppearance(this, R.style.AppTheme);
        ed.setPadding(40, 25, 40, 100);
        ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ed.getLayoutParams();
        int leftMargin = 20;
        int topMargin = 20;
        int rightMargin = 20;
        int bottomMargin = 30;
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        ed.setLayoutParams(layoutParams);
        //ed.setOnClickListener(view ->mngPage(name, sub_id));
        l1.addView(ed);
    }

}