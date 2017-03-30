package com.cviac.com.cviac.app.fragments;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cviac.activity.cviacapp.R;
import com.cviac.activity.cviacapp.XMPPChatActivity;
import com.cviac.com.cviac.app.adapaters.EventsAdapter;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EmployeeInfo;
import com.cviac.com.cviac.app.datamodels.EventInfo;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.cviac.com.cviac.app.restapis.GetStatus;
import com.squareup.okhttp.OkHttpClient;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class EventsFragment extends Fragment {

    private ListView lv1;
    List<Employee> emps;
    String receiverempname, receiverempcode,mobile,emp_namelogged;

    EventInfo emp;
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


                emp = empss.get(pos1);

                for (int i = 0; i <= emp.getEvent_title().length() && i <= emp.getEvent_id().length(); i++) {
                    receiverempname = emp.getEvent_title();
                    receiverempcode = emp.getEvent_id();
                }
                if (!emp_namelogged.equalsIgnoreCase(receiverempname)) {
                    // Toast.makeText(lv.getContext(), "clicked:" + receiverempname, Toast.LENGTH_SHORT).show();
                    // InviteorLanchByPresence(emp.getEmp_code());

                  converseORinvite();
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
    private  void converseORinvite() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
        Retrofit ret = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        CVIACApi api = ret.create(CVIACApi.class);
        final Call<GetStatus> call = api.getstatus(receiverempcode);
        call.enqueue(new Callback<GetStatus>() {
            @Override
            public void onResponse(Response<GetStatus> response, Retrofit retrofit) {
                GetStatus status = response.body();

                    OpenConversation(status);

            }

            @Override
            public void onFailure(Throwable throwable) {

                Toast.makeText(getActivity().getApplicationContext(), throwable.getMessage() + "Network Error", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void OpenConversation(GetStatus status) {
        Conversation cov = new Conversation();
        Employee emplogged = Employee.getemployee(emp.getEvent_id());
        cov.setEmpid(emp.getEvent_id());
        cov.setName(emp.getEvent_title());
        cov.setImageurl(emplogged.getImage_url());
        Context ctx = getActivity().getApplicationContext();
        if (ctx != null) {
//                                Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
//                                i.putExtra("conversewith", cov);
//                                startActivity(i);

            Intent i = new Intent(getActivity().getApplicationContext(), XMPPChatActivity.class);
            i.putExtra("conversewith", cov);
            i.putExtra("status", status);
            startActivity(i);
        }
    }
}
