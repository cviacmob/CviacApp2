package com.cviac.com.cviac.app.fragments;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.adapaters.EventsAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EmployeeInfo;
import com.cviac.com.cviac.app.datamodels.EventInfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import static android.content.Context.MODE_PRIVATE;

public class EventsFragment extends Fragment {

    private ListView lv1;
    List<Employee> emps;
    String receiverempname, receiverempcode,mobile,emp_namelogged;

    Employee emp;
    private List<EventInfo> empss;

    private EventsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View events = inflater.inflate(R.layout.events_frgs, container, false);
        //((TextView)event.findViewById(R.id.events)).setText("EventsFragment");
        lv1 = (ListView) events.findViewById(R.id.eventslist);
        lv1.setDivider(null);
        empss = getEvents();
        adapter = new EventsAdapter(empss, getActivity().getApplicationContext());
        lv1.setAdapter(adapter);
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mobile = prefs.getString("mobile", "");
        Employee emplogged = Employee.getemployeeByMobile(mobile);

        emp_namelogged = emplogged.getEmp_name();

        lv1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
                                    long pos2) {


                emp = emps.get(pos1);

                for (int i = 0; i <= emp.getEmp_name().length() && i <= emp.getEmp_code().length(); i++) {
                    receiverempname = emp.getEmp_name();
                    receiverempcode = emp.getEmp_code();
                }
                if (!emp_namelogged.equalsIgnoreCase(receiverempname)) {
                    // Toast.makeText(lv.getContext(), "clicked:" + receiverempname, Toast.LENGTH_SHORT).show();
                    // InviteorLanchByPresence(emp.getEmp_code());
                    ContactsFragment confrgs= new ContactsFragment();
                    confrgs.converseORinvite();
                }
            }
        });
        return events;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    private List<Employee> getCollegues() {
        List<Employee> emplist = Employee.getemployees();
        return emplist;
    }

    private List<EventInfo> getEvents() {
        return EventInfo.getevents();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.refresh_action).setVisible(false);
        menu.findItem(R.id.action_newgroup).setVisible(false);
        menu.findItem(R.id.action_profile).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

}
