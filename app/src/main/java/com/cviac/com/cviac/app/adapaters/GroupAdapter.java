package com.cviac.com.cviac.app.adapaters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 3/20/2017.
 */

public class GroupAdapter extends BaseAdapter {
    private List<Employee> emps;
    Context mContext;
    String url1;
    Employee emp;


    public GroupAdapter(List<Employee> objects, Context context) {

        this.emps = objects;
        this.mContext = context;

    }


    public static class ViewHolder {
        public ImageView iv;
        public TextView name;
    }

    @Override
    public int getCount() {
        if (emps != null) {
            return emps.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View pho = convertView;
        GroupAdapter.ViewHolder holder;
        emp = emps.get(position);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pho = inflater.inflate(R.layout.gropmembers, null);
            holder = new GroupAdapter.ViewHolder();
            holder.iv = (ImageView) pho.findViewById(R.id.membersfoto);
            holder.name = (TextView) pho.findViewById(R.id.membersname);
            pho.setTag(holder);

        } else {
            holder = (GroupAdapter.ViewHolder) pho.getTag();
        }
        url1 = emp.getImage_url();
        if (url1 != null && url1.length() > 0) {
            Picasso.with(mContext).load(emp.getImage_url()).resize(130, 130).transform(new CircleTransform())
                    .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.iv);
        } else {

            if (emp.getGender().equalsIgnoreCase("female")) {
                Picasso.with(mContext).load(R.drawable.female).resize(130, 130).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.iv);
            } else {
                Picasso.with(mContext).load(R.drawable.ic_boy).resize(130, 130).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.iv);

            }
        }
        holder.name.setText(emp.getEmp_name());
        return pho;
    }
}
