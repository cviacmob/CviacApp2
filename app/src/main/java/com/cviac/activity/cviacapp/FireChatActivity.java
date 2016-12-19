package com.cviac.activity.cviacapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cviac.adapter.cviacapp.CircleTransform;
import com.cviac.cviacappapi.cviacapp.CVIACApi;
import com.cviac.cviacappapi.cviacapp.FCMSendMessageResponse;
import com.cviac.cviacappapi.cviacapp.PushMessageInfo;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.ChatMsg;
import com.cviac.datamodel.cviacapp.Conversation;
import com.cviac.datamodel.cviacapp.PresenceInfo;
import com.cviac.fragments.cviacapp.Chats;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.cviac.activity.cviacapp.R.id.textchat;
import static com.google.android.gms.measurement.internal.zzl.api;

public class FireChatActivity extends Activity {

    private List<ChatMessage> chats;
    private Conversation emp;
    private FirebaseListAdapter<ChatMsg> myAdapter;
    ActionBar actionBar;
    static ActionBar mActionBar;
    private DatabaseReference dbref;
    RelativeLayout.LayoutParams relativelayout;
    ImageView customimageback, customimage, imgvwtick;
    private String myempId;
    private String myempname;
    Context mContext;
    RelativeLayout rp;
    TextView txt, msgview, presenceText;
    PresenceInfo presenceInfo;
    ListView lv;


