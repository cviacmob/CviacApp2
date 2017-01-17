package com.cviac.com.cviac.app.adapaters;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.ConvMessage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ConvMessageAdapter extends ArrayAdapter<ConvMessage> {

    private List<ConvMessage> chats;
    public ArrayAdapter adapter;

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

        holder.msgview.setText(chat.getMsg().toString());
        return vw;
    }
}
