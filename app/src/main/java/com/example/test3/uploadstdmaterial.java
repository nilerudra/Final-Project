package com.example.test3;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.google.android.material.internal.ContextUtils.getActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;

public class uploadstdmaterial extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct;
    Button bt;
    DriveServiceHelper driveServiceHelper;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int PICK_FILE_REQUEST = 1;

    String filePath = "default";
    private Handler mHandler = new Handler();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadstdmaterial);

        bt = findViewById(R.id.upload);
        //processStudentData();
        bt.setOnClickListener(view -> showFileChooser());
        /*GoogleSignInOptions signInOptions =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();

GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);

Intent signInIntent = signInClient.getSignInIntent();
startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
*/


        /*if(acct!=null)
        {
            Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
            handlesignin();
            Toast.makeText(this, acct.getEmail(), Toast.LENGTH_SHORT).show();
            //navigateToSecondActivity();
            //splashScreen ss = new splashScreen();
        }
        else
            Toast.makeText(this, "Didn't Verified", Toast.LENGTH_SHORT).show();*/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
        }


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                    showFileChooser();//processStudentData();// permission granted, proceed with accessing the storage
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

    public void handlesignin()
    {
        /*



        try {
        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        String accessToken = account.getIdToken();

        // Use the access token to authorize requests to the Google Drive API
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Drive driveService = new Drive.Builder(transport, jsonFactory, credential)
                .setApplicationName(getString(R.string.app_name))
                .build();

        // Call Google Drive API methods using the authorized driveService object
        // ...
        } catch (ApiException e) {
        Log.w(TAG, "handleSignInResult:error", e);
        // ...
        }



        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Drive driveService = new Drive.Builder(transport, jsonFactory, credential)
                .setApplicationName(getString(R.string.app_name))
                .build();



        Drive driveService = new Drive.Builder(transport, jsonFactory, credential)
                .setApplicationName(getString(R.string.app_name))
                .build();
        */

        //GoogleSignInAccount account = acct.getResult(ApiException.class);
        //HttpTransport transport = AndroidHttp.newCompatibleTransport();
        //imp - GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        /*HttpRequestInitializer requestInitializer = new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.getHeaders().setAuthorization("Bearer " + accessToken);
            }
        };*/
        //credential.setSelectedAccount(acct.getAccount());

        /*gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("778432956795-5v179rvp7pt0ao9n0tqhd3ioel1elmsi.apps.googleusercontent.com")
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        gsc = GoogleSignIn.getClient(this,gso);*/

        //here checks if user has already signed in. if yes the it does not return null and we go to secondactivity().
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct != null) {

            showToast(acct.getEmail());
            /*String accessToken = acct.getIdToken();
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);*/
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(acct.getAccount());
            Drive googleDriveService = new Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    jsonFactory,
                    credential)
                    .setApplicationName("EduNexus")
                    .build();

            File fileMetaData = new File();
            fileMetaData.setName("myFile");

            //java.io.File externalStorageDirectory = Environment.getExternalStorageDirectory();
            //java.io.File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
// specify the rest of the file path

// create a file object by combining the two paths
            showToast(filePath);
            java.io.File file = new java.io.File("/storage/emulated/0/sample.pdf");
            showToast(file.toString());
            showToast("1 arrived here");
            showToast("1 arrived here");
            showToast("1 arrived here");showToast("1 arrived here");showToast("1 arrived here");
            showToast("1 arrived here");


            //Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();
            FileContent mediaContent = new FileContent("application/pdf", file);
            showToast("1 arrived here");
            String s = "File not initialized";
            try {
                File file1 = googleDriveService.files().create(fileMetaData, mediaContent).setFields("id").execute();
                showToast("File Uploaded successfully!!!");
                s = file1.getId();
            } catch (UserRecoverableAuthIOException e) {
                // This exception is thrown when additional authorization is required
                showToast("Entered User Recoverable Auth IO Exception");
                //Toast.makeText(this, "Entered User Recoverable Auth IO Exception", Toast.LENGTH_SHORT).show();
                Intent authIntent = e.getIntent();
                startActivityForResult(authIntent, REQUEST_AUTHORIZATION);
            } catch (Exception ae) {
                //Toast.makeText(this, "Entered Catch", Toast.LENGTH_SHORT).show();
                showToast("Entered Catch");
                ae.printStackTrace();
                //String s1 = ae.getMessage();
                //Toast.makeText(this, ae.toString(), Toast.LENGTH_SHORT).show();
                //showToast(ae.getMessage());
                showToast(ae.toString());
                System.out.println(ae.toString());
            }

            showToast(s);
            //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

            //Toast.makeText(this, "Entered handlesignin", Toast.LENGTH_SHORT).show();
            //driveServiceHelper = new DriveServiceHelper(googleDriveService);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_AUTHORIZATION && resultCode == RESULT_OK) {
            // The user authorized the app, so retry the Drive API operation
            // that triggered the authorization request
            processStudentData();
        }
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Toast.makeText(this, "Selected File returning path", Toast.LENGTH_SHORT).show();
            /*Uri uri = data.getData();

            if (uri == null) {
                filePath = "uri is null";
            }
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                filePath = path;
            }
            filePath = uri.getPath();
*/
            filePath = data.getData().getPath();
            if (filePath.startsWith("/document/primary:")) {
                Toast.makeText(this, "Path contains /document/primary:", Toast.LENGTH_SHORT).show();
                filePath = Environment.getExternalStorageDirectory() + "/" + filePath.substring("/document/primary:".length());
            }
            //filePath = FileUtils.getPath(this, uri); // get the path of the selected file
            Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
            // use the filePath to access the selected file
            processStudentData();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            //startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), PICK_FILE_REQUEST);
            startActivityForResult(intent,PICK_FILE_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }



    public void uploadFile()
    {






        /*ProgressDialog progressDialog = new ProgressDialog(uploadstdmaterial.this);
        progressDialog.setTitle("Uploading to Google Drive");
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();

        String filePath = "/storage/emulated/0/Sem 6/Sem 6/Computer Graphics/Model Answer/22318-2019-Winter-model-answer-paper[Msbte study resources].pdf";
         driveServiceHelper.createFilePDF(filePath).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();
                Toast.makeText(uploadstdmaterial.this, "uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(uploadstdmaterial.this, "Check your google drive api key", Toast.LENGTH_SHORT).show();
            }
        });*/


        //String s1 = driveServiceHelper.retfid();
        //Toast.makeText(this, s1, Toast.LENGTH_SHORT).show();
    }

    private Runnable getStudentIdAndLoadFromDB = new Runnable() {
        @Override
        public void run() {
            handlesignin();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //loadFromDB();
                }
            }, 500); // delay for 1000 milliseconds
        }
    };

    private void processStudentData() {
        new Thread(getStudentIdAndLoadFromDB).start();
    }
}