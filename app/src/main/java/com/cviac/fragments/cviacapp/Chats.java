package com.cviac.fragments.cviacapp;

import java.util.ArrayList;
import java.util.List;

import com.cviac.activity.cviacapp.FireChatActivity;
import com.cviac.activity.cviacapp.R;
import com.cviac.adapter.cviacapp.ConversationAdapter;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.Conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
		adapter = new ConversationAdapter(emps, getActivity().getApplicationContext());
		lv.setAdapter(adapter);
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
				Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
				i.putExtra("conversewith", emp);
				startActivity(i);


			}
		});
		return chatsfrgs;
	}

	private List<Conversation> getConversation() {
		return Conversation.getConversations();
	}
}