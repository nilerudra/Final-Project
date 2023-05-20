package com.example.test3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Map;

public class Scan_QRCode extends Fragment {

    FirebaseDatabase database;
    DatabaseReference ref;
    String currentDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scan_qrcode, container, false);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        scanCode();
        return view;
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash up");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        boolean isBetween = false;
        /*Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = dateFormat.format(new Date());*/


        String r = result.getContents();
        String[] ar = r.split("_");
        String s = ar[0];


        try {
            // Parse the start time string
            Toast.makeText(requireContext(), "" + s, Toast.LENGTH_SHORT).show();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = inputFormat.parse(s);

            // Create the end time by adding 8 minutes to the start time
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(startTime);
            endTime.add(Calendar.MINUTE, 8);

            // Get the current time
            Calendar currentTime = Calendar.getInstance();

            // Extract individual components for comparison
            /*int currentYear = currentTime.get(Calendar.YEAR);
            int currentMonth = currentTime.get(Calendar.MONTH);
            int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);*/
            int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentTime.get(Calendar.MINUTE);
            //int currentSecond = currentTime.get(Calendar.SECOND);

            //Toast.makeText(requireContext(),currentHour + ":" + currentMinute + ":" + currentSecond, Toast.LENGTH_SHORT).show();

            /*int startYear = startTime.get(Calendar.YEAR);
            int startMonth = startTime.get(Calendar.MONTH);
            int startDay = startTime.get(Calendar.DAY_OF_MONTH);
            int startHour = startTime.get(Calendar.HOUR_OF_DAY);
            int startMinute = startTime.get(Calendar.MINUTE);
            int startSecond = startTime.get(Calendar.SECOND);*/

            /*int endYear = endTime.get(Calendar.YEAR);
            int endMonth = endTime.get(Calendar.MONTH);
            int endDay = endTime.get(Calendar.DAY_OF_MONTH);*/
            int endHour = endTime.get(Calendar.HOUR_OF_DAY);
            int endMinute = endTime.get(Calendar.MINUTE);
            //int endSecond = endTime.get(Calendar.SECOND);


            Toast.makeText(requireContext(), currentMinute + " - " + endMinute, Toast.LENGTH_LONG).show();

            // Check if the current time is within the range
            isBetween = currentMinute <= endMinute;

        } catch (ParseException e) {
            Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }




        if(result.getContents() != null && isBetween)
        {
            insertId(result.getContents());
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Done");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
        else
        {
            Toast.makeText(requireContext(), "Time Expired or Scan Valid QR Code " + isBetween, Toast.LENGTH_LONG).show();
        }
    });

    public void insertId(String s)
    {
        String[] ar = s.split("_");
        String lecNo = ar[0];
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
        assert acct != null;
        String id = acct.getId();

        DatabaseReference df = FirebaseDatabase.getInstance().getReference("Attendance").child(mngtchclass.sub_name);
        df.orderByKey().equalTo(lecNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    assert id != null;
                    df.child(lecNo).child(id).setValue("Present");
                } else {
                    df.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            assert id != null;
                            df.child(lecNo).child(id).setValue(id);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
}