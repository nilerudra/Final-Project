package com.example.test3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LinkAdapter extends ArrayAdapter<String> {

    public LinkAdapter(Context context, List<String> links) {
        super(context, 0, links);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String[] raw = getItem(position).split(":",2);
        final String title = raw[0];
        final String link =  raw[1];

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.res, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.item);
        TextView textView1 = convertView.findViewById(R.id.titleoflink);

        textView.setText(link);
        if(!title.isEmpty())
            textView1.setText(title + ":");
        else
            textView1.setText("[Title unspecified]");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkInBrowser(link);
            }
        });

        return convertView;
    }

    private void openLinkInBrowser(String link) {
        // Open the link in a browser or handle it as per your requirements
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            getContext().startActivity(intent);
        }
        catch (ActivityNotFoundException ae)
        {
            Toast.makeText(getContext(), "This link is invalid!", Toast.LENGTH_SHORT).show();
        }
    }
}

