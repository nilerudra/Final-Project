package com.example.test3;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    private static final String TAG = "BroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called");

        Intent serviceIntent = new Intent(context, NotificationService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Android version >= Oreo. Starting foreground service.");
            ContextCompat.startForegroundService(context, serviceIntent);
        } else {
            Log.d(TAG, "Android version < Oreo. Starting background service.");
            context.startService(serviceIntent);
        }
    }
}
