package com.example.test3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;
    NotificationManager nm;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This method is called when the service is started.
        // You can handle any intent extras here.

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.logo)
                .setContentText(intent.getStringExtra("subject"))
                .setSubText("New Message from EduNexs");

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
        notification.setContentIntent(pendingIntent);

        nm.notify(NOTIFICATION_ID, notification.build());
        return START_STICKY; // Return START_STICKY to keep the service running until it is explicitly stopped.
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
