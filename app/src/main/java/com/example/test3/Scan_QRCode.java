package com.example.test3;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Scan_QRCode extends Fragment {

    FirebaseDatabase database;
    DatabaseReference ref;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    String currentDate = year + "-" + month + "-" + day;
    @Nullable
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
        if(result.getContents().contains(currentDate + "_"))
        {
            insertid();
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
            Toast.makeText(requireContext(), "Scan Valid QR Code", Toast.LENGTH_SHORT).show();
        }
    });

    public void insertid()
    {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
        String id = acct.getId();

        Map<String, Object> data = new HashMap<>();
        data.put("student_id", ""+id);

        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        df.child("Attendence").child(currentDate).setValue(data);
    }
}