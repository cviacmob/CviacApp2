package com.cviac.activity.cviacapp;

import android.*;
import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import com.cviac.com.cviac.app.datamodels.EmployeeInfo;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class XMPPChatActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "XMPPChatActivity";

    private Conversation conv;
    private List<GetStatus> getstatuslist;
    private List<ConvMessage> chats;
    String geteditmgs;
    private static final int MY_PERMISSION_CALL_PHONE = 10;
    public String msgid;
    ImageButton img;
    EditText edittxt;
    ListView lv;
    ActionBar actionBar;
    private String myempId;
    private String myempname;
    ImageView customimageback, customimage;
    private ConvMessageAdapter chatAdapter;
    TextView txt, msgview, presenceText;
    int fromNotify = 0;
    String converseId;
    Date lastseen;
    TextView customTitle,customduration;
    GetStatus empstatus;

    Timer timer;
    MyTimerTask myTimerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_xmppchat);
        setContentView(R.layout.activity_xmppchat);


        Intent i = getIntent();
        conv = (Conversation) i.getSerializableExtra("conversewith");
        empstatus = (GetStatus) i.getSerializableExtra("status");

        fromNotify = i.getIntExtra("fromnotify", 0);
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        myempId = prefs.getString("empid", "");
        myempname = prefs.getString("empname", "");

        CVIACApplication app = (CVIACApplication) getApplication();

        app.setChatActivty(this);
        actionmethod();


        lv = (ListView) findViewById(R.id.listViewChat);
        lv.setDivider(null);
        lv.setDividerHeight(5);
        img = (ImageButton) findViewById(R.id.sendbutton);
        edittxt = (EditText) findViewById(R.id.editTextsend);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geteditmgs = edittxt.getText().toString();
                if (!geteditmgs.equals("")) {
                    String converseId = getNormalizedConverseId(myempId, conv.getEmpid());
                    msgid = getMsgID();
                    ChatMessage chat = new ChatMessage(converseId, myempId, conv.getEmpid(), geteditmgs, msgid, true);
                    chat.setSenderName(myempname);
                    XMPPService.sendMessage(chat);
                    saveChatMessage(chat);
                    edittxt.getText().clear();

                    ChatMsg msg = new ChatMsg();
                    msg.setSenderid(myempId);
                    msg.setSendername(myempname);
                    msg.setMsg(geteditmgs);
                    msg.setMsgid(msgid);
                    msg.setReceiverid(conv.getEmpid());
                    checkAndSendPushNotfication(conv.getEmpid(),msg);
                }

            }
        });
        loadConvMessages();

        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1 * 60 *  1000);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
            final String strDate = simpleDateFormat.format(calendar.getTime());
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    XMPPService.updateSatus();
                    Toast.makeText(getApplicationContext(), "Timer Event", Toast.LENGTH_SHORT).show();
                }});
        }
    }

    private void loadConvMessages() {
        converseId = getNormalizedConverseId(myempId, conv.getEmpid());
        chats = ConvMessage.getAll(converseId);
        chatAdapter = new ConvMessageAdapter(chats, this);
        lv.setAdapter(chatAdapter);
    }

    public String getConverseId() {
        return converseId;
    }


    private String getNormalizedConverseId(String myid, String receverid) {
        if (myid.compareTo(receverid) > 0) {
            return myid + "_" + receverid;
        }
        return receverid + "_" + myid;
    }

    public void addInMessage(ConvMessage msg) {
        // chats.add(msg);
        loadConvMessages();
        chatAdapter.notifyDataSetChanged();
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
        app.setChatActivty(null);
        if (timer != null)
        timer.cancel();
        timer = null;
    }


    public void setMsgID() {
        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.progress:
                ImageView cuscall = (ImageView) findViewById(R.id.ivcall);
                onClick(cuscall);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Employee callemp = Employee.getemployee(conv.getEmpid());
        if (callemp != null && callemp.getMobile() != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + callemp.getMobile()));
            if (ContextCompat.checkSelfPermission(this, (android.Manifest.permission.CALL_PHONE))
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(XMPPChatActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSION_CALL_PHONE);
                return;
            }
            startActivity(callIntent);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_CALL_PHONE: {
                Employee callemp = Employee.getemployee(conv.getEmpid());
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + callemp.getMobile()));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                }
            }
        }
    }

    public void actionmethod() {

        actionBar = getActionBar();
        if (actionBar != null) {
            // Disable the default and enable the custom
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF4848")));

            View customView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            customTitle = (TextView) customView.findViewById(R.id.actionbarTitle);
            customimage = (ImageView) customView.findViewById(R.id.imageViewcustom);
            customimageback = (ImageView) customView.findViewById(R.id.imageViewback);
            customduration = (TextView) customView.findViewById(R.id.duration);
            //lastseen();
            String url1 = conv.getImageurl();
            if (url1 != null && url1.length() > 0) {
                Picasso.with(this).load(conv.getImageurl()).resize(100, 100).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(customimage);
            } else {
                Employee emp = Employee.getemployee(conv.getEmpid());
                conv.setImageurl(emp.getImage_url());
                if (emp.getGender().equalsIgnoreCase("female")) {
                    Picasso.with(this).load(R.drawable.female).resize(100, 100).transform(new CircleTransform())
                            .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(customimage);
                } else {
                    Picasso.with(this).load(R.drawable.ic_boy).resize(100, 100).transform(new CircleTransform())
                            .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(customimage);
                }

            }

            Picasso.with(this).load(R.drawable.backarrow).resize(90, 90)
                    .into(customimageback);


            // Get the textview of the title
         ;

            customTitle.setText(conv.getName());

            customduration.setText(empstatus.getStatus());

            // Change the font family (optional)

            // Set the on click listener for the title
            customTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.w("MainActivity", "ActionBar's title clicked.");
                    Intent i = new Intent(XMPPChatActivity.this, MyProfileActivity.class);
                    i.putExtra("empcode", conv.getEmpid());
                    startActivity(i);
                    finish();
                }
            });
            customimageback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (fromNotify == 1) {
                        Intent i = new Intent(XMPPChatActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
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

    public void updateMessageStatus(String msgId, int status) {
//        for (ConvMessage msg : chats) {
//            if (msg.getMsgid().equalsIgnoreCase(msgId)) {
//                msg.setStatus(status);
//                break;
//            }
//        }
        loadConvMessages();
        chatAdapter.notifyDataSetChanged();

    }



    private Date lastactivity(Date lasttime) {

        Date getdate=lasttime;
        String timeStam = new SimpleDateFormat("dd-MM-yy").format(getdate);

        return lastactivity(lasttime);
    }

    private void SendPushNotification(ChatMsg cmsg,String pushid) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        CVIACApi api = retrofit.create(CVIACApi.class);
        String key = "key=AAAA_01YtJE:APA91bEUGI2jvP6PqBQXuV35rN6yHjUkCYshgQGHvuZkPaYwkRqSmSuukmxnaAbunLQgb_ALrd6ZonqteEjaZ34AD7quSa4-1NZdpzA4fvvfGSYwVLNR-FzvnJlVvA2h-TnGLKka3vO_eAr52shm29VA0XHvFm9SWQ";
        PushMessageInfo pinfo = new PushMessageInfo();
        pinfo.setTo(pushid);
        PushMessageInfo.DataInfo dinfo = new PushMessageInfo.DataInfo();
        dinfo.setMsg(cmsg.getMsg());
        dinfo.setSendername(cmsg.getSendername());
        dinfo.setSenderid(cmsg.getSenderid());
        dinfo.setMsgId(cmsg.getMsgid());
        pinfo.setData(dinfo);
        final Call<FCMSendMessageResponse> call = api.sendPushMessage(key, pinfo);
        call.enqueue(new Callback<FCMSendMessageResponse>() {
            @Override
            public void onResponse(Response<FCMSendMessageResponse> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void checkAndSendPushNotfication(String empCode,final ChatMsg msg) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
        Retrofit ret = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        CVIACApi api = ret.create(CVIACApi.class);
        final Call<GetStatus> call = api.getstatus(empCode);
        call.enqueue(new Callback<GetStatus>() {
            @Override
            public void onResponse(Response<GetStatus> response, Retrofit retrofit) {
                GetStatus status = response.body();
                if (status.getStatus() != null && status.getStatus().equalsIgnoreCase("offline")) {
                    SendPushNotification(msg,status.getPush_id());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }
}
