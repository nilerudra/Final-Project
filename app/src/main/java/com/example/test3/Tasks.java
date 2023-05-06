package com.example.test3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.*;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.test3.R;

public class Tasks extends Fragment {

    TextView t,t2,t3,t4;
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

    public void schlp()
    {
        //return view.findViewById(R.id.schdlp);
        startActivity(new Intent(getActivity(),lecpracschmng.class));
    }
}