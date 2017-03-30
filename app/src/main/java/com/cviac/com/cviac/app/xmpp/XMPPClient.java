package com.cviac.com.cviac.app.xmpp;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.cviac.activity.cviacapp.CVIACApplication;
import com.cviac.activity.cviacapp.FireChatActivity;
import com.cviac.activity.cviacapp.R;
import com.cviac.activity.cviacapp.Verification;
import com.cviac.activity.cviacapp.XMPPChatActivity;
import com.cviac.activity.cviacapp.XMPPGroupChatActivity;
import com.cviac.com.cviac.app.datamodels.ConvMessage;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.GroupInfo;
import com.cviac.com.cviac.app.datamodels.GroupMemberInfo;
import com.cviac.com.cviac.app.fragments.ChatsFragment;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.cviac.com.cviac.app.restapis.GeneralResponse;
import com.cviac.com.cviac.app.restapis.UpdateStatusInfo;
import com.google.gson.Gson;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.parsing.ExceptionLoggingCallback;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static android.R.attr.resource;
import static android.R.attr.toScene;
import static android.content.Context.MODE_PRIVATE;

public class XMPPClient implements StanzaListener {

    public boolean connected = false;
    public boolean loggedin = false;
    public boolean isconnecting = false;
    public static boolean isToasted = true;
    private boolean chat_created = false;
    private String serverAddress;
    public static XMPPTCPConnection connection;
    public static String loginUser;
    public static String passwordUser;
    Gson gson;
    XMPPService context;
    public static XMPPClient instance = null;
    public static boolean instanceCreated = false;
    String onlinestatus = "online";
    String offlinestatus = "offline";
    Date onlinestatusdate = new Date();
    //String onlinestatus=onlinestatusdate.toString();
    // String offline=onlinestatusdate.toString();
    String mobile, emp_namelogged;
    CVIACApplication application;
    private int msgcounter = 0;
    Employee emplogged;
    int counter = 0;

    public XMPPClient(XMPPService context, String serverAdress, String logiUser,
                      String passwordser) {
        this.serverAddress = serverAdress;
        this.loginUser = logiUser;
        this.passwordUser = passwordser;
        this.context = context;
        init();

    }


    public static XMPPClient getInstance(XMPPService context, String server,
                                         String user, String pass) {

        if (instance == null) {
            instance = new XMPPClient(context, server, user, pass);
            instanceCreated = true;
        }
        return instance;

    }

    public boolean isConnected() {
        return connected;
    }



    public org.jivesoftware.smack.chat.Chat Mychat;

    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;

