package com.example.test3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FilesAdapter extends ArrayAdapter<String> {

    public FilesAdapter(Context context, List<String> links) {
        super(context, 0, links);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String[] raw = getItem(position).split("#404#URIBABY",2);
        final String title = raw[0];
        final String type = raw[1];

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.file, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.item);
        TextView textView1 = convertView.findViewById(R.id.filetype);
        ImageView imageView = convertView.findViewById(R.id.itemIcon);

        textView.setText(title);

        switch (type)
        {
            case "application/pdf": imageView.setImageResource(R.drawable.pdf);
                                    textView1.setText("PDF");
                                    break;

            case "image/jpeg": imageView.setImageResource(R.drawable.jpeg);
                               textView1.setText("JPEG");
                               break;

            case "image/png": imageView.setImageResource(R.drawable.jpeg);
                              textView1.setText("PNG");
                              break;

            case "image/gif": imageView.setImageResource(R.drawable.jpeg);
                              textView1.setText("GIF");
                              break;

            case "text/plain": imageView.setImageResource(R.drawable.txt);
                               textView1.setText("TXT");
                               break;

            case "application/vnd.openxmlformats-officedocument.presentationml.presentation": imageView.setImageResource(R.drawable.pptx);
                textView1.setText("GIF");
                                                                                              break;

            case "application/vnd.ms-powerpoint": imageView.setImageResource(R.drawable.pptx);
                                                  textView1.setText("GIF");
                                                  break;

            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": imageView.setImageResource(R.drawable.xlsx);
                                                                                      textView1.setText("XLSX");
                                                                                      break;

            case "application/vnd.ms-excel": imageView.setImageResource(R.drawable.xlsx);
                                             textView1.setText("XLSX");
                                             break;

            default: imageView.setImageResource(R.drawable.common);
                     textView1.setText("DOCS");
        }

        /*if(!title.isEmpty())
            textView1.setText(title + ":");
        else
            textView1.setText("[Title unspecified]");*/

        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkInBrowser(title);
            }
        });*/

        return convertView;
    }

    /*private void openLinkInBrowser(String link) {
        // Open the link in a browser or handle it as per your requirements
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            getContext().startActivity(intent);
        }
        catch (ActivityNotFoundException ae)
        {
            Toast.makeText(getContext(), "This link is invalid!", Toast.LENGTH_SHORT).show();
        }
    }*/
}

