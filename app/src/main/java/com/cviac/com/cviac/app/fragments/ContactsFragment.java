package com.cviac.com.cviac.app.fragments;

import java.util.List;

import com.cviac.activity.cviacapp.FireChatActivity;
import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.adapaters.ColleguesAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.Conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactsFragment extends Fragment {
    private ListView lv;
    List<Employee> emps;
    private RecyclerView recyclerview;
    ColleguesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vw = inflater.inflate(R.layout.collegues_frgs, container, false);
        lv = (ListView) vw.findViewById(R.id.collegueslist);
        lv.setDivider(null);
        emps = getCollegues();
        adapter = new ColleguesAdapter(emps, getActivity().getApplicationContext());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
                                    long pos2) {


                Employee emp = emps.get(pos1);
                Conversation cov = new Conversation();
                cov.setEmpid(emp.getEmp_code());
                cov.setName(emp.getEmp_name());
                cov.setImageurl(emp.getImage_url());
                Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
                i.putExtra("conversewith", cov);

                startActivity(i);

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


}