    String text = "";
    String mMessage = "", mReceiver = "";

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");

        } catch (ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    public void init() {
        gson = new Gson();
        mMessageListener = new MMessageListener(context);
        mChatManagerListener = new ChatManagerListenerImpl();
        initialiseConnection();

    }

    private void initialiseConnection() {

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(serverAddress);
        config.setHost(serverAddress);
        config.setPort(5222);
        config.setDebuggerEnabled(true);


        onReady(config);

//        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
//        XMPPTCPConnection.setUseStreamManagementDefault(true);
//        connection = new XMPPTCPConnection(config.build());
//        connection.setPacketReplyTimeout(10000);
//        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
//        connection.addConnectionListener(connectionListener);
    }

    @Override
    public void processPacket(Stanza packet) throws NotConnectedException {

    }

    static class AcceptAll implements StanzaFilter {
        @Override
        public boolean accept(Stanza packet) {
            return true;
        }
    }

    private void onReady(XMPPTCPConnectionConfiguration.Builder builder) {
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
        builder.setCompressionEnabled(false);
        builder.setSendPresence(true);

        try {
            if (SettingsManager.securityCheckCertificate()) {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                MemorizingTrustManager mtm = new MemorizingTrustManager(context);
                sslContext.init(null, new X509TrustManager[]{mtm}, new java.security.SecureRandom());
                builder.setCustomSSLContext(sslContext);
                builder.setHostnameVerifier(
                        mtm.wrapHostnameVerifier(new org.apache.http.conn.ssl.StrictHostnameVerifier()));
            } else {
                TLSUtils.acceptAllCertificates(builder);
                TLSUtils.disableHostnameVerificationForTlsCertificicates(builder);
            }
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }


        setUpSASL();

        connection = new XMPPTCPConnection(builder.build());
        //ReconnectionManager.getInstanceFor(connection).enableAutomaticReconnection();
        connection.addAsyncStanzaListener(this, new AcceptAll());
        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
        // by default Smack disconnects in case of parsing errors
        connection.setParsingExceptionCallback(new ExceptionLoggingCallback());
//        AccountRosterListener rosterListener = new AccountRosterListener(((AccountItem)connectionItem).getAccount());
//        final Roster roster = Roster.getInstanceFor(xmppConnection);
//        roster.addRosterListener(rosterListener);
//        roster.addRosterLoadedListener(rosterListener);
//        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
//        org.jivesoftware.smackx.ping.PingManager.getInstanceFor(xmppConnection).registerPingFailedListener(this);
    }

    private void setUpSASL() {
        if (SettingsManager.connectionUsePlainTextAuth()) {
            final Map<String, String> registeredSASLMechanisms = SASLAuthentication.getRegisterdSASLMechanisms();
            for (String mechanism : registeredSASLMechanisms.values()) {
                SASLAuthentication.blacklistSASLMechanism(mechanism);
            }

            SASLAuthentication.unBlacklistSASLMechanism(SASLPlainMechanism.NAME);

        } else {
            final Map<String, String> registeredSASLMechanisms = SASLAuthentication.getRegisterdSASLMechanisms();
            for (String mechanism : registeredSASLMechanisms.values()) {
                SASLAuthentication.unBlacklistSASLMechanism(mechanism);
            }
        }
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public void connect(final String caller) {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (connection.isConnected())
                    return false;
                isconnecting = true;
                if (isToasted)
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {

//                            Toast.makeText(context,
//                                    caller + "=>connecting....",
//                                    Toast.LENGTH_LONG).show();
                        }
                    });
                Log.d("Connect() Function", caller + "=>connecting....");

                try {
                    connection.connect();
                    DeliveryReceiptManager dm = DeliveryReceiptManager
                            .getInstanceFor(connection);
                    dm.setAutoReceiptMode(AutoReceiptMode.always);
                    dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                        @Override
                        public void onReceiptReceived(final String fromid,
                                                      final String toid, final String msgid,
                                                      final Stanza packet) {

                        }
                    });
                    connected = true;

                } catch (IOException e) {
                    if (isToasted)
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(
                                                context,
                                                "(" + caller + ")"
                                                        + "IOException: ",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                    Log.e("(" + caller + ")", "IOException: " + e.getMessage());
                } catch (final SmackException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
//                            Toast.makeText(context,
//                                    "(" + caller + ")" + "SMACKException::: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.e("(" + caller + ")",
                            "SMACKException: " + e.getMessage());
                } catch (XMPPException e) {
                    if (isToasted)

                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(
                                                context,
                                                "(" + caller + ")"
                                                        + "XMPPException: ",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    Log.e("connect(" + caller + ")",
                            "XMPPException: " + e.getMessage());

                }
                return isconnecting = false;
            }
        };
        connectionThread.execute();
    }

    private String generateResource() {

        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String resource = prefs.getString("resource", "");
        if (resource.isEmpty()) {
            resource = context.getString(R.string.account_resource_default) + "_" + StringUtils.randomString(8);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("resource", resource);
            editor.commit();
        }
        return resource;
    }
    public void login() {

        try {
            connection.login(loginUser, passwordUser,generateResource());
            Log.i("LOGIN", "Yey! We're connected to the Xmpp server!");

        } catch (XMPPException | SmackException | IOException e) {

            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"LOGIN FAILED: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
        }
        listenForGroupMessages();
    }

    private class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                                final boolean createdLocally) {
            if (!createdLocally)
                chat.addMessageListener(mMessageListener);

        }

    }

    public void sendMessage(ChatMessage chatMessage) {
        String body = gson.toJson(chatMessage);
        Mychat = ChatManager.getInstanceFor(connection).createChat(
                chatMessage.receiver + "@"
                        + context.getString(R.string.server),
                mMessageListener);
        final Message message = new Message();
        message.setBody(body);
        message.setStanzaId(chatMessage.msgid);
        message.setType(Message.Type.chat);


        try {
            if (connection.isAuthenticated()) {
                Mychat.sendMessage(message);
            } else {
                login();
            }
        } catch (NotConnectedException e) {
            Log.e("xmpp.SendMessage()", "msg Not sent!-Not Connected!");

        } catch (Exception e) {
            Log.e("xmppException",
                    "msg Not sent!" + e.getMessage());
        }

    }

    public void sendAckMessage(ChatMessage chatMessage) {
        chatMessage.ack = 1;
        chatMessage.msg = "";
        String body = gson.toJson(chatMessage);
        Mychat = ChatManager.getInstanceFor(connection).createChat(
                chatMessage.sender + "@"
                        + context.getString(R.string.server),
                mMessageListener);

        final Message message = new Message();
        message.setBody(body);
        message.setStanzaId(chatMessage.msgid);
        message.setType(Message.Type.normal);

        try {
            if (connection.isAuthenticated()) {

                Mychat.sendMessage(message);

            } else {

                login();
            }
        } catch (NotConnectedException e) {
            Log.e("xmpp.SendMessage()", "msg Not sent!-Not Connected!");

        } catch (Exception e) {
            Log.e("xmppException",
                    "msg Not sent!" + e.getMessage());
        }

    }

    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {

            Log.d("xmpp", "Connected!");
            connected = true;
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        updateStatus(offlinestatus);
                        //Toast.makeText(context, "ConnectionCLosed!",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setAction("XMPPConnection");
                        intent.putExtra("status", "DisConnected");
                        context.sendBroadcast(intent);
                    }
                });
            Log.d("xmpp", "ConnectionCLosed!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        //Toast.makeText(context, "ConnectionClosedOn Error!!",
                        //        Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction("XMPPConnection");
                        intent.putExtra("status", "DisConnected");
                        context.sendBroadcast(intent);

                    }
                });
            Log.d("xmpp", "ConnectionClosedOn Error!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int arg0) {

            Log.d("xmpp", "Reconnectingin " + arg0);

            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        //Toast.makeText(context, "ReconnectionFailed!",
                        //        Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionFailed!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        //Toast.makeText(context, "REConnected!",
                        //        Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionSuccessful");
            connected = true;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("xmpp", "Authenticated!");
            loggedin = true;

            ChatManager.getInstanceFor(connection).addChatListener(
                    mChatManagerListener);

            chat_created = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {


                        // TODO Auto-generated method stub
//                        Toast.makeText(context, "Connected!",
//                                Toast.LENGTH_SHORT).show();
                        updateStatus(onlinestatus);

                        Intent intent = new Intent();
                        intent.setAction("XMPPConnection");
                        intent.putExtra("status", "Connected");
                        context.sendBroadcast(intent);
                    }
                });
        }
    }

    private class MMessageListener implements ChatMessageListener {

        public MMessageListener(Context contxt) {
        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                    + message);

            if (message.getType() == Message.Type.chat
                    && message.getBody() != null) {
                final ChatMessage chatMessage = gson.fromJson(
                        message.getBody(), ChatMessage.class);
                if (chatMessage.ack == 1) {
                    String msgId = chatMessage.msgid;
                    ConvMessage.updateStatus(msgId, 2);
                    updateMessageStatusInUI(msgId, 2);
                } else {

                    if (chatMessage.msgid.startsWith("GROUPNEW:") ) {
                        joinGroup(chatMessage);
                    }
                    else if (chatMessage.msgid.startsWith("BotMsg:")) {
                        XMPPClient.this.processMessage(chatMessage);
                    }
                    else {
                        XMPPClient.this.processMessage(chatMessage);
                        sendAckMessage(chatMessage);
                    }
                }
            }
        }

        private void updateMessageStatusInUI(final String msgId, final int status) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    CVIACApplication app = (CVIACApplication) context.getApplication();
                    if (app != null) {
                        XMPPChatActivity actv = app.getChatActivty();
                        if (actv != null) {
                            actv.updateMessageStatus(msgId, status);
                        }
                    }
                }
            });
        }

    }

    public void processMessage(final ChatMessage msg) {
        final ConvMessage cmsg = new ConvMessage();
        cmsg.setMsg(msg.msg);
        cmsg.setCtime(new Date());
        //  cmsg.setCtime(msg.ctime);
        cmsg.setMine(false);
        cmsg.setSender(msg.sender);
        cmsg.setSenderName(msg.senderName);
        cmsg.setConverseid(msg.converseid);
        cmsg.setReceiver(msg.receiver);
        cmsg.setMsgid(msg.msgid);
        cmsg.setStatus(-1);

        try {
            cmsg.save();
            saveLastConversationMessage(msg);
        } catch (Exception e) {
        }
        // Chats.chatlist.add(chatMessage);
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                CVIACApplication app = (CVIACApplication) context.getApplication();
                ChatsFragment chatFrag = app.getChatsFragment();
                if (chatFrag != null && chatFrag.adapter != null) {
                    chatFrag.reloadConversation();
                }

                if (app != null) {
                    XMPPChatActivity actv = app.getChatActivty();
                    if (actv != null) {
                        String convId = actv.getConverseId();
                        if (convId.equalsIgnoreCase(msg.converseid)) {
                            actv.addInMessage(cmsg);
                            return;
                        }
                    }
                }
                showMsgNotification(cmsg);
            }
        });
    }

    private void saveLastConversationMessage(ChatMessage msg) {
        CVIACApplication app = (CVIACApplication) context.getApplication();
        ChatsFragment chatFrag = app.getChatsFragment();
        Conversation cnv = Conversation.getConversation(msg.sender);
        boolean newconv = false;
        if (cnv == null) {
            cnv = new Conversation();
            cnv.setReadcount(1);
        }else {
            // CVIACApplication app = (CVIACApplication) context.getApplication();
            XMPPChatActivity actv = app.getChatActivty();
            if (actv != null) {
                String convId = actv.getConverseId();
                if (convId.equalsIgnoreCase(msg.converseid)) {
                    cnv.setReadcount(0);
                }
                else {
                    cnv.setReadcount(cnv.getReadcount()+1);
                }
            }
            else {
                cnv.setReadcount(cnv.getReadcount()+1);
            }
        }
        cnv.setEmpid(msg.sender);
        // cnv.setImageurl(conv.getImageurl());
        cnv.setName(msg.senderName);
        cnv.setDatetime(new Date());
        cnv.setLastmsg(msg.msg);
        cnv.save();

    }

    private void showMsgNotification(ConvMessage cmsg) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cviac_logo)
                        .setContentTitle(cmsg.getSenderName())
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentText(cmsg.getMsg());
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        Intent resultIntent = new Intent(context, XMPPChatActivity.class);

        Conversation cnv = new Conversation();
        cnv.setEmpid(cmsg.getSender());
        cnv.setName(cmsg.getSenderName());
        Employee code= Employee.getemployee(cmsg.getSender());
        if (code != null) {
            cnv.setImageurl(code.getImage_url());
        }
        resultIntent.putExtra("conversewith",cnv);
        resultIntent.putExtra("fromnotify",1);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(FireChatActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(cnv.getEmpid(), 0, mBuilder.build());
    }

    public void updateStatus(String status) {
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mobile = prefs.getString("mobile", "");
        emplogged = Employee.getemployeeByMobile(mobile);
        emp_namelogged = emplogged.getEmp_code();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CVIACApi api = retrofit.create(CVIACApi.class);
        UpdateStatusInfo statusinfo = new UpdateStatusInfo();
        statusinfo.setEmp_code(emp_namelogged);
        // statusinfo.setStatus(new Date().toString());
        statusinfo.setStatus(status);

        Call<GeneralResponse> call = api.updatestatus(statusinfo);
        call.enqueue(new retrofit.Callback<GeneralResponse>() {
            @Override
            public void onResponse(retrofit.Response<GeneralResponse> response, Retrofit retrofit) {
                GeneralResponse rsp = response.body();
                //Toast.makeText(context, "update status Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                // Toast.makeText(context, "update status failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void createGroup(String group,List<String> members) {
        List<String> joinedRooms = null;
        MultiUserChatManager mngr = MultiUserChatManager.getInstanceFor(connection);
        String roomname = this.loginUser + "_" + group;
        MultiUserChat muc = mngr.getMultiUserChat(roomname+"@conference." + serverAddress);
        try {
            muc.createOrJoin(group);
            Form form = muc.getConfigurationForm();
            Form submitForm = form.createAnswerForm();
            List<FormField> formFieldList = submitForm.getFields();
            for (FormField formField : formFieldList) {
                if(!FormField.Type.hidden.equals(formField.getType()) && formField.getVariable() != null) {
                    submitForm.setDefaultAnswer(formField.getVariable());
                }
            }
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            submitForm.setAnswer("muc#roomconfig_publicroom", true);
            muc.sendConfigurationForm(submitForm);
            muc.join(group);

            GroupMemberInfo gmi=new GroupMemberInfo();
            gmi.setMember_id(loginUser);
            gmi.setInvitedate(new Date());
            gmi.setGroup_id(roomname);
            gmi.save();

            String invts = getGroupMembersListCommaSepartate(members);
            for (String mem : members) {
                gmi=new GroupMemberInfo();
                gmi.setMember_id(mem);
                gmi.setInvitedate(new Date());
                gmi.setGroup_id(roomname);
                gmi.save();

                String id = "GROUPNEW:"+ System.currentTimeMillis();
                ChatMessage msg = new ChatMessage(roomname, roomname, mem, loginUser+":"+roomname+":"+group+":"+invts,id, true  );
                msg.setSenderName(group);
                sendMessage(msg);
            }
            Conversation cnv = new Conversation();
            cnv.setImageurl("http://groupicon");
            cnv.setReadcount(0);
            cnv.setEmpid(roomname);
            cnv.setName(group);
            cnv.setDatetime(new Date());
            cnv.setLastmsg("group created");
            cnv.save();
            CVIACApplication app = (CVIACApplication) context.getApplication();
            ChatsFragment chatFrag = app.getChatsFragment();
            if (chatFrag != null && chatFrag.adapter != null) {
                chatFrag.reloadConversation();
            }
            GroupInfo info = new GroupInfo();
            info.setName(group);
            info.setGrpID(roomname);
            info.setCreatedDate(new Date());
            info.setOwner(loginUser);
            info.save();

        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getGroupMembersListCommaSepartate(List<String> members){
        StringBuffer bf = new StringBuffer();
        for (String mem : members) {
            bf.append(mem);
            bf.append(",");
        }
        bf.append(loginUser);
        return  bf.toString();
    }

    public void joinGroup(ChatMessage chatMessage) {
        String params[] = chatMessage.msg.split(":");
        if (params.length < 3) {
            return;
        }
        String adminame = params[0];
        String roomname = params[1];
        final String nickname = params[2];



        try {
            MultiUserChatManager mngr = MultiUserChatManager.getInstanceFor(connection);
            MultiUserChat muc = mngr.getMultiUserChat(roomname+"@conference." + serverAddress);
            //if (!muc.isJoined())
            {
                muc.join(nickname + "_" + loginUser);
            }
            muc.addMessageListener(new MessageListener() {
                @Override
                public void processMessage(Message message) {
                    saveGroupMessage(nickname,message);
                }
            });

            GroupInfo info = new GroupInfo();
            info.setName(nickname);
            info.setGrpID(roomname);
            info.setCreatedDate(new Date());
            info.setOwner(adminame);
            info.save();

            if (params.length  == 4) {
                String  invts = params[3];
                saveGroupMembers(roomname,invts);
            }

            Conversation cnv = new Conversation();
            cnv.setReadcount(0);
            cnv.setEmpid(roomname);
            cnv.setImageurl("http://groupicon");
            cnv.setName(nickname);
            cnv.setDatetime(new Date());
            cnv.setLastmsg("New Group");
            cnv.save();

            CVIACApplication app = (CVIACApplication) context.getApplication();
            ChatsFragment chatFrag = app.getChatsFragment();
            if (chatFrag != null && chatFrag.adapter != null) {
                chatFrag.reloadConversation();
            }

            toastMessage("Joined:"+nickname);
            return;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }
        toastMessage("Join failed:"+nickname);
    }

    private void saveGroupMembers(String groupId, String invts) {
        String [] mems = invts.split(",");
        for (String mem : mems) {
            GroupMemberInfo gmi=new GroupMemberInfo();
            gmi.setMember_id(mem);
            gmi.setInvitedate(new Date());
            gmi.setGroup_id(groupId);
            gmi.save();
        }
    }


    public void sendGroupMessage(String roomname, String msg) {
        Message message = new Message(roomname, Message.Type.groupchat);
        String msgId = "GROUPMSG:" + System.currentTimeMillis();
        ChatMessage cmsg = new ChatMessage(roomname,loginUser,roomname,msg,msgId,true);
        cmsg.setSenderName(emplogged.getEmp_name());
        String body = gson.toJson(cmsg);
        message.setBody(body);
        message.setType(Message.Type.groupchat);
        message.setTo(roomname);
        MultiUserChatManager mngr = MultiUserChatManager.getInstanceFor(connection);
        MultiUserChat muc = mngr.getMultiUserChat(roomname+"@conference." + serverAddress);
        try {
            muc.sendMessage(message);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void listenForGroupMessages() {
        MultiUserChatManager mngr = MultiUserChatManager.getInstanceFor(connection);
        List<GroupInfo> groups = GroupInfo.getGroups();
        if (groups != null) {
            for (final GroupInfo grpInfo : groups) {
                MultiUserChat muc = mngr.getMultiUserChat(grpInfo.getGrpID() + "@conference." + serverAddress);
                try {
                   // if (!muc.isJoined())
                    {
                        DiscussionHistory history = new DiscussionHistory();
                        history.setMaxStanzas(0);
                        muc.join(grpInfo.getName() + "_" + loginUser,
                                null,
                                history,
                                SmackConfiguration.getDefaultPacketReplyTimeout());
                    }
                } catch (Exception e) {
                    toastMessage("group listen failed:"+ grpInfo.getName());
                }
                muc.addMessageListener(new MessageListener() {
                    @Override
                    public void processMessage(Message message) {
                        saveGroupMessage(grpInfo.getName(),message);
                    }
                });
            }
        }
    }

    private void saveLastGroupConversationMessage(ChatMessage msg) {
        CVIACApplication app = (CVIACApplication) context.getApplication();
        ChatsFragment chatFrag = app.getChatsFragment();
        Conversation cnv = Conversation.getConversation(msg.converseid);
        boolean newconv = false;
        if (cnv == null) {
            cnv = new Conversation();
            cnv.setImageurl("http://groupicon");
            cnv.setReadcount(1);
            newconv = true;
        }
        else {
            XMPPGroupChatActivity actv = app.getGroupChatActivity();
            if (actv != null) {
                String convId = actv.getConverseId();
                if (convId.equalsIgnoreCase(msg.converseid)) {
                    cnv.setReadcount(0);
                }
                else {
                    cnv.setReadcount(cnv.getReadcount()+1);
                }
            }
            else {
                cnv.setReadcount(cnv.getReadcount()+1);
            }
        }
        cnv.setEmpid(msg.converseid);
        cnv.setDatetime(new Date());
        cnv.setLastmsg(msg.msg);
        cnv.save();

    }


    public void processGroupMessage(final String groupName,final ChatMessage msg) {
        final ConvMessage cmsg = new ConvMessage();
        cmsg.setMsg(msg.msg);
        cmsg.setCtime(new Date());
        cmsg.setMine(false);
        cmsg.setSender(msg.sender);
        cmsg.setSenderName(msg.senderName);
        cmsg.setConverseid(msg.converseid);
        cmsg.setReceiver(msg.receiver);
        cmsg.setMsgid(msg.msgid);
        cmsg.setStatus(-1);
        try {
            cmsg.save();
            saveLastGroupConversationMessage(msg);
        } catch (Exception e) {
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                CVIACApplication app = (CVIACApplication) context.getApplication();
                ChatsFragment chatFrag = app.getChatsFragment();
                if (chatFrag != null && chatFrag.adapter != null) {
                    chatFrag.reloadConversation();
                }
                if (app != null) {
                    XMPPGroupChatActivity actv = app.getGroupChatActivity();
                    if (actv != null) {
                        String convId = actv.getConverseId();
                        if (convId.equalsIgnoreCase(msg.converseid)) {
                            actv.addInMessage(cmsg);
                            return;
                        }
                    }
                }
                showGroupMsgNotification(groupName, cmsg);
            }
        });
    }

    private void showGroupMsgNotification(String groupName,ConvMessage cmsg) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cviac_logo)
                        .setContentTitle(groupName)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentText(cmsg.getMsg());
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        Intent resultIntent = new Intent(context, XMPPGroupChatActivity.class);

        Conversation cnv = new Conversation();
        cnv.setEmpid(cmsg.getConverseid());
        cnv.setName(groupName);
        cnv.setImageurl("http://groupicon");
        resultIntent.putExtra("conversewith",cnv);
        resultIntent.putExtra("fromnotify",1);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(FireChatActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(cnv.getEmpid(), 0, mBuilder.build());
    }

    public void saveGroupMessage(String groupName,Message msg) {
        //toastMessage("GRPMSG:"+,msg.getFrom()+":"+msg.getBody());
        ChatMessage cmsg = gson.fromJson(
                msg.getBody(), ChatMessage.class);
        if (cmsg != null) {
            if (!cmsg.sender.equalsIgnoreCase(loginUser)) {
                processGroupMessage(groupName,cmsg);
            }
        }
    }

    public void toastMessage(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public List<String> getJoinedGroupByUserName(String userName) {
//        // Get the MultiUserChatManager
//        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
//
//        List<String> joinedRooms = null;
//        try {
//            // Get the rooms where user3@host.org has joined
//            joinedRooms = manager.getJoinedRooms(userName+"@conference." + serverAddress);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return joinedRooms;
//    }

}