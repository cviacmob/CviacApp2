
package com.cviac.adapter.cviacapp;

import java.util.List;

import com.cviac.adapter.cviacapp.ConversationAdapter.ViewHolder;
import com.cviac.activity.cviacapp.R;
import com.cviac.datamodel.cviacapp.Conversation;
import com.cviac.datamodel.cviacapp.Event;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsAdapter extends ArrayAdapter<Event> {

    private List<Event> eve;

    private int lastPostion = -1;

    Context mContext;

    public EventsAdapter(List<Event> objects, Context context) {
        super(context, R.layout.events_item, objects);
        eve = objects;
        mContext = context;
    }

    public static class ViewHolder {
        public TextView nameView;
        public TextView typeview;
        public TextView disview;
        public ImageView imgview;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vw = convertView;
        ViewHolder holder;
        Event even = getItem(position);
        if (convertView == null) {

            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.events_item, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.textViewNameevent);
            holder.typeview = (TextView) vw.findViewById(R.id.textViewdate);
            holder.disview = (TextView) vw.findViewById(R.id.textViewDescription);
            holder.imgview = (ImageView) vw.findViewById(R.id.imageViewevent);
            Picasso.with(mContext).load(R.drawable.birthday).resize(130, 130).transform(new CircleTransform()).into(holder.imgview);
            holder.nameView.setText(even.getTitle());
            holder.typeview.setText(even.getformatedDate());
            holder.disview.setText(even.getDiscription());
        } else {
            holder = (ViewHolder) vw.getTag();
        }
        return vw;


    }
}
