package com.cviac.adapter.cviacapp;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.datamodel.cviacapp.Collegue;
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
        Conversation chat = getItem(position);

        if(convertView==null){
            
           LayoutInflater inf = LayoutInflater.from(getContext());
           vw = inf.inflate(R.layout.conversation_item, parent, false);
             
            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.textViewName);
            holder.msgview=(TextView)vw.findViewById(R.id.textViewLastmsg);
            holder.datetime=(TextView)vw.findViewById(R.id.textViewdatetime);
            
            holder.imgview=(ImageView)vw.findViewById(R.id.imageViewurl);
    		Picasso.with(mContext).load(R.drawable.bala).resize(130, 130).transform(new CircleTransform())
			.into(holder.imgview);

    		holder.nameView.setText(chat.getName());
    		holder.msgview.setText(chat.getLastmsg());
    		holder.datetime.setText(chat.getformatedDate());
             
           /************  Set holder with LayoutInflater ************/
            vw.setTag( holder );
        }
        else {
            holder=(ViewHolder)vw.getTag();
        }    
		return vw;
	}

}