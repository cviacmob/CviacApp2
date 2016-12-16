package com.cviac.adapter.cviacapp;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.datamodel.cviacapp.Conversation;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationAdapter extends ArrayAdapter<Conversation> {

	private List<Conversation> chats;

	private int lastPostion = -1;

	Context mContext;

	public ConversationAdapter(List<Conversation> objects, Context context) {
		super(context, R.layout.conversation_item, objects);
		chats = objects;
		mContext = context;
	}

	public static class ViewHolder {
		public TextView nameView;
		public TextView msgview;
		public TextView datetime;
		public ImageView imgview;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vw = convertView;
        ViewHolder holder;
        Conversation conv = getItem(position);

        if(convertView==null){
            
           LayoutInflater inf = LayoutInflater.from(getContext());
           vw = inf.inflate(R.layout.conversation_item, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.textViewName);
            holder.msgview=(TextView)vw.findViewById(R.id.textViewLastmsg);
            holder.datetime=(TextView)vw.findViewById(R.id.textViewdatetime);
			holder.imgview = (ImageView) vw.findViewById(R.id.imageViewurl);
            vw.setTag( holder );
        }
        else {
            holder=(ViewHolder)vw.getTag();
        }

		String url1 = conv.getImageurl();
		if (url1 != null && url1.length() > 0) {
			Picasso.with(mContext).load(conv.getImageurl()).resize(80, 80).transform(new CircleTransform())
					.into(holder.imgview);
		} else {
			Picasso.with(mContext).load(R.drawable.ic_launcher).resize(80, 80).transform(new CircleTransform())
					.into(holder.imgview);
		}

		holder.nameView.setText(conv.getName());
		holder.msgview.setText(conv.getLastmsg());
		holder.datetime.setText(conv.getformatteddate());
		return vw;
	}

}