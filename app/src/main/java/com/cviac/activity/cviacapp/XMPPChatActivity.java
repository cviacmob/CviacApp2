package com.cviac.activity.cviacapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cviac.com.cviac.app.xmpp.ChatMessage;
import com.cviac.com.cviac.app.xmpp.LocalBinder;
import com.cviac.com.cviac.app.xmpp.XMPPService;

import java.util.List;
import java.util.Random;

public class XMPPChatActivity extends AppCompatActivity {

    private static final String TAG = "XMPPChatActivity";

    private List<ChatMessage> chats;
    String geteditmgs;

    public String msgid;
    ImageButton img;
    EditText edittxt;

    private XMPPService mService;

    private boolean mBounded;

    private final ServiceConnection mConnection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mService = ((LocalBinder<XMPPService>) service).getService();
            mBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };


    private void sendMessage(ChatMessage msg) {
        getmService().xmpp.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmppchat);
        doBindService();

         img=(ImageButton)findViewById(R.id.sendbutton);
        edittxt=(EditText)findViewById(R.id.editTextsend) ;
        geteditmgs=edittxt.getText().toString();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ChatMessage chat=new ChatMessage("cc0089","cc0101",geteditmgs,msgid,true);
                sendMessage(chat);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    void doBindService() {
        bindService(new Intent(this, XMPPService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    public XMPPService getmService() {
        return mService;
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }
}
