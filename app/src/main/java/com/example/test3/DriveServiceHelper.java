package com.example.test3;

import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;


import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper
{
    File myFile;

    private final Executor mxExecutor = Executors.newSingleThreadExecutor();
    private Drive mDriveServices;

    public DriveServiceHelper(Drive mDriveServices)
    {
        this.mDriveServices = mDriveServices;
    }

    public Task<String> createFilePDF(String filePath)
    {
        return Tasks.call(mxExecutor,() -> {
            File fileMetaData = new File();
            fileMetaData.setName("myPDFFile");

            java.io.File file = new java.io.File(filePath);

            FileContent mediaContent = new FileContent("application/pdf",file);

            myFile = null;
            try {

                myFile = mDriveServices.files().create(fileMetaData,mediaContent).execute();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            if(myFile == null)
            {
                throw new IOException("Null result when requesting the file");
            }
            System.out.println("Entered Here means running parallel");
            //throw new IOException("Entered here means no parallel problem");
            retfid();
            return myFile.getId();

        });
    }

    public String retfid()
    {
        String s = myFile.getId();
        return s;
    }




}
