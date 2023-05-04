package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class custom extends AppCompatActivity {

    EditText edt,edt1,edt2;
    RadioButton rd1,rd2;
    RadioGroup rdgp;
    Spinner s1;
    String s = "NO value";
    String[] weekdays = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    String tem = " ",check;
    LinearLayout l1;
    int[] i = {0,0,0,0,0,0,0};
    AppCompatButton ap1,ap2,ap3,ap4,ap5,ap6,ap7,aps,apsave;
    TextView txtv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        apsave = findViewById(R.id.save1);
        apsave.setOnClickListener(view -> savechngs());
        Spinner sp = findViewById(R.id.selocc);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repocc, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        edt1 = findViewById(R.id.occ);
        sp.setAdapter(adapter);

        s1 = findViewById(R.id.selocc);
        l1 = findViewById(R.id.chswk);
        txtv = findViewById(R.id.repon);
        rdgp = findViewById(R.id.endson);
        edt2 = findViewById(R.id.aftocc);
        rdgp.check(R.id.never);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(s1.getSelectedItem().equals("day"))
                {
                     l1.setVisibility(View.GONE);
                     txtv.setVisibility(View.GONE);
                } else if (s1.getSelectedItem().equals("week")) {
                    l1.setVisibility(View.VISIBLE);
                    txtv.setVisibility(View.VISIBLE);
                }
                else if(s1.getSelectedItem().equals("month"))
                {
                    l1.setVisibility(View.GONE);
                    txtv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(s1.getSelectedItem().equals("day"))
                {
                    l1.setVisibility(View.GONE);
                    txtv.setVisibility(View.GONE);
                }
            }
        });

        edt = findViewById(R.id.aftocc);
        edt.setOnClickListener(view -> setrdbtn());
        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                RadioGroup radioGroup = findViewById(R.id.endson);
                RadioButton radioButton = findViewById(R.id.cusocc);
                radioGroup.check(radioButton.getId());
            }
        });

        rd1 = findViewById(R.id.cusdate);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedDateTime = new SimpleDateFormat("MMM/d/yyyy", Locale.getDefault()).format(new Date(year - 1900, month, day));
        rd1.setText("On " + formattedDateTime);
        rd1.setOnClickListener(view -> dtupdths());

        ap1 = findViewById(R.id.w0);
        ap2 = findViewById(R.id.w1);
        ap3 = findViewById(R.id.w2);
        ap4 = findViewById(R.id.w3);
        ap5 = findViewById(R.id.w4);
        ap6 = findViewById(R.id.w5);
        ap7 = findViewById(R.id.w6);

        ap1.setOnClickListener(buttonClickListener);
        ap2.setOnClickListener(buttonClickListener);
        ap3.setOnClickListener(buttonClickListener);
        ap4.setOnClickListener(buttonClickListener);
        ap5.setOnClickListener(buttonClickListener);
        ap6.setOnClickListener(buttonClickListener);
        ap7.setOnClickListener(buttonClickListener);

    }

    public void dtupdths() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth1) -> {

                    calendar.set(Calendar.YEAR, year1);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth1);

                    String formattedDateTime = new SimpleDateFormat("MMM/d/yyyy", Locale.getDefault()).format(calendar.getTime());
                    rd1.setText("On " + formattedDateTime);
                },
                year, month, dayOfMonth);
        datePickerDialog.show();
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int buttonId = v.getId();
            int spos = 0;
            aps = findViewById(buttonId);

            switch (v.getId())
            {
                case R.id.w0: spos = 0; break;
                case R.id.w1: spos = 1; break;
                case R.id.w2: spos = 2; break;
                case R.id.w3: spos = 3; break;
                case R.id.w4: spos = 4; break;
                case R.id.w5: spos = 5; break;
                case R.id.w6: spos = 6; break;
            }


            ColorStateList backgroundTint = aps.getBackgroundTintList();

            if (backgroundTint != null) {
                int defaultColor = backgroundTint.getDefaultColor();
                if (defaultColor == Color.parseColor("#EAEAEA")) {
                    aps.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#946ACF")));
                    i[spos] = 1;
                }
                else
                {
                    aps.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EAEAEA")));
                    i[spos] = 0;
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

    public void setrdbtn()
    {
        RadioGroup radioGroup = findViewById(R.id.endson);
        RadioButton radioButton = findViewById(R.id.cusocc);
        radioGroup.check(radioButton.getId());
    }

    public void savechngs()
    {
        /*day</item>
        <item>week</item>
        <item>month*/
        int a = rdgp.getCheckedRadioButtonId();
        rd2 = findViewById(a);
        String srd = rd2.getText().toString();
        check = rd1.getText().toString();

        if(srd.equals("Never")) {
            if (s1.getSelectedItem().equals("day")) {
                if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))
                    s = "every" + " day";
                else
                    s = "every " + edt1.getText().toString() + " day";
            } else if (s1.getSelectedItem().equals("week")) {
                for (int k = 0; k < i.length; k++) {
                    if (i[k] == 1)
                        tem = tem + " " + weekdays[k];
                }

                if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) && tem.equals(" ")) {
                    s = "weekly";
                } else if (tem.equals(" ")) {
                    s = "every " + edt1.getText().toString() + " weeks";
                } else if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) {
                    s = "weekly on" + tem;
                } else {
                    s = "every " + edt1.getText().toString() + " weeks on" + tem;
                }
            } else if (s1.getSelectedItem().equals("month")) {
            /*for (int k = 0;k < i.length;k++) {
                if(i[k] == 1)
                    tem = tem + " " +weekdays[k];
            }*/

                if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))) {
                    s = "monthly";
                } else {
                    s = "every " + edt1.getText().toString() + " months";
                }
            }
        }

        else if(srd.equals("After occurence"))
        {
            if (edt2.getText().toString().equals("") || edt2.getText().toString().equals("1")) {
                if (s1.getSelectedItem().equals("day")) {
                    if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))
                        s = "every" + " day; Once";
                    else
                        s = "every " + edt1.getText().toString() + " day; Once";
                } else if (s1.getSelectedItem().equals("week")) {
                    for (int k = 0; k < i.length; k++) {
                        if (i[k] == 1)
                            tem = tem + " " + weekdays[k];
                    }

                    if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) && tem.equals(" ")) {
                        s = "weekly; Once";
                    } else if (tem.equals(" ")) {
                        s = "every " + edt1.getText().toString() + " weeks; Once";
                    } else if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) {
                        s = "weekly on" + tem + "; Once";
                    } else {
                        s = "every " + edt1.getText().toString() + " weeks on" + tem + "; Once";
                    }
                } else if (s1.getSelectedItem().equals("month")) {
                    if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))) {
                        s = "monthly; Once";
                    } else {
                        s = "every " + edt1.getText().toString() + " months; once";
                    }
                }
            }

            else {
                if (s1.getSelectedItem().equals("day")) {
                    if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))
                        s = "every" + " day; for " + edt2.getText().toString() + " times";
                    else
                        s = "every " + edt1.getText().toString() + " day; for " + edt2.getText().toString() + " times";
                } else if (s1.getSelectedItem().equals("week")) {
                    for (int k = 0; k < i.length; k++) {
                        if (i[k] == 1)
                            tem = tem + " " + weekdays[k];
                    }

                    if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) && tem.equals(" ")) {
                        s = "weekly; for " + edt2.getText().toString() + " times";
                    } else if (tem.equals(" ")) {
                        s = "every " + edt1.getText().toString() + " weeks; for " + edt2.getText().toString() + " times";
                    } else if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) {
                        s = "weekly on" + tem + "; for " + edt2.getText().toString() + " times";
                    } else {
                        s = "every " + edt1.getText().toString() + " weeks on" + tem + "; for " + edt2.getText().toString() + " times";
                    }
                } else if (s1.getSelectedItem().equals("month")) {
                    if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))) {
                        s = "monthly; for " + edt2.getText().toString() + " times";
                    } else {
                        s = "every " + edt1.getText().toString() + " months; for " + edt2.getText().toString() + " times";
                    }
                }
            }
        }

        if(srd.equals(check)) {
            String[] s2 = check.split(" ");
            if (s1.getSelectedItem().equals("day")) {
                if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))
                    s = "every" + " day; until " + s2[1];
                else
                    s = "every " + edt1.getText().toString() + " day; until " + s2[1];;
            } else if (s1.getSelectedItem().equals("week")) {
                for (int k = 0; k < i.length; k++) {
                    if (i[k] == 1)
                        tem = tem + " " + weekdays[k];
                }

                if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) && tem.equals(" ")) {
                    s = "weekly; until " + s2[1];;
                } else if (tem.equals(" ")) {
                    s = "every " + edt1.getText().toString() + " weeks; until " + s2[1];;
                } else if (edt1.getText().toString().equals("") || edt1.getText().toString().equals("1")) {
                    s = "weekly on" + tem + "; until " + s2[1];;
                } else {
                    s = "every " + edt1.getText().toString() + " weeks on" + tem + "; until " + s2[1];;
                }
            } else if (s1.getSelectedItem().equals("month")) {
            /*for (int k = 0;k < i.length;k++) {
                if(i[k] == 1)
                    tem = tem + " " +weekdays[k];
            }*/

                if ((edt1.getText().toString().equals("") || edt1.getText().toString().equals("1"))) {
                    s = "monthly; until " + s2[1];;
                } else {
                    s = "every " + edt1.getText().toString() + " months; until " + s2[1];;
                }
            }
        }

        Intent intent = new Intent();
        intent.putExtra("1",s);
        setResult(2, intent);
        finish();
        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
    }
}