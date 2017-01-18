package com.cviac.com.cviac.app.adapaters;

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
        public TextView msgview;
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
            vw.setTag(holder);
        } else {
            holder = (ViewHolder) vw.getTag();
        }

        if (chat.isIn() == false) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            RelativeLayout rLayout = (RelativeLayout) vw.findViewById(R.id.textchat);
            Resources res = mContext.getResources();
            Drawable drawable = res.getDrawable(R.drawable.msg_out);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rLayout.setBackgroundDrawable(drawable);
            holder.msgview.setLayoutParams(layoutParams);
            //msgview.setBackgroundResource(R.drawable.bubble2);
            holder.msgview.setText(chat.getMsg());
            // msgview.setText(s.getMsg());
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView msgvieww = (TextView) vw.findViewById(R.id.textchatmsg);
            TextView txt = (TextView) vw.findViewById(R.id.duration);
            msgvieww = (TextView) vw.findViewById(R.id.textchatmsg);

            RelativeLayout rLayout = (RelativeLayout) vw.findViewById(R.id.textchat);
            Resources res = mContext.getResources();
            Drawable drawable = res.getDrawable(R.drawable.msg_in);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            msgvieww.setLayoutParams(layoutParams);
            rLayout.setBackgroundDrawable(drawable);
            // msgview.setBackgroundResource(R.drawable.bubble1);
            msgvieww.setText(chat.getMsg());
        }
        return vw;

    }

}
