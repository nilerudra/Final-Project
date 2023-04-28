package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class custom extends AppCompatActivity {

    TextView t1;
    RadioButton rd1;
    AppCompatButton ap1,ap2,ap3,ap4,ap5,ap6,ap7,aps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        Spinner sp = findViewById(R.id.selocc);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repocc, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);

        //t1 = findViewById(R.id.cusdate);

        rd1 = findViewById(R.id.cusdate);
        View customview = LayoutInflater.from(this).inflate(R.layout.customdate, null);
        rd1.setCompoundDrawablesWithIntrinsicBounds(getDrawableFromView(customview), null, null, null);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedDateTime = new SimpleDateFormat("MMM/d/yyyy", Locale.getDefault()).format(new Date(year - 1900, month, day));
        /*dtpick.setText(day + "/" + (month + 1) + "/" + year + "     " + hour + ":" + minute);*/
        //t1.setText(formattedDateTime);

        ap1 = findViewById(R.id.s1);
        ap2 = findViewById(R.id.m);
        ap3 = findViewById(R.id.t1);
        ap4 = findViewById(R.id.w);
        ap5 = findViewById(R.id.t2);
        ap6 = findViewById(R.id.f);
        ap7 = findViewById(R.id.s2);

        ap1.setOnClickListener(buttonClickListener);
        ap2.setOnClickListener(buttonClickListener);
        ap3.setOnClickListener(buttonClickListener);
        ap4.setOnClickListener(buttonClickListener);
        ap5.setOnClickListener(buttonClickListener);
        ap6.setOnClickListener(buttonClickListener);
        ap7.setOnClickListener(buttonClickListener);

    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int buttonId = v.getId(); // Get the ID of the clicked button
            aps = findViewById(buttonId);


            ColorStateList backgroundTint = aps.getBackgroundTintList();

            if (backgroundTint != null) {
                int defaultColor = backgroundTint.getDefaultColor();
                if (defaultColor == Color.parseColor("#BABABA")) {
                    aps.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#946ACF")));
                }
                else
                {
                    aps.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BABABA")));
                }
            } else {
                aps.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BABABA")));
            }
        }
    };

    private Drawable getDrawableFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return new BitmapDrawable(getResources(), bitmap);
    }

}