package com.cviac.com.cviac.app.adapaters;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.Employee;
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
    String url1;

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
            Picasso.with(mContext).load(emp.getImage_url()).resize(80, 80).transform(new CircleTransform())
                    .into(holder.empimage);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_launcher).resize(80, 80).transform(new CircleTransform())
                    .into(holder.empimage);
        }
        holder.nameView.setText(emp.getEmp_name());
        holder.mobile.setText(emp.getEmail());
     /*   holder.empimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton("Get Pro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater =  LayoutInflater.from(getContext());
                View dialogLayout = inflater.inflate(R.layout.customalert, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), Integer.parseInt(url1));
                        float imageWidthInPX = (float)image.getWidth();

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                                Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                        image.setLayoutParams(layoutParams);


                    }
                });
            }
        });*/

        return vw;

    }



}