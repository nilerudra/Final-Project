package com.example.test3;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ListOfFiles extends Fragment {


    ListView listView;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    ArrayList<String> itemList;
    TextView textView;
    LinkAdapter linkAdapter;

    public ListOfFiles() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list_of_files, container, false);
        textView = view.findViewById(R.id.placeholderf);
        listView = view.findViewById(R.id.listres);
        listView.setDivider(null);
        /*textView = view.findViewById(R.id.placeholder);
        listView = rootView.findViewById(R.id.listlinks);*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(uploadstdmaterial.s.equals("Teacher"))
            showFiles();
        else
            viewFiles();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    public void showFiles()
    {
        File directory;
        if(uploadstdmaterial.s.equals("Teacher")) {
            directory = new File(getActivity().getApplicationContext().getFilesDir(), "Study material" + uploadstdmaterial.sub_Id);
        }
        else {
            directory = new File(getActivity().getApplicationContext().getFilesDir(), "Study material" + uploadstdmaterial.stud_id + uploadstdmaterial.sub_name);
        }


        if(directory.exists())
        {
            textView.setVisibility(View.GONE);
            File[] files = directory.listFiles();
            List<String> fileNames = new ArrayList<>();
            for (File file : files) {
                Uri fileUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.example.test3.fileprovider", file);
                fileNames.add(file.getName() + "#404#URIBABY" + getContext().getContentResolver().getType(fileUri));
            }
            FilesAdapter adapter = new FilesAdapter(getActivity(), fileNames);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File selectedFile = files[position];
                    openFile(selectedFile);
                }
            });

        }
        else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void openFile(File file) {
        try {
            Uri fileUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.example.test3.fileprovider", file);
            Intent openIntent = new Intent(Intent.ACTION_VIEW);
            openIntent.setDataAndType(fileUri, getMimeType(fileUri));
            openIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(openIntent);
        } catch (NullPointerException np) {
            Toast.makeText(getContext(), "Error opening file", Toast.LENGTH_SHORT).show();
        } catch (ActivityNotFoundException anf)
        {
            Toast.makeText(getContext(), "Can't open file", Toast.LENGTH_SHORT).show();
        }
        catch (IllegalArgumentException iae)
        {
            Toast.makeText(getContext(), "can't open file", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMimeType(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        return contentResolver.getType(uri);
    }

    public void viewFiles()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Drive Links").child(uploadstdmaterial.sub_Id);
        /*itemList = new ArrayList<>();
        linkAdapter = new LinkAdapter(getActivity(), itemList);
        listView.setAdapter(linkAdapter);*/
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*itemList.clear();
                linkAdapter.clear();
                linkAdapter.notifyDataSetChanged();*/
                if(uploadstdmaterial.s.equals("Student")) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            if (childSnapshot.exists() && childSnapshot.child("fileName").getValue() != null && childSnapshot.child("fileDownloadLink").getValue() != null) {
                                String title = childSnapshot.child("fileName").getValue().toString();
                                String link = childSnapshot.child("fileDownloadLink").getValue().toString();
                                downloadFile(link, title);
                                //itemList.add(title + ":" + link);
                            }
                        }
                        //linkAdapter.notifyDataSetChanged();
                    /*if(itemList.isEmpty())
                    {
                        textView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        textView.setVisibility(View.GONE);
                    }*/
                    }
                }
                /*else
                {
                    textView.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textView.setVisibility(View.VISIBLE);
            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }

    private void downloadFile(String downloadUrl, String fileName) {
        /*Uri uri = Uri.parse(downloadUrl);

        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        // Set destination directory and file name
        java.io.File destinationFolder = new java.io.File(getActivity().getApplicationContext().getFilesDir(), "Study material" + uploadstdmaterial.stud_id + uploadstdmaterial.sub_name);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        File file = new File(destinationFolder.getPath(),*//*vironment.DIRECTORY_DOWNLOADS,*//* fileName);
        if(!file.exists()) {
            request.setDestinationInExternalPublicDir(destinationFolder.getPath(),*//*vironment.DIRECTORY_DOWNLOADS,*//* fileName);

            // Optionally, set other request parameters such as title, description, etc.
            request.setTitle("Downloading File");
            request.setDescription("File is being downloaded...");
            Toast.makeText(getContext(), "File is downloading....", Toast.LENGTH_SHORT).show();
            // Enqueue the download and get download ID
            long downloadId = downloadManager.enqueue(request);
        }
        else
        {
            showFiles();
        }
        // You can track the download progress using the download ID if needed*/



        new Thread(() -> {
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Create the destination folder if it doesn't exist
                File destinationFolder = new File(getActivity().getApplicationContext().getFilesDir(), "Study material" + uploadstdmaterial.stud_id + uploadstdmaterial.sub_name);
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs();
                }

                File file = new File(destinationFolder, fileName);

                if (!file.exists()) {
                    // Download the file
                    InputStream input = new BufferedInputStream(connection.getInputStream());
                    OutputStream output = new FileOutputStream(file);

                    byte[] data = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(data)) != -1) {
                        output.write(data, 0, bytesRead);
                    }

                    output.flush();
                    output.close();
                    input.close();

                    // File downloaded successfully
                    getActivity().runOnUiThread(() -> {
                        showToast("FIle downloaded successfully");
                        //Toast.makeText(getContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();
                        showFiles();
                    });
                } else {
                    // File already exists
                    getActivity().runOnUiThread(this::showFiles);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception accordingly
            }
        }).start();
    }

    private void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}