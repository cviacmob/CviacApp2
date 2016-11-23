package com.cviac.adapter.cviacapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cviac.activity.cviacapp.R;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.Employee;

import java.util.List;

/**
 * Created by User on 11/22/2016.
 */

public class EmployeeDetailsAdapter extends ArrayAdapter<Employee> {
    Context mContext;
    private List<Employee> emp;
    public ArrayAdapter adapter;

    private int lastPostion = -1;
    public EmployeeDetailsAdapter(List<Employee> objects, Context context) {
        super(context, R.layout.profiledetails, objects);
        emp = objects;
         mContext = context;
    }
    public static class ViewHolder {
        public TextView title;
        public TextView details;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vw = convertView;
        EmployeeDetailsAdapter.ViewHolder holder;
        Employee emp = getItem(position);
        if(convertView==null) {

            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.profiledetails, parent, false);
            holder = new EmployeeDetailsAdapter.ViewHolder();

            holder.details = (TextView) vw.findViewById(R.id.tvdatiles);

            holder.details.setText((CharSequence) emp.getName());
            vw.setTag(holder);
        }
        else {
            holder =  (EmployeeDetailsAdapter.ViewHolder) vw.getTag();
        }

        return super.getView(position, convertView, parent);
    }
}
