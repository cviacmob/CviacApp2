package com.cviac.com.cviac.app.fragments;

import java.util.List;

import com.cviac.activity.cviacapp.CVIACApplication;
import com.cviac.activity.cviacapp.FireChatActivity;
import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.adapaters.ColleguesAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.PresenceInfo;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import static android.content.Context.MODE_PRIVATE;

public class ContactsFragment extends Fragment {
    private ListView lv;
    List<Employee> emps;

    ColleguesAdapter adapter;
    Employee emp;
    String a;
    String mobile;
    String emp_namelogged;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vw = inflater.inflate(R.layout.collegues_frgs, container, false);
        lv = (ListView) vw.findViewById(R.id.collegueslist);
        lv.setDivider(null);
        emps = getCollegues();
        adapter = new ColleguesAdapter(emps, getActivity().getApplicationContext());
        lv.setAdapter(adapter);
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mobile = prefs.getString("mobile", "");
        Employee emplogged = Employee.getemployeeByMobile(mobile);
        emp_namelogged = emplogged.getEmp_name();

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
                                    long pos2) {


                emp = emps.get(pos1);

                for (int i = 0; i <= emp.getEmp_name().length(); i++) {
                    a = emp.getEmp_name();
                }
                if (emp_namelogged.equalsIgnoreCase(a)) {
                //do nothing
                } else {
                    InviteorLanchByPresence(emp.getEmp_code());
                }


                //Toast.makeText(lv.getContext(), "clicked:"+ emp.getName(), Toast.LENGTH_SHORT).show();


            }
        });


        return vw;
    }


    private List<Employee> getCollegues() {
        List<Employee> emplist = Employee.getemployees();
        return emplist;
    }

    public void reloadFilterByName(String searchName) {
        List<Employee> emplist = Employee.getemployees(searchName);
        emps.clear();
        emps.addAll(emplist);
        adapter.notifyDataSetChanged();
    }

    private void InviteorLanchByPresence(String empCode) {
        DatabaseReference mfiredbref = FirebaseDatabase.getInstance().getReference().child("presence");
        DatabaseReference dbRef = mfiredbref.child(empCode);
        if (dbRef != null) {
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        PresenceInfo presenceInfo = dataSnapshot.getValue(PresenceInfo.class);
                        if (presenceInfo != null) {
                            Conversation cov = new Conversation();
                            cov.setEmpid(emp.getEmp_code());
                            cov.setName(emp.getEmp_name());
                            cov.setImageurl(emp.getImage_url());
                            Context ctx = getActivity().getApplicationContext();
                            if (ctx != null) {
                                Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
                                i.putExtra("conversewith", cov);
                                startActivity(i);
                            }
                        } else {
                            alertforivite();

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void alertforivite() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("App Not Install");
        builder1.setMessage("Invite " + emp.getEmp_name() + " to install CVIAC App");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Invite",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        String message = "Greeting from CVIAC MOBILITY,\n\n"+"            "+emp_namelogged + " invited you to install the CviacChat App.Click the below link to insatll.\n"+"http://www.apps.cviac.com/mobileapps/cviacapp.apk";

                        Context ctx = getActivity().getApplicationContext();
                        if (ctx != null) {
                            CVIACApplication app = (CVIACApplication) getActivity().getApplicationContext();
                            String email=emp.getEmail();
                            app.sendEmail(email, "Install CviacChat App", message);
                        }

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
