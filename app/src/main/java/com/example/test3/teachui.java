package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class teachui extends AppCompatActivity {
        AppCompatButton ap,ap2;
        //TextView;
        LinearLayout l;
        Toolbar t;
        ImageView imageView;
        GoogleSignInOptions gso;
        GoogleSignInClient gsc;
        Dialog d;
        EditText e,des;
        FirebaseDatabase database;
        DatabaseReference ref;
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
                e = d.findViewById(R.id.cname);
                des = d.findViewById(R.id.cldesc);
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

                ap = findViewById(R.id.bt1);
                l = findViewById(R.id.cc);
                ap.setOnClickListener(view -> addcp());

                database = FirebaseDatabase.getInstance();
                ref = database.getReference("Subject");

                imageView.setOnClickListener(view -> show_profile());
                showClass();
               /* imageView.setOnClickListener(view -> openMenu());*/
        }

        private void show_profile() {
                Intent i = new Intent(teachui.this, Profile_page.class);
                startActivity(i);
        }

        //Tasks ts = new Tasks();
       /* Menuopt mno = new Menuopt();
        public void openMenu()
        {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.menuopt, mno)
                        .commit();
        }*/
        public void addcp() {
                d.show();
        }
        public void showClass()
        {
                // clear the LinearLayout first
                ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                l.removeAllViews();
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        // Get the class ID and create a new Class instance
                                        String classId = childSnapshot.getKey();
                                        Subject newClass = new Subject("", "", "", "");

                                        // Fill the values of the Class instance using the child values in the snapshot
                                        newClass.setName(childSnapshot.child("name").getValue(String.class));
                                        newClass.setDescription(childSnapshot.child("description").getValue(String.class));
                                        newClass.setTeacher_id(childSnapshot.child("teacher_id").getValue(String.class));
                                        newClass.setSubject_id(childSnapshot.child("subject_id").getValue(String.class));

                                        if(newClass.teacher_id.equals(getIntent().getStringExtra("id"))){
                                                addClass(newClass.getName(), newClass.getDescription(), newClass.subject_id);
                                        }
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                });
        }

        public void addClassToDatabase(){
                String teacher_id = getIntent().getStringExtra("id");
                String name = e.getText().toString();
                String description = des.getText().toString();
                String subject_id = teacher_id + "_" + name;

                Subject sub = new Subject(name,description,teacher_id,subject_id);
                ref.push().setValue(sub);

                e.setText("");
                des.setText("");
        }

        public void addClass(String name, String description, String sub_id){
                TextView ed = new TextView(teachui.this);
                ed.setText(String.format("%s\n\n%s",name,description));
                ed.setBackgroundResource(R.drawable.fortui);
                ed.setTextSize(26);
                ed.setTextColor(ed.getContext().getColor(R.color.white));
                ed.setTextAppearance(this, R.style.AppTheme);
                ed.setPadding(40, 25, 40, 100);
                ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ed.getLayoutParams();
                int leftMargin = 20;
                int topMargin = 20;
                int rightMargin = 20;
                int bottomMargin = 0;
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                ed.setLayoutParams(layoutParams);
                ed.setOnClickListener(view ->mngPage(name, sub_id, description));
                l.addView(ed);
                d.hide();
        }

        public void classadd() {
                //for creating a excel file to store attendance of students
                generateExcelFile(e.getText().toString());
                addClass(e.getText().toString(), des.getText().toString(), getIntent().getStringExtra("id")+"_"+e.getText().toString());
                addClassToDatabase();
        }

        public void mngPage(String name, String sub_id, String desc){
                Intent i = new Intent(teachui.this, mngtchclass.class);
                i.putExtra("sub_id", sub_id);
                i.putExtra("name",name);
                i.putExtra("dsc",desc);
                startActivity(i);
        }

        private void generateExcelFile(String name)
        {
                Workbook workbook = new HSSFWorkbook();
                // Create a new sheet
                Sheet sheet = workbook.createSheet("My Sheet");

                // Create the header row with column names
                Row headerRow = sheet.createRow(0);
                Cell enrNoCell = headerRow.createCell(0);
                enrNoCell.setCellValue("Enrollment No.");

                Cell nameCell = headerRow.createCell(1);
                nameCell.setCellValue("Name");

                // Create columns for each day of the month
                /*int numDays = 30; // Or use Calendar.getActualMaximum(Calendar.DAY_OF_MONTH) to get the actual number of days in the month
                for (int i = 0; i < numDays; i++) {
                        Cell dayCell = headerRow.createCell(i + 2);
                        dayCell.setCellValue("Day " + (i + 1));
                }*/

                // Write the workbook to a file
                // Add data rows for each student
                // Replace this with your own logic to populate the data
                /*for (int i = 1; i <= 10; i++) {
                        Row dataRow = sheet.createRow(i);

                        Cell enrNoDataCell = dataRow.createCell(0);
                        enrNoDataCell.setCellValue("Enrollment No. " + i);

                        Cell nameDataCell = dataRow.createCell(1);
                        nameDataCell.setCellValue("Student " + i);

                        for (int j = 0; j < numDays; j++) {
                                Cell dayDataCell = dataRow.createCell(j + 2);
                                // Replace this with your own logic to populate attendance data
                                dayDataCell.setCellValue("Present");
                        }
                }*/

                // Write the workbook to a file
                File file = new File(getExternalFilesDir(null), name + ".xlsx");
                FileOutputStream outputStream = null;
                try {
                        outputStream = new FileOutputStream(file);
                        workbook.write(outputStream);
                        outputStream.close();
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }
}