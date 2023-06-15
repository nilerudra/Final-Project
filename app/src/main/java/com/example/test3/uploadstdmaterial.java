package com.example.test3;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class uploadstdmaterial extends AppCompatActivity {

    GoogleSignInAccount acct,stud;
    AppCompatButton bt,bt1,bt2;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int PICK_FILE_REQUEST = 1;
    private Thread signInThread,thread,another;
    String filePath = "default";
    SharedPreferences sharedPreferences1,sharedPreferences,sharedPreferences3,sharedPreferences4,sharedPreferences5;
    public static String sub_name,stud_id = "Nope";
    String flag,folderId;
    int value = 0;
    static String dynamicurl,getFlag;
    public static String sub_Id,s;
    Dialog d1,d2;
    TextView t1;
    EditText t2,t3;
    ViewPager viewPager;
    TabLayout tabLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadstdmaterial);

        sharedPreferences4 = getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        s = sharedPreferences4.getString("myStringKey", "not found");

        d2 = new Dialog(this);
        d2.setContentView(R.layout.uploadlink);
        t2 = d2.findViewById(R.id.linkurl);
        t3 = d2.findViewById(R.id.linktitle);
        bt2 = d2.findViewById(R.id.savelink);
        bt2.setOnClickListener(view -> getUrl());



        bt1 = findViewById(R.id.upldlink);
        bt1.setOnClickListener(view ->updlink());

        bt = findViewById(R.id.upload);
        bt.setOnClickListener(view -> showChooser());

        acct = GoogleSignIn.getLastSignedInAccount(this);
        sharedPreferences3 = getSharedPreferences("fold_id", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("sub_name", Context.MODE_PRIVATE);
        sub_name = sharedPreferences.getString("sub", "");
        sub_Id = sharedPreferences.getString("subid", "");
        sharedPreferences1 = getSharedPreferences("flag", Context.MODE_PRIVATE);
        flag = sharedPreferences1.getString("key2" + sub_name,"0");
        if(flag.equals("0")) {

            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("key2" + sub_name, "1");
            editor.apply();

            d1 = new Dialog(this);
            d1.setContentView(R.layout.directionofuse);
            t1 = d1.findViewById(R.id.drctouse);
            t1.setText("Hello " + acct.getDisplayName() + "! On this page you can Upload the Study Material related to subject " + sub_name + " and provide links to videos and blogs online.");
            d1.show();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Drive driveService = getDriveService();
                    File folderMetadata = new File();
                    folderMetadata.setName("Study Material: " + sub_name);
                    folderMetadata.setParents(Collections.singletonList("root"));
                    folderMetadata.setMimeType("application/vnd.google-apps.folder");

                    try {
                        File createdFolder = driveService.files().create(folderMetadata).setFields("id").execute();
                        folderId = createdFolder.getId();

                        SharedPreferences.Editor editor = sharedPreferences3.edit();
                        editor.putString("sub" + sub_name, folderId);
                        editor.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }


        stud = GoogleSignIn.getLastSignedInAccount(this);


        if (s.equals("Student")) {
            stud_id = stud.getId();
            bt.setVisibility(View.GONE);
            bt1.setVisibility(View.GONE);
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Drive driveService = getDriveService();
                File folderMetadata = new File();
                folderMetadata.setName("Study Material: " + sub_name);
                folderMetadata.setParents(Collections.singletonList("root"));
                folderMetadata.setMimeType("application/vnd.google-apps.folder");
                File folder = null;
                try {
                    folder = driveService.files().get(sharedPreferences3.getString("sub" + sub_name, "")).execute();
                } catch (IOException e) {
                    try {
                        if(folder == null) {
                            File createdFolder = driveService.files().create(folderMetadata).setFields("id").execute();
                            folderId = createdFolder.getId();
                            SharedPreferences.Editor editor = sharedPreferences3.edit();
                            editor.putString("sub" + sub_name, folderId);
                            editor.apply();
                        }
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                }
            }
        });
        thread.start();


        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter adapterv = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterv);


        sharedPreferences5 = getSharedPreferences("dynamicurl", Context.MODE_PRIVATE);
        dynamicurl = sharedPreferences5.getString("urldyn", "not found");
        getFlag = sharedPreferences5.getString("flag", "not found");


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(s.equals("Teacher")) {
                    int position = tab.getPosition();
                    switch (position) {
                        case 0:
                            if(!dynamicurl.equals("not found") && value == 0)
                            {
                                if(getFlag.equals("file"))
                                {
                                    bt.setVisibility(View.VISIBLE);
                                    bt1.setVisibility(View.GONE);
                                    java.io.File file = new java.io.File(getFilePath(getApplicationContext(),Uri.parse(dynamicurl)));
                                    Toast.makeText(uploadstdmaterial.this, file.toString(), Toast.LENGTH_SHORT).show();
                                    signInThread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            uploadFile(file);
                                        }
                                    });
                                    signInThread.start();
                                    SharedPreferences.Editor editor = sharedPreferences5.edit();
                                    editor.remove("urldyn");
                                    editor.apply();
                                    dynamicurl = "not found";

                                }
                                else {
                                    bt.setVisibility(View.GONE);
                                    bt1.setVisibility(View.VISIBLE);
                                    tab = tabLayout.getTabAt(1);
                                    tabLayout.selectTab(tab);
                                    value = 1;
                                }
                            }
                            else {
                                bt.setVisibility(View.VISIBLE);
                                bt1.setVisibility(View.GONE);
                            }
                            break;
                        case 1:
                            if(!dynamicurl.equals("not found"))
                            {
                                if(getFlag.equals("file"))
                                {

                                }
                                else {
                                    t2.setText(dynamicurl);
                                    d2.show();
                                    SharedPreferences.Editor editor = sharedPreferences5.edit();
                                    editor.remove("urldyn");
                                    editor.apply();
                                }
                            }
                            bt.setVisibility(View.GONE);
                            bt1.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tabLayout.setupWithViewPager(viewPager);
    }

    public static String getFilePath(Context context, Uri uri) {
        if (getFlag.equals("file")) {
            try {
                String path = null;
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(columnIndex);
                    cursor.close();
                }

                return path;
            }
            catch (SecurityException se)
            {
                se.printStackTrace();
            }
            getFlag = "";
        }
        return "";
    }

    public void getUrl()
    {
        if(!t2.getText().toString().isEmpty()) {
            String url = t2.getText().toString();
            String s = " ";
            if(t3.getText().toString().endsWith(":"))
            {
                s = t3.getText().toString().substring(0,t3.getText().toString().length() - 1);
            }
            else
                s = t3.getText().toString();

            String title = s;
            another = new Thread(new Runnable() {
                @Override
                public void run() {
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


                        // Assuming you have the website URL stored in a variable called "websiteUrl"
                        //Document doc = Jsoup.connect(websiteUrl).get();
                        Elements iconElements = doc.select("link[rel~=icon], link[rel~=shortcut icon], link[rel~=apple-touch-icon]");

                        int maxIconSize = 0;

                        for (Element element : iconElements) {
                            String href = element.attr("href");
                            String sizeAttr = element.attr("sizes");

                            // Extract the size from sizes attribute if available
                            int size = 0;
                            if (!sizeAttr.isEmpty()) {
                                String[] sizes = sizeAttr.split("x");
                                if (sizes.length > 0) {
                                    try {
                                        size = Integer.parseInt(sizes[0].trim());
                                    } catch (NumberFormatException e) {
                                        // Handle parsing error if needed
                                    }
                                }
                            }

                            // Check if the size is larger than the previous largest icon
                            if (size > maxIconSize) {
                                maxIconSize = size;
                                faviconUrl = href;
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (RuntimeException re) {
                        re.printStackTrace();
                    }

                    saveLinks(faviconUrl,title, url);
                }
            });
            another.start();
            d2.hide();
            t2.setText("");
            t3.setText("");
        }
        else
        {
            showToast("Please provide link/url...");
        }
    }
    public void saveLinks(String faviconurl, String s, String url)
    {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Links").child(mngtchclass.subId).push();

            reference.child("linktitle").setValue(s);
            reference.child("linkthumnail").setValue(faviconurl);
            reference.child("link").setValue(url);
            dynamicurl = "not found";
    }
    public void updlink()
    {
        d2.show();
    }

    public void showChooser()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
        }
        else {
            showFileChooser();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    {
                         startActivity(new Intent(this,Request_access.class));
                    }
                    else
                        showFileChooser();
                } else {
                    Toast.makeText(this, "Cannot access storage", Toast.LENGTH_SHORT).show(); // permission denied, handle the situation accordingly
                }
                return;
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(uploadstdmaterial.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public Drive getDriveService()
    {
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
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
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){

            if (requestCode == REQUEST_AUTHORIZATION && resultCode == RESULT_OK) {
                showFileChooser();
            }

            /*if (requestCode == PICK_FILE_REQUEST_11 && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();

                    // Access the file using the DocumentFile
                    DocumentFile documentFile = DocumentFile.fromSingleUri(this, fileUri);

                    if (documentFile != null && documentFile.isFile()) {
                        Uri fileContentUri = documentFile.getUri();
                        java.io.File originalFile = new java.io.File(fileContentUri.getPath());
                        signInThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                uploadFile(originalFile);
                            }
                        });
                        signInThread.start();
                    }
                }
            }*/

            /*if (requestCode == REQUEST_CODE_OPEN_DOCUMENT_TREE && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri treeUri = data.getData();

                    try {
                        getContentResolver().takePersistableUriPermission(treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, treeUri); // Set the initial directory

                        startActivityForResult(intent, PICK_FILE_REQUEST);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData().getPath();
                    if (filePath.startsWith("/document/primary:")) {
                        //Toast.makeText(this, "Path contains /document/primary:", Toast.LENGTH_SHORT).show();
                        filePath = Environment.getExternalStorageDirectory() + "/" + filePath.substring("/document/primary:".length());
                    }
                    java.io.File file = new java.io.File(filePath);
                    signInThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadFile(file);
                        }
                    });
                    signInThread.start();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

        private void showFileChooser () {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");

            try {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    startActivityForResult(Intent.createChooser(intent1, "Select a file to upload"), REQUEST_CODE_OPEN_DOCUMENT_TREE);
                }
                else {*/
                    showToast("Select Study Material for Subject");
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), PICK_FILE_REQUEST);
               // }
            } catch (android.content.ActivityNotFoundException ex) {
                showToast("please install a file manager");
            }
        }

        public void uploadFile (java.io.File originalFile)
        {
            Drive googleDriveService = getDriveService();
            String folder_id = sharedPreferences3.getString("sub" + sub_name, "");
            File fileMetaData = new File();
            fileMetaData.setParents(Collections.singletonList(folder_id));
            java.io.File file = originalFile;
            fileMetaData.setName(file.getName());
            FileContent mediaContent = new FileContent("application/octet-stream", file);
            try {
                File file1 = googleDriveService.files().create(fileMetaData, mediaContent).setFields("id, webContentLink, webViewLink").execute();
                String fileId = file1.getId();
                Permission publicPermission = new Permission()
                        .setType("anyone")
                        .setRole("reader");
                googleDriveService.permissions().create(fileId, publicPermission).execute();
                String publicUrl = file1.getWebViewLink();

                String downloadUrl = file1.getWebContentLink();



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Drive Links").child(mngtchclass.subId).push();

                reference.child("fileName").setValue(file.getName());
                reference.child("fileLink").setValue(publicUrl);
                reference.child("fileDownloadLink").setValue(downloadUrl);

                showToast("File Uploaded successfully!");
            } catch (UserRecoverableAuthIOException e) {
                showToast("Re-authentication required!");
                Intent authIntent = e.getIntent();
                startActivityForResult(authIntent, REQUEST_AUTHORIZATION);
            } catch (FileNotFoundException fnf) {
                showToast(fnf.toString());
            } catch (Exception ae) {
                ae.printStackTrace();
                showToast("Error uploading file, Please try again...");
            }

            java.io.File destinationFolder = new java.io.File(getApplicationContext().getFilesDir(), "Study material" + sub_Id);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            java.io.File destinationFile = new java.io.File(destinationFolder, originalFile.getName());
            try {
                InputStream inputStream = new FileInputStream(originalFile);
                OutputStream outputStream = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
            catch (FileNotFoundException fileNotFoundException)
            {
                showToast("File not found");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }