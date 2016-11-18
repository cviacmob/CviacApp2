package com.cviac.activity.cviacapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cviac.adapter.cviacapp.ColleguesAdapter;
import com.cviac.adapter.cviacapp.ConversationAdapter;
import com.cviac.datamodel.cviacapp.Collegue;
import com.cviac.datamodel.cviacapp.Conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;

public class Chats extends Fragment {
	private ListView lv;
	List<Conversation> emps;
	public ArrayAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View chatsfrgs = inflater.inflate(R.layout.chats_frgs, container, false);
		// ((TextView)chats.findViewById(R.id.chat)).setText("chats");

		lv = (ListView) chatsfrgs.findViewById(R.id.chatlist);
		lv.setDivider(null);
		emps = getConversation();
		lv.setAdapter(new ConversationAdapter(emps, getActivity().getApplicationContext()));
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Conversation selectedItem = emps.get(position);
				emps.remove(selectedItem);
				adapter.notifyDataSetChanged();
				return true;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos1, long pos2) {

				Conversation emp = emps.get(pos1);

				// Toast.makeText(lv.getContext(), "clicked:" + emp.getName(),
				// Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
				i.putExtra("conversewith", emp);
				startActivity(i);
			}
		});

		return chatsfrgs;
	}

	private List<Conversation> getConversation() {
		List<Conversation> emps = new ArrayList<Conversation>();
		Conversation emp = new Conversation();
		emp.setName("Renuga0");
		emp.setEmpid("123");
		emp.setLastmsg("welcome");
		emp.setDatetime(new Date());
		emps.add(emp);

		emp = new Conversation();

		emp.setName("Renuga");
		emp.setEmpid("1234");
		emp.setLastmsg("welcome");
		emp.setDatetime(new Date());

		emps.add(emp);

		emp = new Conversation();
		emp.setName("Renuga2");
		emp.setEmpid("1235");
		emp.setLastmsg("welcome");
		emp.setDatetime(new Date());

		emps.add(emp);

		emp = new Conversation();
		emp.setName("Renuga3");
		emp.setEmpid("1236");
		emp.setLastmsg("welcome");
		emp.setDatetime(new Date());

		emps.add(emp);

		emp = new Conversation();
		emp.setName("Renuga4");
		emp.setEmpid("1237");
		emp.setLastmsg("welcome");
		emp.setDatetime(new Date());

		emps.add(emp);

		return emps;

	}

}