    private String getNormalizedConverseId(String myid, String receverid) {
        if (myid.compareTo(receverid) > 0) {
            return myid + "_" + receverid;
        }
        return receverid + "_" + myid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        lv = (ListView) findViewById(R.id.listViewChat);
        lv.setDivider(null);
        lv.setStackFromBottom(true);

        //chats = new ArrayList<ChatMessage>();

        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        myempId = prefs.getString("empid", "");
        myempname = prefs.getString("empname", "");

        Intent i = getIntent();

        emp = (Conversation) i.getSerializableExtra("conversewith");
        final String converseId = getNormalizedConverseId(myempId, emp.getEmpid());
        dbref = FirebaseDatabase.getInstance().getReference().
                child("conversations").child(converseId);


        actionmethod();

        myAdapter = new FirebaseListAdapter<ChatMsg>(this, ChatMsg.class, R.layout.fragment_chat, dbref) {
            @Override
            protected void populateView(View vw, ChatMsg s, int i) {

                if (myempId.equals(s.getSenderid())) {


                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    msgview = (TextView) vw.findViewById(R.id.textchatmsg);
                    txt = (TextView) vw.findViewById(R.id.duration);
                    imgvwtick = (ImageView) vw.findViewById(R.id.list_image);
                    RelativeLayout rLayout = (RelativeLayout) vw.findViewById(R.id.textchat);
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.bubble2);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    rLayout.setBackgroundDrawable(drawable);
                   // rLayout.setBackgroundColor(Color.parseColor("#C4FED4"));
                    msgview.setLayoutParams(layoutParams);
                    //msgview.setBackgroundResource(R.drawable.bubble2);
                    msgview.setText(s.getMsg());
                    //rLayout.setBackgroundResource(R.color.green);

                    if (s.getStatus() == 0) {
                        imgvwtick.setBackgroundResource(R.drawable.schedule);
                    }
                    if (s.getStatus() == 1) {
                        imgvwtick.setBackgroundResource(R.drawable.done);
                    } else if (s.getStatus() == 2) {
                        imgvwtick.setBackgroundResource(R.drawable.done_all);
                    } else if (s.getStatus() == 3) {
                        imgvwtick.setBackgroundResource(R.drawable.done_all_colo);
                    }
                    String st = getformatteddate(s.getCtime());

                    txt.setText(st);

                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    TextView msgvieww = (TextView) vw.findViewById(R.id.textchatmsg);
                    TextView txt = (TextView) vw.findViewById(R.id.duration);
                    msgvieww = (TextView) vw.findViewById(R.id.textchatmsg);

                    RelativeLayout rLayout = (RelativeLayout) vw.findViewById(R.id.textchat);
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.bubble1);

                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    msgvieww.setLayoutParams(layoutParams);
                    rLayout.setBackgroundDrawable(drawable);
                    // msgview.setBackgroundResource(R.drawable.bubble1);
                    msgvieww.setText(s.getMsg());
                    txt.setText(getformatteddate(s.getCtime()));
                }

            }
        };
        lv.setAdapter(myAdapter);


        final EditText msgview = (EditText) findViewById(R.id.editTextsend);
        final ImageButton sendbutton = (ImageButton) findViewById(R.id.sendbutton);
        msgview.setSingleLine(false);
        msgview.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        msgview.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        msgview.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        msgview.setVerticalScrollBarEnabled(true);
        msgview.setMovementMethod(ScrollingMovementMethod.getInstance());
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        msgview.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

     /*   msgview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide soft keyboard.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(msgview.getWindowToken(), 0);
                    // Make it non-editable again.
                    msgview.setKeyListener(null);
                }
            }
        });*/

        sendbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                String msg = msgview.getText().toString();
                if (msg.length() != 0) {
                    ChatMsg mgsopj = new ChatMsg();
                    mgsopj.setMsg(msg);
                    saveLastConversationMessage(mgsopj);
                    new SendMessageTask().execute(mgsopj);
                    msgview.getText().clear();

                }
            }
        });
    }

    private void saveLastConversationMessage(ChatMsg msg) {

        CVIACApplication app = (CVIACApplication) getApplication();
        Chats chatFrag = app.getChatsFragment();
        Conversation cnv = Conversation.getConversation(emp.getEmpid());
        boolean newconv = false;
        if (cnv == null) {
            cnv = new Conversation();
            newconv = true;
        }
        cnv.setEmpid(emp.getEmpid());
        cnv.setImageurl(emp.getImageurl());
        cnv.setName(emp.getName());
        cnv.setDatetime(new Date());
        cnv.setLastmsg(msg.getMsg());
        cnv.save();
        if (chatFrag != null && chatFrag.adapter != null) {
            chatFrag.reloadConversation();
        }

    }

    private void SendPushNotification(ChatMsg cmsg) {
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
        pinfo.setTo(presenceInfo.getPushId());
        PushMessageInfo.DataInfo dinfo = new PushMessageInfo.DataInfo();
        dinfo.setMsg(cmsg.getMsg());
        dinfo.setSendername(cmsg.getSendername());
        dinfo.setSenderid(cmsg.getSenderid());
        dinfo.setMsgId(cmsg.getMsgId());
        pinfo.setData(dinfo);
        final Call<FCMSendMessageResponse> call = api.sendPushMessage(key,pinfo);
        call.enqueue(new Callback<FCMSendMessageResponse>() {
            @Override
            public void onResponse(Response<FCMSendMessageResponse> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private class SendMessageTask extends AsyncTask<ChatMsg, Integer, Long> {

        @Override
        protected Long doInBackground(ChatMsg... params) {
            ChatMsg cmsg = params[0];
            Map<String, Object> updateValues = new HashMap<>();
            String msgid = System.currentTimeMillis() + "";
            updateValues.put("msg", cmsg.getMsg());
            cmsg.setCtime(new Date());
            updateValues.put("ctime",cmsg.getCtime() );
            cmsg.setSenderid(myempId);
            updateValues.put("senderid", myempId);
            cmsg.setSendername(myempname);
            updateValues.put("sendername", myempname);
            cmsg.setReceiverid(emp.getEmpid());
            updateValues.put("receiverid", emp.getEmpid());
            cmsg.setReceivername(emp.getName());
            updateValues.put("receivername", emp.getName());
            updateValues.put("msgid", msgid);
            cmsg.setMsgId(msgid);
            updateValues.put("status", 0);
            dbref.child(msgid).setValue(
                    updateValues,
                    new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                            if (firebaseError != null) {
                                Toast.makeText(FireChatActivity.this,
                                        "Send message failed: " + firebaseError.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(FireChatActivity.this,
                                        "Send message success", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            if (presenceInfo != null && presenceInfo.getStatus().equalsIgnoreCase("offline")) {
                if ((presenceInfo.getPushId() != null) && presenceInfo.getPushId().length() > 0) {
                    SendPushNotification(cmsg);
                }
            }
            return null;
        }
    }

    public void actionmethod() {

        actionBar = getActionBar();
        if (actionBar != null) {
            // Disable the default and enable the custom
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3B5CD1")));

            View customView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            customimage = (ImageView) customView.findViewById(R.id.imageViewcustom);
            customimageback = (ImageView) customView.findViewById(R.id.imageViewback);
            presenceText = (TextView) customView.findViewById(R.id.textView5);
            setPresence(emp.getEmpid());

            String url1 = emp.getImageurl();
            if (url1 != null && url1.length() > 0) {
                Picasso.with(mContext).load(emp.getImageurl()).resize(100, 100).transform(new CircleTransform())
                        .into(customimage);
            } else {
                Picasso.with(mContext).load(R.drawable.ic_launcher).resize(80, 80).transform(new CircleTransform())
                        .into(customimage);
            }
            Picasso.with(mContext).load(R.drawable.backarrow).resize(90, 90)
                    .into(customimageback);


            // Get the textview of the title
            TextView customTitle = (TextView) customView.findViewById(R.id.actionbarTitle);

            customTitle.setText(emp.getName());
            // Change the font family (optional)

            // Set the on click listener for the title
            customTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.w("MainActivity", "ActionBar's title clicked.");
                    Intent i = new Intent(FireChatActivity.this, MyProfileActivity.class);

                    i.putExtra("empcode", emp.getEmpid());
                    startActivity(i);
                    finish();
                }
            });
            customimageback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(FireChatActivity.this, HomeActivity.class);
//                    startActivity(i);
                    finish();
                }
            });

            // Apply the custom view
            actionBar.setCustomView(customView);
        }

    }

    private void setPresence(String empCode) {
        DatabaseReference mfiredbref = FirebaseDatabase.getInstance().getReference().child("presence");
        DatabaseReference dbRef = mfiredbref.child(empCode);
        if (dbRef != null) {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    presenceInfo = dataSnapshot.getValue(PresenceInfo.class);
                    if (presenceInfo != null) {
                        if (presenceInfo.getStatus().equalsIgnoreCase("online")) {
                            presenceText.setText(presenceInfo.getStatus());
                        } else {
                            String st = getformatteddate(presenceInfo.getLastseen());
                            presenceText.setText("last seen " + st);
                        }
                    } else {
                        presenceText.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        presenceText.setText("");
    }

    private String getformatteddate(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);

        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "today at  " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "yesterday at " + timeFormatter.format(dateTime);
        } else {
            DateFormat dateform = new SimpleDateFormat("dd-MMM-yy");
            return dateform.format(dateTime) + " " + timeFormatter.format(dateTime);
        }

    }


}
