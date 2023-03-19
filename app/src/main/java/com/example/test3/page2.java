package com.example.test3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.auth.api.signin.GoogleSignInClient.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

/*public class page2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
    }
}*/
public class page2 extends AppCompatActivity {

//    private EditText taskNameEditText;
//    private EditText taskDescriptionEditText;
//    private EditText taskDateEditText;
//    private EditText taskTimeEditText;
//    private ImageView datePickerImageView;
//    private ImageView timePickerImageView;
//    private Button scheduleButton;
//
//    private int mYear, mMonth, mDay, mHour, mMinute;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        TextView t1 = (TextView) findViewById(R.id.edt1);
        TextView t2 = (TextView) findViewById(R.id.edt2);

        MaterialButton m = (MaterialButton) findViewById(R.id.login);

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t1.getText().toString().equals("Jayesh Patil") && t2.getText().toString().equals("gamer123"))
                    Toast.makeText(page2.this, "Login successful!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(page2.this, "Login failed!", Toast.LENGTH_SHORT).show();

            }

        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            navigateToSecondActivity();
        }

        iv = findViewById(R.id.ll);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
    }

    void signin()
    {
        Intent is = gsc.getSignInIntent();
        startActivityForResult(is,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void navigateToSecondActivity()
    {
        finish();
        Intent in = new Intent(page2.this,page3.class);
        startActivity(in);
    }
}
//
//
//
//        datePickerImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Show the date picker
//                showDatePicker();
//            }
//        });
//
//        timePickerImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Show the time picker
//                showTimePicker();
//            }
//        });
//
//        scheduleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Save the task
//                saveTask();
//            }
//        });
//    }
//
//    private void saveTask() {
//    }
//
//    private void showDatePicker() {
//        // Get the current date
//        final Calendar c = Calendar.getInstance();
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        mDay = c.get(Calendar.DAY_OF_MONTH);
//
//        // Launch the date picker dialog
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        taskDateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }
//
//    private void showTimePicker() {
//        // Get the current time
//        final Calendar c = Calendar.getInstance();
//        mHour = c.get(Calendar.HOUR_OF_DAY);
//        mMinute = c.get(Calendar.MINUTE);
//
//        // Launch the time picker dialog
//        //TimePickerDialog timePickerDialog = new TimePickerDialog();
//    }
//}