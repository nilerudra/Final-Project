package com.example.test3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class LinkAdapter extends ArrayAdapter<String> {

    Context context;
    public LinkAdapter(Context context, List<String> links) {
        super(context, 0, links);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String[] raw = getItem(position).split(":",2);
        final String title = raw[0];
        final String[] linkraw =  raw[1].split("aHHfvbsh342&#",2);
        final String link = linkraw[0];
        final String thumbnail = linkraw[1];

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.res, parent, false);
        }

        Uri uri = Uri.parse(link);
        String source = uri.getHost();
        TextView textView = convertView.findViewById(R.id.item);
        TextView textView1 = convertView.findViewById(R.id.titleoflink);
        TextView textView2 = convertView.findViewById(R.id.source);
        ImageView imageView = convertView.findViewById(R.id.thumbnail);

        Glide.with(context).load(thumbnail).into(imageView);

        textView2.setText(source);
        textView.setText(link);
        if(!title.isEmpty())
            textView1.setText(title + ":");
        else
            textView1.setText("[Title unspecified]");


        String url = link;

        //new FetchThumbnailTask(imageView).execute(url);

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

    private class FetchThumbnailTask extends AsyncTask<String, Void, String> {
        private ImageView imageView;

        public FetchThumbnailTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];

            String faviconUrl = null;

            try {
                /*Document document = Jsoup.connect(url).get();
                faviconUrl = document.select("img.thumbnail").attr("src");
                // Retrieve the favicon URL from the HTML
                //faviconUrl = document.select("link[rel~=(?i)^(shortcut|icon)$]").attr("href");
                if (faviconUrl.startsWith("//")) {
                    faviconUrl = "http:" + faviconUrl;
                } else if (!faviconUrl.startsWith("http")) {
                    faviconUrl = url + faviconUrl;
                }*/

                // Assuming you have the website URL stored in a variable called "websiteUrl"
                Document doc = Jsoup.connect(url).get();
                Element iconElement = doc.select("link[rel~=(?i)^(shortcut|icon|favicon)]").first();
                faviconUrl = iconElement.absUrl("href");

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (RuntimeException re)
            {
                re.printStackTrace();
            }

            return faviconUrl;
        }

        @Override
        protected void onPostExecute(final String faviconUrl) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // Use the favicon URL to fetch and display the thumbnail image
                    // (You can use an image loading library like Glide or Picasso)
                    // For example, using Glide:
                    Glide.with(context).load(faviconUrl).into(imageView);
                }
            });
        }
    }


}

