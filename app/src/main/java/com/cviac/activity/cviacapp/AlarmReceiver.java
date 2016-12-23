package com.cviac.activity.cviacapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.cviac.datamodel.cviacapp.Conversation;
import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.EventInfo;
import com.cviac.fragments.cviacapp.Events;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;


/**
 * Created by User on 12/21/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent) {

        String srt = null;
        List<Employee> emplist = Employee.eventsbydate();


        for (Employee e : emplist) {
            EventInfo evt = new EventInfo();
            evt.setEvent_title(e.getEmp_name());
            evt.setEvent_description("Happy Birthday");
            evt.setEvent_date(new Date());
            evt.save();

            srt = e.getEmp_name();
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.cviac_logo)
                    .setContentTitle(srt)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentText("Happy Birthday");


            Intent resultIntent = new Intent(context, HomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(FireChatActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.

            mNotificationManager.notify(0, mBuilder.build());
        }


    }


}