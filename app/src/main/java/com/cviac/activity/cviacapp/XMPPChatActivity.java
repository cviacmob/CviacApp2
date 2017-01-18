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

import com.cviac.com.cviac.app.adapaters.CircleTransform;
import com.cviac.com.cviac.app.adapaters.ConvMessageAdapter;
import com.cviac.com.cviac.app.datamodels.ChatMsg;
import com.cviac.com.cviac.app.datamodels.ConvMessage;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.fragments.ChatsFragment;
import com.cviac.com.cviac.app.xmpp.ChatMessage;
import com.cviac.com.cviac.app.xmpp.LocalBinder;
import com.cviac.com.cviac.app.xmpp.XMPPService;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class XMPPChatActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "XMPPChatActivity";

    private Conversation conv;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmppchat);


        Intent i = getIntent();
        conv = (Conversation) i.getSerializableExtra("conversewith");

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
                String converseId =  getNormalizedConverseId(myempId,conv.getEmpid());
                msgid = getMsgID();
                ChatMessage chat = new ChatMessage(converseId,myempId, conv.getEmpid(), geteditmgs, msgid, true);
                chat.setSenderName(myempname);
                XMPPService.sendMessage(chat);
                saveChatMessage(chat);
                edittxt.getText().clear();
            }
        });
        loadConvMessages();
    }

    private void loadConvMessages() {
        String converseId = getNormalizedConverseId(myempId, conv.getEmpid());
        chats = ConvMessage.getAll(converseId);
        chatAdapter = new ConvMessageAdapter(chats, this);
        lv.setAdapter(chatAdapter);
    }


    private String getNormalizedConverseId(String myid, String receverid) {
        if (myid.compareTo(receverid) > 0) {
            return myid + "_" + receverid;
        }
        return receverid + "_" + myid;
    }

    public void addInMessage(ConvMessage msg) {
        chats.add(msg);
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
            customimage = (ImageView) customView.findViewById(R.id.imageViewcustom);
            customimageback = (ImageView) customView.findViewById(R.id.imageViewback);

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
            TextView customTitle = (TextView) customView.findViewById(R.id.actionbarTitle);

            customTitle.setText(conv.getName());
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
/*
                    if (fromNotify == 1) {
                        Intent i = new Intent(XMPPChatActivity.this, HomeActivity.class);
                        startActivity(i);
                    }*/
                    Intent i = new Intent(XMPPChatActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            });


            // Apply the custom view
            actionBar.setCustomView(customView);
        }

    }

    public String getMsgID() {
        return System.currentTimeMillis()+"";
    }
}
