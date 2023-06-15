package com.example.test3;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.test3.R;

public class Tasks extends Fragment {

    TextView t,t2,t3,t4,t5,t6,t7;
    View view;

    public Tasks(){
        // require a empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tasks, container, false);
        //schlp();
        t = view.findViewById(R.id.schdlp);
        t.setOnClickListener(view1 -> schlp());

        t7 = view.findViewById(R.id.asswrk);
        t7.setVisibility(View.GONE);

        t6 = view.findViewById(R.id.classid);
        t6.setText(mngtchclass.subId);

        t6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String textToCopy = t6.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getActivity().getApplicationContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();

                return true;
            }
        });


        t5 = view.findViewById(R.id.clsname);
        t5.setText(mngtchclass.sub_name);

        t4 = view.findViewById(R.id.crtt);
        t4.setOnClickListener(view1 -> assignactvt());

        t3 = view.findViewById(R.id.schdtsk);
        t3.setOnClickListener(view1 -> schetest());

        t2 = view.findViewById(R.id.upldstmt);
        t2.setOnClickListener(view1 -> upldstdwrk());
        return view;
    }

    public void assignactvt()
    {
        startActivity(new Intent(getActivity(),assign_activity.class));
    }
    public void schetest()
    {
        startActivity(new Intent(getActivity(),schedule_test.class));
    }
    private void upldstdwrk() {
        startActivity(new Intent(getActivity(),uploadstdmaterial.class));
    }
    public void schlp(){ startActivity(new Intent(getActivity(),lecpracschmng.class)); }
}