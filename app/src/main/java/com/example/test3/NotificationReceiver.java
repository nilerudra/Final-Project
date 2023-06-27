package com.example.test3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("1");
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        ArrayList<String> token = intent.getStringArrayListExtra("tokens");

        // Send the FCM request to deliver the notification
        sendFCMRequest(title, message, token);
    }

    private void sendFCMRequest(String title, String message, ArrayList<String>tokens) {
        try {
            OkHttpClient client = new OkHttpClient();
            System.out.println("1");
            // Prepare the notification payload
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);
            System.out.println("2");
            // Create the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("registration_ids", new JSONArray(tokens));
            requestBody.put("notification", notification);
            System.out.println("3");
            // Build the request
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());
            Builder builder = new Builder();
            builder.url("https://fcm.googleapis.com/fcm/send");
            builder.post(body);
            builder.addHeader("Content-Type", "application/json");
            builder.addHeader("Authorization", "Bearer " + "AAAAKMoghUs:APA91bEqxwAytYjcOUWweVuzo0wV1q3pEYgB_m4r_Ldtj3bs0WVnHiP_ERqwlIebfHwCRzZ1mLgjTmAD_vJhdmDYTe7SIWMxtduwjKn_Qfr0NOxN8G_29QCMDBiL4jfu23PyhYz16ROP");
            Request request = builder
                    .build();
            System.out.println("4");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("hii");
                    e.printStackTrace();
                    // Handle failure case
                }

                @Override
                public void onResponse(Call call, Response response) {
                    System.out.println("5");
                    if (response.isSuccessful()) {
                        // Notification sent successfully
                        System.out.println("Notification sent to student");
                    } else {
                        // Error occurred while sending notification
                        System.out.println("Failed to send notification to student");
                    }
                    response.close();
                }
            });

            System.out.println("6");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
