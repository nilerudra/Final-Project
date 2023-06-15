package com.example.test3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class std_dashboard extends Fragment {

    TextView t,t2,t5,t6,t7;
    View view;
    TextView edt;
    ProgressBar progressBar;
    TextView progress,text1,text2;
    String stud_name;
    String stud_enr;
    public std_dashboard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setpro();
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setpro();
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_std_dashboard, container, false);

        t = view.findViewById(R.id.view_schdlp);
        t.setOnClickListener(view1 -> schlp());

        t6 = view.findViewById(R.id.asswrkstd);
        t6.setVisibility(View.GONE);

        t7 = view.findViewById(R.id.crtt);
        t7.setOnClickListener(view1 -> gotoAssact());

        t2 = view.findViewById(R.id.schdtskp);
        t2.setOnClickListener(view1 -> progress());

        text1 = view.findViewById(R.id.cls1);
        text1.setText(mngtchclass.sub_name);

        text2 = view.findViewById(R.id.clsdisc);
        text2.setText(mngtchclass.descp);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        edt = view.findViewById(R.id.asswrkstd);
        progress = view.findViewById(R.id.progress);

        t5 = view.findViewById(R.id.upldstmtudp);
        t5.setOnClickListener(view1 -> download());

        /*try {
            setpro();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error loading attendance", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }*/
        return view;
    }


    public void gotoAssact()
    {
        startActivity(new Intent(getActivity(),assign_activity.class));
    }
    public void download()
    {
        /*startActivity(new Intent(getActivity(),DownloadStdResources.class));*/
        startActivity(new Intent(getActivity(),uploadstdmaterial.class));
    }
    public void setpro() throws IOException {

        stud_name  = "";
        stud_enr = "";

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Users");

                r.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            UserInfo u = childSnapshot.getValue(UserInfo.class);
                            if(acct.getId().equals(u.getId())){
                                stud_name = u.getName();
                                stud_enr = u.getEnr_no();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        HashMap<String, Integer> attendanceData = new HashMap<>();
        attendanceData.put("1",0);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("attendance_files/")
                .child(mngtchclass.teacher_id + "/" + mngtchclass.sub_name + ".xlsx");


        File localFile = File.createTempFile("temp", ".xlsx");
        reference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            try {
                // Read the local file into a Workbook object
                FileInputStream inputStream = new FileInputStream(localFile);
                Workbook workbook = new HSSFWorkbook(inputStream);

                // Get the first sheet from the workbook
                Sheet sheet = workbook.getSheetAt(0);

                // Do something with the sheet object
                int enrollmentColIndex = 0; // Assuming enrollment number is in the first column
                int nameColIndex = 1; // Assuming name is in the second column



                // Loop through each row in the sheet, starting from the second row
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    // Get the enrollment number and name for this row
                    String enrollmentNo = row.getCell(enrollmentColIndex).getStringCellValue();
                    String name = row.getCell(nameColIndex).getStringCellValue();

                    // Initialize the attendance count for this student to zero
                    int attendanceCount = 0;

                    if(enrollmentNo.equals(stud_enr) && name.equals(stud_name)) {

                        // Loop through each column in the row, starting from the third column
                        for (int colIndex = 2; colIndex < row.getLastCellNum(); colIndex++) {

                            Cell cell = row.getCell(colIndex);
                            if(cell != null){
                                // If the cell value is "P", increment the attendance count for this student
                                if (cell.getStringCellValue().equalsIgnoreCase("P")) {
                                    attendanceCount++;
                                    // Store the attendance count for this student in the HashMap
                                    attendanceData.put("1", attendanceCount);
                                }
                            }
                        }
                    }
                }

                // Close the workbook and input stream
                workbook.close();
                inputStream.close();

                // Delete the local file
                localFile.delete();

                final int[] i = {0};
                String s = mngtchclass.hours;
                int total = Integer.parseInt(s);
                int present = attendanceData.get("1");
                present = present * 100;
                total = present/total;
                int a = total;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(i[0] <= a)
                        {
                            progress.setText(i[0] + "%");
                            progressBar.setProgress(i[0]);
                            i[0]++;
                            handler.postDelayed(this,22);
                        }

                    }
                }, 22);
                i[0] = 0;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).addOnFailureListener(exception -> {
            // Handle any errors that occur while downloading the file
            exception.printStackTrace();
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            setpro();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error loading attendance", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        progressBar.setProgress(0);
        progress.setText("0%");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressBar.setProgress(0);
        progress.setText("0%");
    }

    public void progress()
    {
        Intent intent = new Intent(getActivity(),schedule_test.class);
        startActivity(intent);
    }
    public void schlp()
    {
        startActivity(new Intent(getActivity(),lecpracschmng.class));
        //progressBar.setProgress(0);
    }
}