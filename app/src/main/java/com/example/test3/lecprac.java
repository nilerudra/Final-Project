package com.example.test3;

import static okhttp3.Request.Builder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class lecprac extends AppCompatActivity {
    TextView t1, t2, t3;
    private Handler mHandler = new Handler();
    RadioGroup rdg;
    Dialog d1;
    String s = "Custom";
    EditText edt;
    TextView dtpick;
    AppCompatButton ap;
    RadioButton rd;
    String status;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecprac);

        FirebaseApp.initializeApp(this);

        edt = findViewById(R.id.cls);

        t2 = findViewById(R.id.crdate);

        ap = findViewById(R.id.save);
        ap.setOnClickListener(view -> sendingNotification());

        dtpick = findViewById(R.id.crdate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String formattedDateTime = new SimpleDateFormat("MMM/d/yyyy      hh:mm a", Locale.getDefault()).format(new Date(year - 1900, month, day, hour, minute));
        dtpick.setText(formattedDateTime);
        dtpick.setOnClickListener(view -> dtupd());


        d1 = new Dialog(this);
        d1.setContentView(R.layout.custom);
        rdg = d1.findViewById(R.id.rdgrp);

        t1 = findViewById(R.id.dnrepeat);
        t1.setOnClickListener(view -> setCustom());


    }

    public void dtupd() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // Show the DatePickerDialog to allow the user to select a date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) (view, year1, monthOfYear, dayOfMonth1) -> {

                    calendar.set(Calendar.YEAR, year1);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth1);

                    // Show the TimePickerDialog to allow the user to select a time
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (TimePickerDialog.OnTimeSetListener) (view1, hourOfDay, minute) -> {

                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                String formattedDateTime = new SimpleDateFormat("MMM/d/yyyy      hh:mm a", Locale.getDefault()).format(calendar.getTime());

                                // Update the text of the TextView or EditText with the selected date and time
                                dtpick.setText(formattedDateTime);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            DateFormat.is24HourFormat(this));
                    timePickerDialog.show();
                },
                year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void setCustom() {
        d1.show();
        rdg.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i != -1) {
                rd = rdg.findViewById(i);
                t1.setText(rd.getText());
                if (rd.getText().equals("Custom")) {
                    rdg.clearCheck();
                    int a = rdg.getChildCount();
                    if (a > 5) {
                        View childview = rdg.getChildAt(0);
                        rdg.removeView(childview);
                    }
                    Intent intent = new Intent(lecprac.this, custom.class);
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            }
            d1.hide();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            // setResult(2);
            s = "Repeats " + data.getStringExtra("1");
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            t1.setText(s);
            //finish();
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(s);
            radioButton.setId(View.generateViewId());
            rdg.addView(radioButton, 0);
            radioButton.setChecked(true);

        } else {
            t1.setText("Does not repeat");
        }
    }

    ArrayList<String> token = new ArrayList<>();
    long time;

    String body;
    public void savechngs() {
        String s1 = t2.getText().toString();
        String[] dt = s1.split("      ");
        String s2 = dt[0];
        String s3 = dt[1];

        body = "Lecture of " + mngtchclass.sub_name + " is scheduled on " + s2 + " at " + s3;

        time = convertDateTimeToMilliseconds(s2 + " " + s3);
        Intent intent = new Intent();
        intent.putExtra("1", edt.getText().toString());
        intent.putExtra("2", s2);
        intent.putExtra("3", s3);
        intent.putExtra("4", t1.getText().toString());
        setResult(1, intent);
        finish();

        DatabaseReference subjectConnectsStudentRef = FirebaseDatabase.getInstance().getReference().child("SubjectConnectsStudent");
        Query query = subjectConnectsStudentRef.orderByChild("subject_id").equalTo(mngtchclass.subId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String studentID = studentSnapshot.child("student_id").getValue(String.class);

                    System.out.println("Student ID :" + studentID);
                    assert studentID != null;
                    DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference().child("FCM Token");
                    Query query1 = tokensRef.orderByChild("student_id").equalTo(studentID);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot tokenSnapshot : dataSnapshot.getChildren()) {
                                String tokenID = tokenSnapshot.child("Token").getValue(String.class);
                                if(! token.contains(tokenID)) {
                                    token.add(tokenID);
                                }
                                // Use the tokenID as needed
                                System.out.println("Token ID: " + tokenID);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Error occurred while retrieving the token
                            System.out.println("Failed to retrieve token for Student ID " + studentID + ": " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while retrieving the student IDs
                System.out.println("Failed to retrieve student IDs: " + databaseError.getMessage());
            }
        });

        //Toast.makeText(this, "saved in DB", Toast.LENGTH_SHORT).show();
    }

    private void sendNotificationToStudent(String title, String message) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putStringArrayListExtra("tokens", token); // Pass the ArrayList of tokens
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        System.out.println(time);
        if (alarmManager != null) {
            long triggerTime = time - 5 * 60 * 1000; // Trigger after 5 minutes
            System.out.println(triggerTime);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    private Runnable sendNotification = new Runnable() {
        @Override
        public void run() {
            savechngs();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendNotificationToStudent("EduNexus", body);
                }
            }, 1000); // delay for 1000 milliseconds
        }
    };

    private void sendingNotification() {
        new Thread(sendNotification).start();
    }

    private long convertDateTimeToMilliseconds(String dateTimeString) {
        String pattern = "MMM/dd/yyyy hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());

        try {
            Date date = sdf.parse(dateTimeString);
            System.out.println(dateTimeString);
            System.out.println("done");
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any parsing exceptions
        }

        return -1; // Return a default value indicating an error
    }
}