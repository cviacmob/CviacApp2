package com.cviac.com.cviac.app.xmpp;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;

public class XMPPService extends Service {
    //private static final String DOMAIN1 = "cviacmob.p1.im";
    private static final String DOMAIN = "ec2-52-33-98-83.us-west-2.compute.amazonaws.com";
//    private static final String USERNAME = "guna";
//    private static final String PASSWORD = "tech@cviac";
    public static ConnectivityManager cm;
    public static XMPPClient xmpp;
    public static boolean ServerchatCreated = false;
    String text = "";


    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<XMPPService>(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String userid = prefs.getString("empid", "");
        xmpp = XMPPClient.getInstance(XMPPService.this, DOMAIN, userid, "1234");
        xmpp.connect("onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp.connection.disconnect();
    }

    public static boolean isNetworkConnected() {
        return cm.getActiveNetworkInfo() != null;
    }

    public static void sendMessage(ChatMessage msg) {
        if (xmpp != null) {
            xmpp.sendMessage(msg);
        }
    }
}