package com.cviac.com.cviac.app.adapaters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.datamodels.ConvMessage;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;
import static org.jivesoftware.smack.packet.IQ.Type.set;

public class ConvMessageAdapter extends ArrayAdapter<ConvMessage> {

    private List<ConvMessage> chats;
    public ArrayAdapter adapter;
    private String myempId;
    private int lastPostion = -1;
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;

    Context mContext;

    private Set<String> groupNames = new HashSet<>();

    public ConvMessageAdapter(List<ConvMessage> objects, Context context) {
        super(context, R.layout.item_mine_message, objects);
        chats = objects;
        mContext = context;
    }

    private String getDate(Date dt) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(dt);
    }


    public String getGroupHeaderText(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        } else {
            DateFormat dateform = new SimpleDateFormat("dd/MM/yy");
            return dateform.format(dt);
        }
    }

    private boolean checkGroupName(String name) {
        if (groupNames.contains(name)) {
            return false;
        }
        groupNames.add(name);
        return true;
    }

    private String getGroupHeaderName(int position) {
        ConvMessage curr=getItem(position);
        if (position == 0) {
            String gname =  getGroupHeaderText(curr.getCtime());
            return gname;
        }
        else {
              String gname = getGroupHeaderText(curr.getCtime());
            if (checkGroupName(gname)) {
                return gname;
            }
            return "";
        }
    }


    public static class ViewHolder {
        public TextView msgview,txt;
        public ImageView statusview;
        public  RelativeLayout rLayout;

    }
    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }
    @Override
    public int getItemViewType(int position) {
        ConvMessage item = getItem(position);
        if (item.isMine()) return MY_MESSAGE;
        else if (!item.isMine() ) return OTHER_MESSAGE;
        else if (item.isMine()) return MY_IMAGE;
        else return OTHER_IMAGE;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        ConvMessage chat=getItem(position);

        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            TextView textView1 = (TextView) convertView.findViewById(R.id.text1);
            TextView textView3 = (TextView) convertView.findViewById(R.id.textheader);
            ImageView textView2 = (ImageView) convertView.findViewById(R.id.imageView2);

            textView.setText( getItem(position).getMsg());

            textView1.setText(getformatteddate(chat.getCtime()));
            if (chat.getStatus() == 0) {
                textView2.setBackgroundResource(R.drawable.schedule);
            }
            if (chat.getStatus() == 1) {
                textView2.setBackgroundResource(R.drawable.done);
            } else if (chat.getStatus() == 2) {
                textView2.setBackgroundResource(R.drawable.done_all);
            } else if (chat.getStatus() == 3) {
                textView2.setBackgroundResource(R.drawable.done_all_colo);
            }

        }
        else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getMsg());
            TextView textView1 = (TextView) convertView.findViewById(R.id.text1);
            textView1.setText(getformatteddate(chat.getCtime()));
            TextView textView3 = (TextView) convertView.findViewById(R.id.textheader);

        }

        return convertView;
    }



    // @Override
    public View getView2(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        ConvMessage chat=getItem(position);

        String groupName = getGroupHeaderName(position);

        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            TextView textView1 = (TextView) convertView.findViewById(R.id.text1);
            TextView textView3 = (TextView) convertView.findViewById(R.id.textheader);
            ImageView textView2 = (ImageView) convertView.findViewById(R.id.imageView2);

            textView.setText( getItem(position).getMsg());

            if (!groupName.isEmpty()) {
                textView3.setText(groupName);
                textView3.setVisibility(View.VISIBLE);
            }
            else {
                textView3.setVisibility(View.INVISIBLE);
            }


            textView1.setText(getformatteddate(chat.getCtime()));
            if (chat.getStatus() == 0) {
                textView2.setBackgroundResource(R.drawable.schedule);
            }
            if (chat.getStatus() == 1) {
                textView2.setBackgroundResource(R.drawable.done);
            } else if (chat.getStatus() == 2) {
                textView2.setBackgroundResource(R.drawable.done_all);
            } else if (chat.getStatus() == 3) {
                textView2.setBackgroundResource(R.drawable.done_all_colo);
            }

        }
        else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getMsg());
            TextView textView1 = (TextView) convertView.findViewById(R.id.text1);
            textView1.setText(getformatteddate(chat.getCtime()));
            TextView textView3 = (TextView) convertView.findViewById(R.id.textheader);
            if (!groupName.isEmpty()) {
                textView3.setText(groupName);
                textView3.setVisibility(View.VISIBLE);
            }
            else {
                textView3.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }



/*    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vw = convertView;
        ViewHolder holder;
        ConvMessage chat = getItem(position);
        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(getContext());
            vw = inf.inflate(R.layout.fragment_chat, parent, false);
            holder = new ViewHolder();
            holder.msgview = (TextView) vw.findViewById(R.id.textchatmsg);
            holder.txt = (TextView) vw.findViewById(R.id.duration);
            holder.statusview= (ImageView)vw.findViewById(R.id.list_image) ;
            holder.rLayout = (RelativeLayout) vw.findViewById(R.id.textchat);
            vw.setTag(holder);
        } else {
            holder = (ViewHolder) vw.getTag();
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (chat.isMine() == true) {
            holder.statusview.setVisibility(View.VISIBLE);
            Resources res = mContext.getResources();
            Drawable drawable = res.getDrawable(R.drawable.msg_out);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.rLayout.setBackgroundDrawable(drawable);
            holder.msgview.setLayoutParams(layoutParams);
            //msgview.setBackgroundResource(R.drawable.bubble2);
            holder.msgview.setText(chat.getMsg());
            // msgview.setText(s.getMsg());
            holder.txt.setText(getformatteddate(chat.getCtime()));
            if (chat.getStatus() == 0) {
                holder.statusview.setBackgroundResource(R.drawable.schedule);
            }
            if (chat.getStatus() == 1) {
                holder.statusview.setBackgroundResource(R.drawable.done);
            } else if (chat.getStatus() == 2) {
                holder.statusview.setBackgroundResource(R.drawable.done_all);
            } else if (chat.getStatus() == 3) {
                holder.statusview.setBackgroundResource(R.drawable.done_all_colo);
            }
           // holder.statusview.setBackgroundResource(R.drawable.done);

        } else {
            holder.statusview.setVisibility(View.INVISIBLE);
            Resources res = mContext.getResources();
            Drawable drawable = res.getDrawable(R.drawable.msg_in);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.msgview.setLayoutParams(layoutParams);
            holder.rLayout.setBackgroundDrawable(drawable);
            // msgview.setBackgroundResource(R.drawable.bubble1);
            holder.msgview.setText(chat.getMsg());
            holder.txt.setText(getformatteddate(chat.getCtime()));

        }
        return vw;

    }*/

    public String getformatteddate(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);

        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "today at  " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "yesterday at " + timeFormatter.format(dateTime);
        } else {
            DateFormat dateform = new SimpleDateFormat("dd-MMM-yy");
            return dateform.format(dateTime) + " " + timeFormatter.format(dateTime);
        }

    }

}
