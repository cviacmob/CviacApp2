package com.cviac.com.cviac.app.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.cviac.activity.cviacapp.FireChatActivity;
import com.cviac.activity.cviacapp.HomeActivity;
import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EventInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by User on 12/21/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent) {

        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);


        String lastAlarm = prefs.getString("lastalaram", null);
        if (lastAlarm != null) {
            if (lastAlarm.equalsIgnoreCase(getDate())) {
                return;
            }
        }

        String srt = null;
        List<Employee> emplist = Employee.eventsbydate();


        int counter = 0;
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


            Intent resultIntent = new Intent(context, FireChatActivity.class);

            Conversation cnv = new Conversation();
            cnv.setEmpid(e.getEmp_code());
            cnv.setName(e.getEmp_name());
            resultIntent.putExtra("conversewith",cnv);
            resultIntent.putExtra("fromnotify",1);

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

            mNotificationManager.notify(counter, mBuilder.build());
            counter++;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastalaram", getDate());
        editor.commit();


    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }


}