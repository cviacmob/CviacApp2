package com.cviac.com.cviac.app.adapaters;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.ChatMessage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

	private List<ChatMessage> chats;
	public ArrayAdapter adapter;

	private int lastPostion = -1;

	Context mContext;

	public ChatMessageAdapter(List<ChatMessage> objects, Context context) {
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
		ChatMessage chat = getItem(position);
		if(convertView==null) {
			LayoutInflater inf = LayoutInflater.from(getContext());
			vw = inf.inflate(R.layout.fragment_chat, parent, false);
			holder = new ViewHolder();
			holder.msgview = (TextView) vw.findViewById(R.id.textchatmsg);
			vw.setTag(holder);
		}
		else {
			holder =  (ViewHolder) vw.getTag();
		}

		holder.msgview.setText(chat.getMsg().toString());
		return vw;
	}
}
