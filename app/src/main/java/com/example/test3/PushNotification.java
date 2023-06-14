package com.example.test3;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotification extends FirebaseMessagingService {

    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "MyChannel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            // Handle the notification message
            handleNotification(title, message);
        } else {
            Log.d(TAG, "No notification payload found in the remote message.");
        }
    }

    private void handleNotification(String title, String message) {
        // Handle the received notification, e.g., show it in a notification view or display a dialog
        Log.d(TAG, "Received notification: " + title + ", " + message);

        // Show the notification
        showNotification(getApplicationContext(), title, message);

        // Notify the student using a dialog
       // showDialog(title, message);
    }

    private void showDialog(final String title, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PushNotification.this);
                builder.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform any necessary actions when the student clicks the OK button
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, splashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Acquire the WakeLock to wake up the device
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "MyApp:WakeLock");

        // Acquire the WakeLock
        wakeLock.acquire(10*60*1000L /*10 minutes*/);

        // Show the notification on the main thread
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // Build the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.logo)
                        .setAutoCancel(true);

                //builder.setContentIntent(pendingIntent);

                // Show the notification
                notificationManager.notify(1, builder.build());

                // Release the WakeLock after a certain duration (e.g., 5 seconds)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wakeLock.release();
                    }
                }, 5000); // Adjust the duration as needed
            }
        });
    }
}
