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
        String s = intent.getStringExtra("sub_name");

        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra("subject", s);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Android version >= Oreo. Starting foreground service.");
            context.startService(serviceIntent);
        } else {
            Log.d(TAG, "Android version < Oreo. Starting background service.");
            context.startService(serviceIntent);
        }
    }
}
