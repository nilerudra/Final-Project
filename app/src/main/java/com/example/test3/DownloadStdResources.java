package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

public class DownloadStdResources extends AppCompatActivity {

    GoogleSignInAccount acct;
    String sub_name;
    private Thread signInThread;
    AppCompatButton appCompatButton;
    SharedPreferences sharedPreferences,sharedPreferences1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_std_resources);

        sharedPreferences = getSharedPreferences("fold_id", Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("sub_name", Context.MODE_PRIVATE);
        sub_name = sharedPreferences1.getString("sub", "");

        appCompatButton = findViewById(R.id.download);
        appCompatButton.setOnClickListener(view -> startdld());

    }

    public Drive getDriveService()
    {
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            //showToast(acct.getEmail());
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(acct.getAccount());
            Drive googleDriveService = new Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    jsonFactory,
                    credential)
                    .setApplicationName("EduNexus")
                    .build();
            return googleDriveService;
        }
        else
            return null;
    }

    public void startdld() {

        signInThread = new Thread(new Runnable() {
            @Override
            public void run() {
                downloadFile();
            }
        });
        signInThread.start();
    }

    public void downloadFile()
    {
        /*java.io.File studyMaterialDir = new java.io.File(getFilesDir(), "study material");
        if (!studyMaterialDir.exists()) {
            studyMaterialDir.mkdirs();

            showToast(studyMaterialDir.toString() + " Created Inside if");
            //Toast.makeText(this, studyMaterialDir.toString() + " Created", Toast.LENGTH_SHORT).show();
        }
        showToast(studyMaterialDir.toString() + " Created");*/
        //Toast.makeText(this, studyMaterialDir.toString() + " Created", Toast.LENGTH_SHORT).show();

// Construct the file path within the study material directory
        //String filePath = studyMaterialDir.getPath() + java.io.File.separator + file.getName();


        java.io.File studyMaterialDir = new java.io.File("/storage/emulated/0/", "study material");
        if (!studyMaterialDir.exists()) {
            studyMaterialDir.mkdirs();

            showToast(studyMaterialDir + " Created Inside if");
            //Toast.makeText(this, studyMaterialDir.toString() + " Created", Toast.LENGTH_SHORT).show();
        }
        showToast(studyMaterialDir + " Created");


        Drive googleDriveService = getDriveService();

// Define the folder ID of the Google Drive folder
        String folderId = "1glaPI5pTKldOibH6QZta0HMLgcVCSPZu";//sharedPreferences.getString("sub" + sub_name, "");;

// Retrieve the files from the folder
        FileList result = null;
        try {
            result = googleDriveService.files().list()
                    .setQ("'" + folderId + "' in parents")
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<File> files = result.getFiles();

// Iterate through the files and download each one
        try {
            for (File file : files) {
                String fileId = file.getId();
                OutputStream outputStream = null;
                outputStream = new FileOutputStream(studyMaterialDir.getPath() + java.io.File.separator + file.getName());
                googleDriveService.files().get(fileId)
                        .executeMediaAndDownloadTo(outputStream);
                outputStream.close();
            }
            showToast("Files Downloaded");
            //Toast.makeText(this, "Files downloaded", Toast.LENGTH_SHORT).show();

        }
        catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DownloadStdResources.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


}