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

    TextView t,sub_name;
    View view;
    public Tasks(){
        // require a empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tasks, container, false);
        //schlp();
        t = view.findViewById(R.id.schdlp);
        sub_name = view.findViewById(R.id.cls);
        t.setOnClickListener(view1 -> schlp());
        sub_name.setText(mngtchclass.subName);
        return view;
    }

    public void schlp()
    {
        //return view.findViewById(R.id.schdlp);
        startActivity(new Intent(getActivity(),lecpracschmng.class));
    }
}