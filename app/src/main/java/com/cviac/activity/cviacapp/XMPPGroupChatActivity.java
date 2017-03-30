package com.cviac.activity.cviacapp;

import android.*;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cviac.com.cviac.app.adapaters.CircleTransform;
import com.cviac.com.cviac.app.adapaters.ConvMessageAdapter;
import com.cviac.com.cviac.app.datamodels.ChatMsg;
import com.cviac.com.cviac.app.datamodels.ConvMessage;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.fragments.ChatsFragment;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.cviac.com.cviac.app.restapis.FCMSendMessageResponse;
import com.cviac.com.cviac.app.restapis.GetStatus;
import com.cviac.com.cviac.app.restapis.PushMessageInfo;
import com.cviac.com.cviac.app.xmpp.ChatMessage;
import com.cviac.com.cviac.app.xmpp.LocalBinder;
import com.cviac.com.cviac.app.xmpp.XMPPService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class XMPPGroupChatActivity extends Activity  {

    private static final String TAG = "XMPPGroupChatActivity";
    private Conversation conv;
    private List<ConvMessage> chats;
    String geteditmgs;
    public String msgid;
    ImageButton img;
    EditText edittxt;
    ListView lv;
    ActionBar actionBar;
    private String myempId;
    private String myempname;
    String status;
    ImageView customimageback;
    ImageView customimage;
    private ConvMessageAdapter chatAdapter;
    TextView txt, msgview, presenceText;
    int fromNotify = 0;
    String converseId;
    Date lastseen;
    TextView customTitle, customduration;
    GetStatus empstatus;
    CVIACApplication app;
    Timer timer;
    XMPPChatActivity.MyTimerTask myTimerTask;
    private BroadcastReceiver xmppConnReciver;
    Context mcontext;
    private XMPPService mService;
    private boolean mBounded;
    private MultiUserChat mMultiUserChat;

    static Date date = null;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmppchat);


        Intent i = getIntent();
        conv = (Conversation) i.getSerializableExtra("conversewith");

        fromNotify = i.getIntExtra("fromnotify", 0);
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        myempId = prefs.getString("empid", "");
        myempname = prefs.getString("empname", "");

        app = (CVIACApplication) getApplication();
        app.setGroupChatActivity(this);
        actionmethod();
        doBindService();

        lv = (ListView) findViewById(R.id.listViewChat);
        lv.setDivider(null);
        img = (ImageButton) findViewById(R.id.sendbutton);
        edittxt = (EditText) findViewById(R.id.editTextsend);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geteditmgs = edittxt.getText().toString();
                if (XMPPService.isNetworkConnected() && XMPPService.xmpp.isConnected()) {
                    if (!geteditmgs.equals("")) {
                        String converseId = conv.getEmpid();
                        msgid = "GROUPMSG:"+getMsgID();
                        ChatMessage chat = new ChatMessage(converseId, myempId, converseId, geteditmgs, msgid, true);
                        chat.setSenderName(myempname);
                        XMPPService.sendGroupMessage(conv.getEmpid(),geteditmgs);
                        saveChatMessage(chat);
                        edittxt.getText().clear();
                    }

                } else {
                    Toast.makeText(XMPPGroupChatActivity.this,
                            "Check your internet connection", Toast.LENGTH_LONG).show();
                }


            }
        });
        loadConvMessages();
    }

    private void loadConvMessages() {
        chats = ConvMessage.getAll(conv.getEmpid());
        chatAdapter = new ConvMessageAdapter(chats, this);
        lv.setAdapter(chatAdapter);
    }

    public void addInMessage(ConvMessage msg) {
        // chats.add(msg);
        loadConvMessages();
        chatAdapter.notifyDataSetChanged();
    }

    public String getConverseId() {
       return  conv.getEmpid();
    }

    public void saveChatMessage(ChatMessage msg) {
        ConvMessage cmsg = new ConvMessage();
        cmsg.setMsg(msg.msg);
        cmsg.setCtime(new Date());
        cmsg.setConverseid(msg.converseid);
        cmsg.setSenderName(msg.senderName);
        cmsg.setReceiver(msg.receiver);
        cmsg.setSender(msg.sender);
        cmsg.setMsgid(msg.msgid);
        cmsg.setMine(msg.isMine);
        cmsg.setMine(true);
        cmsg.setStatus(1);
        cmsg.save();
        saveLastConversationMessage(msg);
        chats.add(cmsg);
        chatAdapter.notifyDataSetChanged();
    }

    private void saveLastConversationMessage(ChatMessage msg) {
        CVIACApplication app = (CVIACApplication) getApplication();
        ChatsFragment chatFrag = app.getChatsFragment();
        Conversation cnv = Conversation.getConversation(conv.getEmpid());
        boolean newconv = false;
        if (cnv == null) {
            cnv = new Conversation();
            newconv = true;
        }
        cnv.setEmpid(conv.getEmpid());
        cnv.setImageurl(conv.getImageurl());
        cnv.setName(conv.getName());
        cnv.setDatetime(new Date());
        cnv.setLastmsg(msg.msg);
        cnv.save();
        if (chatFrag != null && chatFrag.adapter != null) {
            chatFrag.reloadConversation();
        }
    }

    @Override
    protected void onDestroy() {
        CVIACApplication app = (CVIACApplication) getApplication();
        super.onDestroy();
        doUnbindService();
        app.setGroupChatActivity(null);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.progress:
//                ImageView cuscall = (ImageView) findViewById(R.id.ivcall);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void actionmethod() {

        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB1924")));

            final View customView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            customTitle = (TextView) customView.findViewById(R.id.actionbarTitle);
            customimage = (ImageView) customView.findViewById(R.id.imageViewcustom);
            customimageback = (ImageView) customView.findViewById(R.id.imageViewback);
            customduration = (TextView) customView.findViewById(R.id.duration);
            String url1 = conv.getImageurl();
            if (url1 != null && url1.length() > 0) {
                if (url1.startsWith("http://groupicon")) {
                    Picasso.with(this).load(R.drawable.groupicon).resize(100, 100).transform(new CircleTransform())
                            .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(customimage);
                }else {
                    Picasso.with(this).load(conv.getImageurl()).resize(100, 100).transform(new CircleTransform())
                            .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(customimage);
                }
            }
            Picasso.with(this).load(R.drawable.backarrow).resize(90, 90)
                    .into(customimageback);
            customTitle.setText(conv.getName());

            // Set the on click listener for the title
            customTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent in = new Intent(XMPPGroupChatActivity.this,GroupInfoActivity.class);
                    in.putExtra("groupid",conv.getEmpid());
                    in.putExtra("groupname",conv.getName());
                 startActivity(in);
                }
            });
            customimageback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            // Apply the custom view
            actionBar.setCustomView(customView);
        }

    }

    public String getMsgID() {
        return System.currentTimeMillis() + "";
    }

    void doBindService() {
        xmppConnReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                status = intent.getStringExtra("status");
                if (status != null && status.equalsIgnoreCase("connected")) {
                    img.setBackgroundResource(R.drawable.send);
                } else if (status != null && status.equalsIgnoreCase("Disconnected")) {
                    img.setBackgroundResource(R.drawable.send_red);
                }

            }
        };
        bindService(new Intent(this, XMPPService.class), mConnection,
                Context.BIND_AUTO_CREATE);

        registerReceiver(xmppConnReciver, new IntentFilter("XMPPConnection"));
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
            unregisterReceiver(xmppConnReciver);
        }
    }
}

