package com.cviac.adapter.cviacapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cviac.activity.cviacapp.R;
import com.cviac.datamodel.cviacapp.Employee;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by User on 11/22/2016.
 */

public class EmployeeDetailsAdapter extends ArrayAdapter<Employee> {
    Context context;
    private List<Employee> employees;
    public ArrayAdapter adapter;
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private int lastPostion = -1;

    public EmployeeDetailsAdapter(Context context,List<Employee> employees) {
        super(context, R.layout.profile, employees);
        this.context = context;
        this.employees = employees;
    }

    private class ViewHolder {
        TextView empIdTxt;
        TextView empNameTxt;
        TextView empMobile;
        TextView empemail;
        TextView empdepartment;
        TextView empmanagername;
        TextView empgender;
        TextView empDob;
        TextView empdegination;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile, null);
            holder = new ViewHolder();

            holder.empIdTxt = (TextView) convertView
                    .findViewById(R.id.tvcoder);
            holder.empNameTxt = (TextView) convertView
                    .findViewById(R.id.tvnamer);
            holder.empMobile = (TextView) convertView
                    .findViewById(R.id.tvmobiler);
            holder.empDob = (TextView) convertView
                    .findViewById(R.id.tvdobr);
            holder.empemail = (TextView) convertView
                    .findViewById(R.id.tvemailidr);
            holder.empdepartment = (TextView) convertView
                    .findViewById(R.id.tvdeptr);
            holder.empgender = (TextView) convertView
                    .findViewById(R.id.tvgenderr);
            holder.empmanagername = (TextView) convertView
                    .findViewById(R.id.tvmanagerr);
            holder.empdegination = (TextView) convertView
                    .findViewById(R.id.tvdesigr);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Employee employee = (Employee) getItem(position);
        holder.empIdTxt.setText(employee.getEmpID());
        holder.empNameTxt.setText(employee.getName());
        holder.empMobile.setText(employee.getMobile());
        holder.empDob.setText(formatter.format(employee.getDob()));
        holder.empemail.setText(employee.getEmailID());
        holder.empgender.setText(employee.getGender());
        holder.empmanagername.setText(employee.getManagername());
        holder.empdepartment.setText(employee.getDepartment());
        holder.empdegination.setText(employee.getDepartment());


        return convertView;
    }
}
