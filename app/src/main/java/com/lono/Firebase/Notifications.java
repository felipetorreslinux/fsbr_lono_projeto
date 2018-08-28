package com.lono.Firebase;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lono.R;

import java.security.Principal;

public class Notifications extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        send(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void send (String title, String message){
        Intent intent = new Intent(this, Principal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentlikes = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channel_likes = getString(R.string.channel_notifications);
        Uri defaultSoundUri_likes = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder_likes = new NotificationCompat.Builder(this, channel_likes)
                .setSmallIcon(R.drawable.ic_search)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri_likes)
                .setContentIntent(pendingIntentlikes);
        NotificationManager notificationManager_likes = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_likes,
                    "lono",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager_likes.createNotificationChannel(channel);
        }
        notificationManager_likes.notify(0, notificationBuilder_likes.build());
    }
}
