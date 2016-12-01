package com.cviac.adapter.cviacapp;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.datamodel.cviacapp.Employee;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.cviac.activity.cviacapp.R.id.empimage;

public class ColleguesAdapter extends ArrayAdapter<Employee> {

	private List<Employee> emps;

	private int lastPostion = -1;

	Context mContext;

	public ColleguesAdapter(List<Employee> objects, Context context) {
		super(context, R.layout.collegues_item, objects);
		emps = objects;
		mContext = context;
	}

	public static class ViewHolder {
		public TextView nameView;
		public TextView mobile;
		public ImageView empimage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vw = convertView;
        ViewHolder holder;
		Employee emp = getItem(position);
		if(convertView==null){
			
		LayoutInflater inf = LayoutInflater.from(getContext());
		vw = inf.inflate(R.layout.collegues_item, parent, false);
		holder = new ViewHolder();
		holder.nameView = (TextView) vw.findViewById(R.id.colleguesname);
		holder.mobile = (TextView) vw.findViewById(R.id.textemail);
		holder.empimage = (ImageView) vw.findViewById(empimage);
		String url1=emp.getImage_url();
		String imageurl="http://www.gantrypark.com/Portals/12/Users/066/14/53314/adam-parker-large.jpg";
		if(url1==null)
		{
			Picasso.with(mContext).load(emp.getImage_url()).resize(80, 80).transform(new CircleTransform())
					.into(holder.empimage);


		}else
		{
			Picasso.with(mContext).load(R.drawable.ic_launcher).resize(80, 80).transform(new CircleTransform())
					.into(holder.empimage);
		}

		holder.nameView.setText(emp.getEmp_name());
		holder.mobile.setText(emp.getEmail());

		}else
		{
	        holder=(ViewHolder)vw.getTag();
		}
		return vw;

	}

}