package com.cviac.activity.cviacapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cviac.com.cviac.app.adapaters.CircleTransform;
import com.cviac.com.cviac.app.adapaters.GroupAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.xmpp.XMPPService;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupNameActivity extends AppCompatActivity {
    private EditText groupname;
    private String grpname;
    private String details;
    private List<Employee> selectedEmps;
    private GridView gv;
    GroupAdapter adapter;
    private ImageView grpphoto;
    private TextView participants;
    ArrayList<String> selectedlist;
    private Button nextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("New Group");
        ab.setSubtitle("Add subjects");

        Intent i = getIntent();
        selectedlist = (ArrayList<String>) i.getStringArrayListExtra("selected");
        int totalContacts=i.getIntExtra("totalcontacts",0);

        selectedEmps = Employee.getSelectedemployees(selectedlist);

        groupname = (EditText) findViewById(R.id.groupname);

        grpphoto = (ImageView) findViewById(R.id.grpicon);

        gv = (GridView) findViewById(R.id.gdview);
        participants=(TextView)findViewById(R.id.participatensname);
        int count=selectedEmps.size();
        participants.setText("Participants :"+ count+"/"+totalContacts);
        adapter = new GroupAdapter(selectedEmps, this.getApplicationContext());
        gv.setAdapter(adapter);

        Picasso.with(this).load(R.drawable.groupicon).resize(120, 120).transform(new CircleTransform())
                .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(grpphoto);




        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grpname = groupname.getText().toString();
                if(!grpname.isEmpty()&& grpname!=null) {
//                    Intent i = new Intent(GroupNameActivity.this, HomeActivity.class);
//                    startActivity(i);
//                    finish();

                    if (XMPPService.xmpp != null ) {
                        XMPPService.xmpp.createGroup(grpname,selectedlist);
                        Intent intent = new Intent();
                        intent.setAction("Activityfinish");
                        intent.putExtra("activityclosed", "finish");
                        getApplicationContext().sendBroadcast(intent);
                        finish();
                    }

                }else{
                    Toast.makeText(GroupNameActivity.this,"Enter Group Name",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
