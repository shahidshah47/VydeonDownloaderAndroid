package com.appdev360.jobsitesentry.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.appdev360.jobsitesentry.application.JobSiteSentry;
import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Notification;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.util.EventBusMessage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Updated By Hussain Saad on 27/01/22
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("Test321", "Remote eventType");
        // Check if eventType contains a data payload.
        if (remoteMessage.getData().size() <= 0) {
            return;
        }

        if (!JobSiteSentry.isAppRunning()) {
            sendNotification(remoteMessage.getData().get("title"));
            return;
        }

        if (remoteMessage.getData() != null) {
            EventBus.getDefault().post(new EventBusMessage(EventBusMessage.EVENT_FIREBASE_MESSAGE, remoteMessage.getData().get("title")));
        }

    }

    //This method is only generating push notification
    private void sendNotification(String messageBody) {

        if (messageBody == null) {
            return;
        }

        Intent intent = new Intent(this, SentryLocationActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("notification", messageBody);
        intent.putExtras(bundle);

        int uniqueID = getID();

        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueID, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder;
        // Check if we're running on Android 5.0 or higher
        //  if (Build.VERSION.SDK_INT >= 21) {
        // Call some material design APIs here

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.devices_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.devices_icon))
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle("New Arrest Video")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
            // Implement this feature without material design
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.devices_icon)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle("New Arrest Video")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent);
        }


        notificationManager.notify(uniqueID, notificationBuilder.build());
    }


    public int getID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }


    private Notification parseResponse(RemoteMessage remoteMessage) {
        Gson gson = new Gson();
        return gson.fromJson(remoteMessage.getData().get("body"), Notification.class);
    }
}