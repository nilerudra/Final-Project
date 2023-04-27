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
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Attendance extends Fragment {

    private TextView qrCodeTV;
    private ImageView qrCodeIV;
    private TextInputEditText dataEdt;
    private Button generateQRBtn;

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
        generateQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = dataEdt.getText().toString();
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
}
