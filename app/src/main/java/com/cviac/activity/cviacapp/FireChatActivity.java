package com.cviac.activity.cviacapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cviac.adapter.cviacapp.CircleTransform;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.ChatMsg;
import com.cviac.datamodel.cviacapp.Conversation;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  FireChatActivity extends Activity {

    private List<ChatMessage> chats;
    private Conversation emp;
    private FirebaseListAdapter<ChatMsg> myAdapter;
    ActionBar actionBar;
    static ActionBar mActionBar;
    private DatabaseReference dbref;
    RelativeLayout relativelayout;
    ImageView customimageback, customimage;
    private String myempId;
    private String myempname;
    Context mContext;


    private String getNormalizedConverseId(String myid, String receverid) {
        if (myid.compareTo(receverid) > 0) {
            return myid+"_"+receverid;
        }
        return receverid+"_"+myid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        ListView lv = (ListView) findViewById(R.id.listViewChat);
        lv.setDivider(null);
        //chats = new ArrayList<ChatMessage>();

        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        myempId = prefs.getString("empid","");
        myempname = prefs.getString("empname","");

        Intent i = getIntent();

        emp = (Conversation) i.getSerializableExtra("conversewith");
        final String converseId = getNormalizedConverseId(myempId,emp.getEmpid());
        dbref = FirebaseDatabase.getInstance().getReference().
                                    child("conversations").child(converseId);

        actionmethod();

        myAdapter = new FirebaseListAdapter<ChatMsg>(this,ChatMsg.class,R.layout.fragment_chat,dbref) {
            @Override
            protected void populateView(View vw, ChatMsg s, int i) {

                if (myempId.equals(s.getSenderid())) {
                    relativelayout = new RelativeLayout(getBaseContext());
                    TextView msgview = new TextView(getBaseContext());
                    msgview = (TextView) vw.findViewById(R.id.textchatmsg);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    msgview.setLayoutParams(layoutParams);
                    msgview.setBackgroundResource(R.drawable.bubble2);




                 /*   msgview.setLayoutParams(lp);
                    layouth.addView(msgview);*/



                    msgview.setText(s.getMsg());
                }
                else {
                    relativelayout = new RelativeLayout(getBaseContext());
                    TextView msgview = new TextView(getBaseContext());
                    msgview = (TextView) vw.findViewById(R.id.textchatmsg);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    msgview.setLayoutParams(layoutParams);
                    msgview.setBackgroundResource(R.drawable.bubble1);
                    msgview.setText(s.getMsg());
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
        msgview.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

        msgview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                String msg = msgview.getText().toString();
                if (msg.length() != 0) {
                    ChatMsg mgsopj = new ChatMsg();
                    mgsopj.setMsg(msg);
                    new SendMessageTask().execute(mgsopj);
                    msgview.getText().clear();
                }
            }
        });
    }






    private class SendMessageTask extends AsyncTask<ChatMsg, Integer, Long> {

        @Override
        protected Long doInBackground(ChatMsg... params) {
            ChatMsg cmsg = params[0];
            Map<String, Object> updateValues = new HashMap<>();
            String msgid = System.currentTimeMillis()+"";
            updateValues.put("msg",cmsg.getMsg());
            updateValues.put("ctime", new Date());
            updateValues.put("senderid", myempId);
            updateValues.put("sendername",myempname);
            updateValues.put("receiverid",emp.getEmpid());
            updateValues.put("receivername",emp.getName());
            updateValues.put("msgid",msgid);
            dbref.child(msgid).setValue(
                    updateValues,
                    new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                            if (firebaseError != null) {
                                Toast.makeText(FireChatActivity.this,
                                        "Send message failed: " + firebaseError.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(FireChatActivity.this,
                                        "Send message success" ,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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

            Picasso.with(mContext).load(R.drawable.bala).resize(110, 110).transform(new CircleTransform())
                    .into(customimage);
           Picasso.with(mContext).load(R.drawable.backarrow).resize(55, 55).transform(new CircleTransform())
                    .into(customimageback);


            // Get the textview of the title
            TextView customTitle = (TextView) customView.findViewById(R.id.actionbarTitle);

            customTitle.setText(emp.getName());
            // Change the font family (optional)
            customTitle.setTypeface(Typeface.MONOSPACE);
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
                    Intent i = new Intent(FireChatActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            // Apply the custom view
            actionBar.setCustomView(customView);
        }

    }


}
