package com.example.test3;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "HIIIII", Toast.LENGTH_SHORT).show();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
        ArrayList<String> sub = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SubjectConnectsStudent");
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Scheduling");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    assert acct != null;
                    // Toast.makeText(studui.this, acct.getId() + " - "+ childSnapshot.child("student_id").getValue().toString(), Toast.LENGTH_SHORT).show();
                    if(Objects.equals(acct.getId(), Objects.requireNonNull(childSnapshot.child("student_id").getValue()).toString())){
                        sub.add(childSnapshot.child("subject_id").getValue().toString());
                        //Toast.makeText(studui.this, "hiii"+childSnapshot.child("subject_id").getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                for (String subjectId : sub) {
                    DatabaseReference subjectSchedulingRef = r.child(subjectId);
                    subjectSchedulingRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> timeList = new ArrayList<>();
                            // Loop through each push ID under the subject ID and retrieve scheduling data for each push ID
                            for (DataSnapshot pushIdSnapshot : dataSnapshot.getChildren()) {
                                String pushId = pushIdSnapshot.getKey();
                                String date = pushIdSnapshot.child("Date").getValue(String.class);
                                String time = pushIdSnapshot.child("Time").getValue(String.class);
                                String repetition = pushIdSnapshot.child("Repetition").getValue(String.class);
                                String description = pushIdSnapshot.child("description").getValue(String.class);

                                if(time != null) {
                                    String s = convertTimeTo24HourFormat(time);
                                    String[] t = s.split(":");

                                    // Set the time for the notification to be triggered (10 AM in this example)
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
                                    calendar.set(Calendar.MINUTE, Integer.parseInt(t[1]));
                                    calendar.set(Calendar.SECOND, 0);

                                    Intent i = new Intent(context, NotificationService.class);

                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);

                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                                    Toast.makeText(context, "Alarm scheduled successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        System.out.println("receiver");
    }

    public String convertTimeTo24HourFormat(String timeStr) {
        String convertedTime = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        try {
            Date date = inputFormat.parse(timeStr);
            convertedTime = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Toast.makeText(this, convertedTime + "happy", Toast.LENGTH_SHORT).show();
        return convertedTime;
    }
}
