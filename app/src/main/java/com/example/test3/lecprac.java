package com.example.test3;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class lecprac extends AppCompatActivity {
    TextView t1;
    RadioGroup rdg;
    Dialog d1;
    TextView dtpick;
    RadioButton rd;
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

    public void setCustom()
    {
        d1.show();
            rdg.setOnCheckedChangeListener((radioGroup, i) -> {
                String s;
                rd = rdg.findViewById(i);
                t1.setText(rd.getText());
                /*if (i == -1) {
                    // Handle the case where no RadioButton is selected
                    s = "No radio button selected";
                } else {
                    // Get the selected RadioButton object
                    rd = rdg.findViewById(i);
                    if (rd == null) {
                        // Handle the case where the RadioButton is not found
                        s = "RadioButton not found";
                    } else {
                        s = rd.getText().toString();
                        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }


               // Toast.makeText(lecprac.this, rd.getText().toString(), Toast.LENGTH_SHORT).show();

                if (rd != null) {
                    // Get the text of the selected RadioButton
                    s = rd.getText().toString();
                    Toast.makeText(lecprac.this, "hiiii", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the selectedRadioButton is null
                    s = "No radio button selected";
                }

                //String s = rd.getText().toString();
                t1.setText(s);*/
            if(rd.getText().equals("Custom"))
            {
                startActivity(new Intent(lecprac.this,custom.class));
            }

                d1.hide();
            });
    }

}