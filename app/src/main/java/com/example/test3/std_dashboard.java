package com.example.test3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class std_dashboard extends Fragment {

    TextView t,t2;
    View view;
    EditText edt;
    ProgressBar progressBar;
    int i = 0;
    TextView progress,text1,text2;
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
        //schlp();
        t = view.findViewById(R.id.view_schdlp);
        t.setOnClickListener(view1 -> schlp());

        t2 = view.findViewById(R.id.schdtskp);
        t2.setOnClickListener(view1 -> progress());

        text1 = view.findViewById(R.id.cls1);
        text1.setText(mngtchclass.sub_name);

        text2 = view.findViewById(R.id.clsdisc);
        text2.setText(mngtchclass.descp);

        progressBar = view.findViewById(R.id.progressBar);

        edt = view.findViewById(R.id.asswrkstd);
        progress = view.findViewById(R.id.progress);

        setpro();
        return view;
    }

    public void setpro()
    {
        int a = 75;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(i <= a)
                {
                    progress.setText(i + "%");
                    progressBar.setProgress(i);
                    i++;
                    handler.postDelayed(this,22);
                }

            }
        }, 22);
        i = 0;
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