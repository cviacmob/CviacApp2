
package com.cviac.com.cviac.app.adapaters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.EventInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsAdapter extends ArrayAdapter<EventInfo> {

    private List<EventInfo> eve;


    private int lastPostion = -1;

    Context mContext;

    public EventsAdapter(List<EventInfo> objects, Context context) {
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
        EventInfo even = getItem(position);
        View vw = convertView;
        ViewHolder holder;

        EventInfo evennn = getItem(position);
        if (convertView == null) {

            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.events_item, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.textViewNameevent);
            holder.typeview = (TextView) vw.findViewById(R.id.textViewdate);
            holder.disview = (TextView) vw.findViewById(R.id.textViewDescription);
            holder.imgview = (ImageView) vw.findViewById(R.id.imageViewevent);
            vw.setTag(holder);
        } else {
            holder = (ViewHolder) vw.getTag();
        }
        String st = holder.imgview.toString();
        if (st != null) {
            Picasso.with(mContext).load(R.drawable.birthday).resize(130, 130).transform(new CircleTransform()).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(holder.imgview);
        }
        holder.nameView.setText(even.getEvent_title());
        String timeStam = new SimpleDateFormat("dd-MM-yy").format(new Date());
        holder.typeview.setText(timeStam);
        holder.disview.setText(even.getEvent_description());

        return vw;


    }
}
