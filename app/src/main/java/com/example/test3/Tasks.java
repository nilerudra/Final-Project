package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.*;
import androidx.fragment.app.Fragment;

import com.example.test3.R;

public class Tasks extends Fragment {

    TextView t,t2;
    View view;
    public Tasks(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tasks, container, false);
        //schlp();
        t = view.findViewById(R.id.schdlp);
        t.setOnClickListener(view1 -> schlp());

        t2 = view.findViewById(R.id.upldstmt);
        t2.setOnClickListener(view1 -> upldstdwrk());
        return view;
    }

    private void upldstdwrk() {
       // startActivity(new Intent(getActivity(),uploadstudymaterial.class));
    }

    public void schlp()
    {
        //return view.findViewById(R.id.schdlp);
        startActivity(new Intent(getActivity(),lecpracschmng.class));
    }
}