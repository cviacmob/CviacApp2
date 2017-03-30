package com.cviac.com.cviac.app.adapaters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cviac.activity.cviacapp.MyProfile;
import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.GroupMemberInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.cviac.activity.cviacapp.R.id.empimage;

public class GroupInfoAdapter extends ArrayAdapter<GroupMemberInfo> {

    private List<GroupMemberInfo> emps;

    private int lastPostion = -1;
    String url1;
    String receiverempcode;
    Employee empobject;
    GroupMemberInfo emp;
    private SparseBooleanArray mSelectedItemsIds;
    Context mContext;


    public GroupInfoAdapter(List<GroupMemberInfo> objects, Context context) {
        super(context, R.layout.collegues_item, objects);
        emps = objects;
        mContext = context;
        mSelectedItemsIds = new  SparseBooleanArray();
    }

    public static class ViewHolder {
        public TextView nameView;
        public TextView email;
        public ImageView empimage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vw = convertView;
        ViewHolder holder;
        emp= getItem(position);

        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.collegues_item, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.colleguesname);
            holder.email = (TextView) vw.findViewById(R.id.textemail);
            holder.empimage = (ImageView) vw.findViewById(empimage);
            vw.setTag(holder);

        } else {
            holder = (ViewHolder) vw.getTag();
        }
        empobject = Employee.getemployee(emp.getMember_id());
        url1 = empobject.getImage_url();
        if (url1 != null && url1.length() > 0) {
            Picasso.with(mContext).load(url1).resize(80, 80).transform(new CircleTransform())
                    .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.empimage);
        } else {
            if(empobject.getGender().equalsIgnoreCase("female"))
            {
                Picasso.with(mContext).load(R.drawable.female).resize(80, 80).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(holder.empimage);
            }else
            {
                Picasso.with(mContext).load(R.drawable.ic_boy).resize(80, 80).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(holder.empimage);
            }

        }
        holder.nameView.setText(empobject.getEmp_name());
        holder.email.setText(empobject.getEmail());
        holder.empimage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            emp = emps.get(position);
            empobject = Employee.getemployee(emp.getMember_id());
            receiverempcode= empobject.getEmp_code();
            Intent i = new Intent(getContext(), MyProfile.class);
            i.putExtra("empcode", receiverempcode);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(i);

        }
    });
        return vw;

    }





}