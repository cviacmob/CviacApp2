/*
package com.cviac.com.cviac.app.adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.Annoncements;

import java.text.SimpleDateFormat;
import java.util.List;


 // Created by Administrator on 1/24/2017.



public class AnnoncementAdapter extends ArrayAdapter<Annoncements> {

List<Annoncements> annonce;
    Context mContext;

    public AnnoncementAdapter(List<Annoncements> objects, Context context) {
        super(context, R.layout.fragment_chat, objects);
        annonce = objects;
        mContext = context;
    }
    public static class ViewHolder {
        public TextView nameView;
        public TextView datetime;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vw = convertView;
       AnnoncementAdapter.ViewHolder holder;
     Annoncements Annoncement = getItem(position);

        if (convertView == null) {

            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.annoncementtext, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.nameView);
            holder.datetime = (TextView) vw.findViewById(R.id.datetime);

            vw.setTag(holder);
        } else {
            holder = (AnnoncementAdapter.ViewHolder) vw.getTag();
        }
        holder.nameView.setText(Annoncement.getAnnoncemsg());
        String timeStam = new SimpleDateFormat("dd-MM-yy").format(Annoncement.getDate());
        holder.datetime.setText(timeStam);
        return vw;
    }
}
*/
