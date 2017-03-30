package com.cviac.activity.cviacapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.cviac.com.cviac.app.adapaters.ColleguesAdapter;
import com.cviac.com.cviac.app.adapaters.GroupInfoAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.GroupMemberInfo;

import java.util.List;

public class GroupInfoActivity extends AppCompatActivity {
    private ListView lv;
    GroupInfoAdapter adapter;
    List<GroupMemberInfo> grpinfo;
    private String groupname,groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_frgs);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        Intent in =getIntent();
        groupname=in.getStringExtra("groupname");
        groupid=in.getStringExtra("groupid");
        ab.setTitle(groupname);
        ab.setSubtitle("members");
        lv = (ListView) findViewById(R.id.chatlist);
        lv.setDivider(null);
        grpinfo = getmembers();
        adapter = new GroupInfoAdapter(grpinfo, this);
        lv.setAdapter(adapter);
        lv.setAdapter(adapter);


    }

    private List<GroupMemberInfo> getmembers() {
        List<GroupMemberInfo> emplist = GroupMemberInfo.getmembers(groupid);
        return emplist;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
