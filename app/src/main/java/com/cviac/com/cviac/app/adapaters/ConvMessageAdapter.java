package com.cviac.com.cviac.app.adapaters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.ConvMessage;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class ConvMessageAdapter extends ArrayAdapter<ConvMessage> {

    private List<ConvMessage> chats;
    public ArrayAdapter adapter;
    private String myempId;
    private int lastPostion = -1;

    Context mContext;

    public ConvMessageAdapter(List<ConvMessage> objects, Context context) {
        super(context, R.layout.fragment_chat, objects);
        chats = objects;
        mContext = context;
    }

    public static class ViewHolder {
        public TextView msgview,txt;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vw = convertView;
        ViewHolder holder;
        ConvMessage chat = getItem(position);
        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.fragment_chat, parent, false);
            holder = new ViewHolder();
            holder.msgview = (TextView) vw.findViewById(R.id.textchatmsg);
            holder.txt = (TextView) vw.findViewById(R.id.duration);
            vw.setTag(holder);
        } else {
            holder = (ViewHolder) vw.getTag();
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (chat.isMine() == true) {
            RelativeLayout rLayout = (RelativeLayout) vw.findViewById(R.id.textchat);
            Resources res = mContext.getResources();
            Drawable drawable = res.getDrawable(R.drawable.msg_out);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rLayout.setBackgroundDrawable(drawable);
            holder.msgview.setLayoutParams(layoutParams);
            //msgview.setBackgroundResource(R.drawable.bubble2);
            holder.msgview.setText(chat.getMsg());
            // msgview.setText(s.getMsg());
            holder.txt.setText(getformatteddate(chat.getCtime()));
        } else {

            //holder.msgview = (TextView) vw.findViewById(R.id.textchatmsg);
            //holder.txt = (TextView) vw.findViewById(R.id.duration);
            //msgvieww = (TextView) vw.findViewById(R.id.textchatmsg);

            RelativeLayout rLayout = (RelativeLayout) vw.findViewById(R.id.textchat);
            Resources res = mContext.getResources();
            Drawable drawable = res.getDrawable(R.drawable.msg_in);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.msgview.setLayoutParams(layoutParams);
            rLayout.setBackgroundDrawable(drawable);
            // msgview.setBackgroundResource(R.drawable.bubble1);
            holder.msgview.setText(chat.getMsg());
            holder.txt.setText(getformatteddate(chat.getCtime()));
        }
        return vw;

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
