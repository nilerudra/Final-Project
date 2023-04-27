package com.example.test3;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class lecprac extends AppCompatActivity {

    TextView dtpick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecprac);

        dtpick = findViewById(R.id.crdate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String formattedDateTime = new SimpleDateFormat("MMM/d/yyyy      hh:mm a", Locale.getDefault()).format(new Date(year - 1900, month, day, hour, minute));
        /*dtpick.setText(day + "/" + (month + 1) + "/" + year + "     " + hour + ":" + minute);*/
        dtpick.setText(formattedDateTime);
        dtpick.setOnClickListener(view -> dtupd());

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

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

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