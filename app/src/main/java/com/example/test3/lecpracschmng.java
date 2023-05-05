package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;

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
        loadFromDB();
    }

    public void nxtpg()
    {

        Intent intent = new Intent(lecpracschmng.this,lecprac.class);

        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Scheduling").child(mngtchclass.subId).push();

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

            reference.child("description").setValue(value);
            reference.child("Date").setValue(date);
            reference.child("Time").setValue(time);
            reference.child("Repetition").setValue(occurence);

            /*if(occurence.equals("Every day")) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                String da = convertTimeTo24HourFormat(time);
                Toast.makeText(lecpracschmng.this, da, Toast.LENGTH_SHORT).show();

                String[] tm = da.split(":");
                // Set the time for the notification to be triggered (10 AM in this example)
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tm[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(tm[1]));
                calendar.set(Calendar.SECOND, 0);


                Intent i = new Intent(lecpracschmng.this, BroadcastReceiver.class);
                i.putExtra("sub_name", mngtchclass.sub_name);

                *//*PendingIntent pi = PendingIntent.getBroadcast(lecpracschmng.this, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);*//*
            }*/
            loadFromDB();
        }

    }

    public static String convertTimeTo24HourFormat(String time) {

        // create a DateTimeFormatter for parsing the input string
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("h:mm a");

// create a DateTimeFormatter for formatting the output string
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("HH:mm");

// parse the input string and format it to 24-hour format
        LocalTime time24 = LocalTime.parse(time, inputFormat);
        String time24String = time24.format(outputFormat);
        return time24String;
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

    public void loadFromDB(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Scheduling").child(mngtchclass.subId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                l1.removeAllViews();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String des = childSnapshot.child("description").getValue().toString();
                    String date = childSnapshot.child("Date").getValue().toString();
                    String time = childSnapshot.child("Time").getValue().toString();
                    String repetition = childSnapshot.child("Repetition").getValue().toString();

                    addClass(des,date,time,repetition);
                    // Do something with the sub ID
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

}