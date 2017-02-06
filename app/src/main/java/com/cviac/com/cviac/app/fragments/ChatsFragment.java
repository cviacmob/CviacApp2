package com.cviac.com.cviac.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cviac.activity.cviacapp.R;
import com.cviac.activity.cviacapp.XMPPChatActivity;
import com.cviac.com.cviac.app.adapaters.ConversationAdapter;
import com.cviac.com.cviac.app.datamodels.Conversation;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
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
        if (emps == null) {
            emps = new ArrayList<>();
        }
        adapter = new ConversationAdapter(emps, getActivity().getApplicationContext());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1, long pos2) {
                Conversation emp = emps.get(pos1);
                //Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
                Intent i = new Intent(getActivity().getApplicationContext(), XMPPChatActivity.class);
                i.putExtra("conversewith", emp);
                startActivity(i);
            }
        });
        return chatsfrgs;
    }

    public void reloadConversation() {
        emps.clear();
        emps.addAll(getConversation());
        adapter.notifyDataSetChanged();
    }

    private List<Conversation> getConversation() {
        return Conversation.getConversations();
    }

    public void reloadFilterByChats(String searchName) {
        List<Conversation> chatlist = Conversation.getmessage(searchName);
        emps.clear();
        emps.addAll(chatlist);
        adapter.notifyDataSetChanged();
    }
}
