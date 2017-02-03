package com.cviac.com.cviac.app.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.cviac.activity.cviacapp.FireChatActivity;
import com.cviac.activity.cviacapp.R;

import com.cviac.activity.cviacapp.XMPPChatActivity;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by User on 11/24/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessageService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            showChatNotification(data);
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            return;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String notofi=remoteMessage.getNotification().getBody();
            showAnnouncement(notofi);
            //annoncement(notofi);
            Log.d(TAG, "Message Annoncements Body: " + remoteMessage.getNotification().getBody());

        }
    }
   /* private void annoncement(String msg){
        Annoncements anc=new Annoncements();
        anc.setAnnoncemsg(msg);
        anc.setDate(new Date());
        anc.save();
    }*/

    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private void showChatNotification(Map<String, String> data) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cviac_logo)
                        .setContentTitle(data.get("sendername"))
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentText(data.get("msg"));

        Intent resultIntent = new Intent(this, XMPPChatActivity.class);
        Conversation conv = new Conversation();
        conv.setName(data.get("sendername"));
        conv.setEmpid(data.get("senderid"));
        conv.setLastmsg(data.get("msg"));
        conv.setDatetime(new Date());
        resultIntent.putExtra("fromnotify", 1);
        resultIntent.putExtra("conversewith", conv);
        Conversation.updateOrInsertConversation(conv);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(FireChatActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        mNotificationManager.notify(0, mBuilder.build());


    }

    private void showAnnouncement(String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cviac_logo)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentTitle("CVIAC")
                        .setContentText(msg);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());


    }
}
