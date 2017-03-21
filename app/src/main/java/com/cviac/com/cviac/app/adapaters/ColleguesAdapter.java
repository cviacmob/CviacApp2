package com.cviac.com.cviac.app.adapaters;

import java.util.List;

import com.cviac.activity.cviacapp.HomeActivity;
import com.cviac.activity.cviacapp.MyProfile;
import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.fragments.ContactsFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.cviac.activity.cviacapp.R.id.empimage;

public class ColleguesAdapter extends ArrayAdapter<Employee> {

    private List<Employee> emps;

    private int lastPostion = -1;
    String url1;
    String receiverempcode;
    Employee emp;
    private SparseBooleanArray mSelectedItemsIds;
    Context mContext;


    public ColleguesAdapter(List<Employee> objects, Context context) {
        super(context, R.layout.collegues_item, objects);
        emps = objects;
        mContext = context;
        mSelectedItemsIds = new  SparseBooleanArray();
    }

    public static class ViewHolder {
        public TextView nameView;
        public TextView mobile;
        public ImageView empimage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vw = convertView;
        ViewHolder holder;
       emp = getItem(position);

        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.collegues_item, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) vw.findViewById(R.id.colleguesname);
            holder.mobile = (TextView) vw.findViewById(R.id.textemail);
            holder.empimage = (ImageView) vw.findViewById(empimage);
            vw.setTag(holder);

        } else {
            holder = (ViewHolder) vw.getTag();
        }

        url1 = emp.getImage_url();
        if (url1 != null && url1.length() > 0) {
            Picasso.with(mContext).load(url1).resize(80, 80).transform(new CircleTransform())
                    .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.empimage);
        } else {
            if(emp.getGender().equalsIgnoreCase("female"))
            {
                Picasso.with(mContext).load(R.drawable.female).resize(80, 80).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(holder.empimage);
            }else
            {
                Picasso.with(mContext).load(R.drawable.ic_boy).resize(80, 80).transform(new CircleTransform())
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(holder.empimage);
            }

        }
        holder.nameView.setText(emp.getEmp_name());
        holder.mobile.setText(emp.getEmail());
        holder.empimage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            emp = emps.get(position);
            receiverempcode= emp.getEmp_code();
            Intent i = new Intent(getContext(), MyProfile.class);
            i.putExtra("empcode", receiverempcode);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(i);

        }
    });
        return vw;

    }

    @Override
    public void remove(@Nullable Employee object) {

        notifyDataSetChanged();
        emps.remove(object);
    }
    public  List<Employee> getMyList() {
        return emps;
    }
    public void  toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position,  value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }
    public void  removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public int  getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public  SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }





}