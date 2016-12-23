package com.cviac.activity.cviacapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cviac.com.cviac.app.adapaters.ChatMessageAdapter;
import com.cviac.com.cviac.app.adapaters.CircleTransform;
import com.cviac.com.cviac.app.datamodels.ChatMessage;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {
    static EditText sendedit;
    static View rootView;
    ImageView customimageback, customimage;
    ActionBar actionBar;
    static ActionBar mActionBar;
    private List<ChatMessage> chats;
    private ChatMessageAdapter chatAdapter;
    Context mContext;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private Conversation emp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        ListView lv = (ListView) findViewById(R.id.listViewChat);
        //chats = new ArrayList<ChatMessage>();

        Intent i = getIntent();
        emp = (Conversation) i.getSerializableExtra("conversewith");
        chats = ChatMessage.getAll(emp.getEmpid());
        //setTitle(emp.getName());
        actionmethod();
        chatAdapter = new ChatMessageAdapter(chats, this);
        lv.setDivider(null);
        lv.setAdapter(chatAdapter);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
        final EditText msgview = (EditText) findViewById(R.id.editTextsend);
        final ImageButton b = (ImageButton) findViewById(R.id.sendbutton);

        msgview.setSingleLine(false);
        msgview.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        msgview.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        msgview.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        msgview.setVerticalScrollBarEnabled(true);
        msgview.setMovementMethod(ScrollingMovementMethod.getInstance());
        msgview.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                String msg = msgview.getText().toString();

                if (msg.length() != 0) {

                    ChatMessage mgsopj = new ChatMessage();

                    mgsopj.setMsg(msg);
                    mgsopj.setIn(false);
                    mgsopj.setFrom(emp.getEmpid());
                    mgsopj.setCtime(new Date());
                    mgsopj.setName(emp.getName());
                    chats.add(mgsopj);
                    mgsopj.save();

                    msgview.getText().clear();
                    chatAdapter.notifyDataSetChanged();
                    //chatAdapter.notifyDataSetInvalidated();

                }

            }
        });
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("cviac")
                // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_chat, container, false);

            return rootView;
        }
    }

    //action bar set
    public void actionmethod() {

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Disable the default and enable the custom
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3B5CD1")));

            View customView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
            customimage = (ImageView) customView.findViewById(R.id.imageViewcustom);
            //customimageback = (ImageView) customView.findViewById(R.id.imageViewback);

            Picasso.with(mContext).load(R.drawable.ic_launcher).resize(110, 110).transform(new CircleTransform())
                    .into(customimage);
            // Picasso.with(mContext).load(R.drawable.backarrow).resize(55, 55).transform(new CircleTransform())
            //     .into(customimageback);


            // Get the textview of the title
            TextView customTitle = (TextView) customView.findViewById(R.id.actionbarTitle);

            customTitle.setText(emp.getName());
            // Change the font family (optional)

            // Set the on click listener for the title
            customTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.w("MainActivity", "ActionBar's title clicked.");
                    Intent i = new Intent(ChatActivity.this, MyProfileActivity.class);
                    i.putExtra("empcode", emp.getEmpid());
                    startActivity(i);
                    finish();
                }
            });
           /* customimageback.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ChatActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            });*/
            // Apply the custom view
            actionBar.setCustomView(customView);
        }
    }

}