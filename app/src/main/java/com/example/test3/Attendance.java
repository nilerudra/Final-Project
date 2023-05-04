package com.example.test3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Attendance extends Fragment {

    private TextView qrCodeTV;
    private ImageView qrCodeIV;
    private TextInputEditText dataEdt;
    private Button generateQRBtn, getFile;
    public static String data;
    List<String> id,name;

    public Attendance(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        qrCodeTV = view.findViewById(R.id.idTVGenarateQR);
        qrCodeIV = view.findViewById(R.id.idIVQRCode);
        dataEdt = view.findViewById(R.id.idEdtData);
        generateQRBtn = view.findViewById(R.id.idBtnGenerateQR);
        getFile = view.findViewById(R.id.GetAttendance);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = year + "-" + month + "-" + day;

        dataEdt.setText(currentDate + "_" + mngtchclass.subId);
        dataEdt.setEnabled(false);

        getFile.setOnClickListener(View -> getData());
        generateQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = dataEdt.getText().toString();
                if(data.isEmpty())
                {
                    Toast.makeText(getContext(), "Please enter some data to generate QR Code..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int dimen = width<height ? width:height;
                    dimen = dimen * 3/4;

                    //Generating QR Code
                    QRCodeWriter writer = new QRCodeWriter();

                    //passing Contents to encode in QR Code
                    BitMatrix bit;
                    try {
                        bit = writer.encode(data, BarcodeFormat.QR_CODE,dimen,dimen);

                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }
                    //convert bitmatrix into image
                    int w = bit.getWidth();
                    int h = bit.getHeight();
                    int[] pixels = new int[w*h];
                    for(int y = 0 ; y<h ; y++)
                    {
                        int offset = y*w;
                        for(int x = 0; x<w ; x++)
                        {
                            pixels[offset + x] = bit.get(x,y) ? Color.BLACK : Color.WHITE;
                        }
                    }
                    Bitmap bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
                    bitmap.setPixels(pixels,0,w,0,0,w,h);
                    //displaying QR code on screen
                    qrCodeIV.setImageBitmap(bitmap);
                    qrCodeTV.setVisibility(View.GONE);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void getData(){
        id = new ArrayList<>();
        name = new ArrayList<>();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users");

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    id.add(childSnapshot.child("id").getValue().toString());
                    name.add(childSnapshot.child("name").getValue().toString());}
                ShowFile();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowFile() {


        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance");
        Row headerRow = sheet.createRow(0);
        Cell idHeaderCell = headerRow.createCell(0);
        idHeaderCell.setCellValue("Student ID");
        Cell nameHeaderCell = headerRow.createCell(1);
        nameHeaderCell.setCellValue("Student Name");

        // Add attendance data rows
        for (int i = 0; i < id.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell idCell = row.createCell(0);
            idCell.setCellValue(id.get(i));
            Cell nameCell = row.createCell(1);
            nameCell.setCellValue(name.get(i));
        }

        // Save Excel sheet to file
        File file = new File(getContext().getExternalFilesDir(null), "attendance.xls");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(getContext(), "Attendance file saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to save attendance file.", Toast.LENGTH_LONG).show();
        }
    }
}
