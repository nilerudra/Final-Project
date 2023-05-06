package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class schdl_test extends AppCompatActivity {

    TextView t1,t2;
    Dialog d1;
    String s = "Custom";
    EditText edt,edt1;
    TextView dtpick;
    AppCompatButton ap;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schdl_test);

        edt = findViewById(R.id.testdisc);
        edt1 = findViewById(R.id.marks);

        t2 = findViewById(R.id.tstdate);

        ap = findViewById(R.id.save_time);
        ap.setOnClickListener(view -> savechngs());

        dtpick = findViewById(R.id.tstdate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String formattedDateTime = new SimpleDateFormat("MMM/d/yyyy      hh:mm a", Locale.getDefault()).format(new Date(year - 1900, month, day, hour, minute));
        dtpick.setText(formattedDateTime);
        dtpick.setOnClickListener(view -> dtupd());
    }

    public void savechngs()
    {
        String s1 = t2.getText().toString();
        String[] dt = s1.split("      ");
        String s2 = dt[0];
        String s3 = dt[1];

        Intent intent = new Intent();
        intent.putExtra("1",edt.getText().toString());
        intent.putExtra("2",s2);
        intent.putExtra("3",s3);
        intent.putExtra("4",edt1.getText().toString());
        setResult(6, intent);
        finish();

        Toast.makeText(this, "saved in DB", Toast.LENGTH_SHORT).show();
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
}