package com.cviac.activity.cviacapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cviac.com.cviac.app.adapaters.ConvMessageAdapter;
import com.cviac.com.cviac.app.datamodels.ChatMsg;
import com.cviac.com.cviac.app.datamodels.ConvMessage;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.fragments.ChatsFragment;
import com.cviac.com.cviac.app.xmpp.ChatMessage;
import com.cviac.com.cviac.app.xmpp.LocalBinder;
import com.cviac.com.cviac.app.xmpp.XMPPService;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class XMPPChatActivity extends AppCompatActivity {

    private static final String TAG = "XMPPChatActivity";

    private Conversation conv;

    private List<ConvMessage> chats;
    String geteditmgs;

    public String msgid;
    ImageButton img;
    EditText edittxt;
    ListView lv;

    private String myempId;
    private String myempname;

    private ConvMessageAdapter chatAdapter;


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

        //final String converseId = getNormalizedConverseId(myempId, conv.getEmpid());

        lv = (ListView) findViewById(R.id.listViewChat);
        img = (ImageButton) findViewById(R.id.sendbutton);
        edittxt = (EditText) findViewById(R.id.editTextsend);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geteditmgs = edittxt.getText().toString();
                ChatMessage chat = new ChatMessage(myempId, conv.getEmpid(), geteditmgs, msgid, true);
                chat.setMsgID();
                chat.setSenderName(myempname);
                XMPPService.sendMessage(chat);
                saveChatMessage(chat);
            }
        });
        loadConvMessages();
    }

    private void loadConvMessages() {
        chats = ConvMessage.getAll(conv.getEmpid());
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
        cmsg.setMsg(msg.body);
        cmsg.setCtime(new Date());
        cmsg.setFrom(msg.receiver);
        cmsg.setName(msg.senderName);
        cmsg.setReceiver(msg.sender);
        cmsg.setMsgid(msg.msgid);
        cmsg.setIn( (msg.isMine == true) ? false : true);
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
        cnv.setLastmsg(msg.body);
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
}
