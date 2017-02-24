package com.cviac.com.cviac.app.adapaters;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.squareup.picasso.MemoryPolicy;
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
        public TextView readcount;
        public ImageView imgview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vw = convertView;
        ViewHolder holder;
        Conversation conv = getItem(position);

        if (convertView == null) {

            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.conversation_item, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.textViewName);
            holder.msgview = (TextView) vw.findViewById(R.id.textViewLastmsg);
            holder.datetime = (TextView) vw.findViewById(R.id.textViewdatetime);
            holder.readcount = (TextView) vw.findViewById(R.id.textreadcount);
            holder.imgview = (ImageView) vw.findViewById(R.id.imageViewurl);
            holder.readcount.setVisibility(View.VISIBLE);
            vw.setTag(holder);
        } else {
            holder = (ViewHolder) vw.getTag();
        }

        String url1 = conv.getImageurl();
        if (url1 != null && url1.length() > 0) {
            Picasso.with(mContext).load(conv.getImageurl()).resize(80, 80).transform(new CircleTransform())
                    .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(holder.imgview);
        } else {
            Employee emp = Employee.getemployee(conv.getEmpid());
            if (emp != null) {
                conv.setImageurl(emp.getImage_url());
                if (emp.getGender().equalsIgnoreCase("female")) {
                    Picasso.with(mContext).load(R.drawable.female).resize(80, 80).transform(new CircleTransform())
                            .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.imgview);
                } else {
                    Picasso.with(mContext).load(R.drawable.ic_boy).resize(80, 80).transform(new CircleTransform())
                            .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.imgview);
                }
            }
        }

        holder.nameView.setText(conv.getName());
        holder.msgview.setText(conv.getLastmsg());
        holder.datetime.setText(conv.getformatteddate());
        if (conv.getReadcount() == 0 ) {
            holder.readcount.setVisibility(View.INVISIBLE);
        }else {

            holder.readcount.setVisibility(View.VISIBLE);
            holder.readcount.setText(conv.getReadcount() + "");
        }
        return vw;
    }

}