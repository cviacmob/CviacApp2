package com.cviac.com.cviac.app.fragments;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cviac.activity.cviacapp.MyProfile;
import com.cviac.activity.cviacapp.R;
import com.cviac.activity.cviacapp.Verification;
import com.cviac.activity.cviacapp.XMPPChatActivity;
import com.cviac.com.cviac.app.adapaters.ColleguesAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.EmployeeInfo;
import com.cviac.com.cviac.app.datamodels.PresenceInfo;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.cviac.com.cviac.app.restapis.GeneralResponse;
import com.cviac.com.cviac.app.restapis.GetStatus;
import com.cviac.com.cviac.app.restapis.SMSInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.OkHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView lv;
    List<Employee> emps;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ColleguesAdapter adapter;
    Employee emp;
    String receiverempname, receiverempcode, GetStatusByID;
    String mobile;
    ProgressDialog progressDialog;
    String emp_namelogged;
    List<EmployeeInfo> emplist;
    String code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vw = inflater.inflate(R.layout.collegues_frgs, container, false);
        lv = (ListView) vw.findViewById(R.id.collegueslist);
        lv.setDivider(null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) vw.findViewById(R.id.activity_main_swipe_refresh_layoutcollegue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
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

                for (int i = 0; i <= emp.getEmp_name().length() && i <= emp.getEmp_code().length(); i++) {
                    receiverempname = emp.getEmp_name();
                    receiverempcode = emp.getEmp_code();
                }
                if (!emp_namelogged.equalsIgnoreCase(receiverempname)) {
                    // Toast.makeText(lv.getContext(), "clicked:" + receiverempname, Toast.LENGTH_SHORT).show();
                    // InviteorLanchByPresence(emp.getEmp_code());

                    converseORinvite();
                }




            }
        });

        if (mSwipeRefreshLayout != null)

        {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.bluerefersh);
        }
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

 /*   private void InviteorLanchByPresence(String empCode) {
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
//                                Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
//                                i.putExtra("conversewith", cov);
//                                startActivity(i);

                                Intent i = new Intent(getActivity().getApplicationContext(), XMPPChatActivity.class);
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

    }*/



    private void Smsinvite() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("App Not Install");
        builder1.setMessage("Invite " + emp.getEmp_name() + " to install CVIAC App");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Invite",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        String message = emp_namelogged + " invited you to install CVIAC Chat App.\nClick below link to install.\n" + "http://www.apps.cviac.com/mobileapps/cviacapp.apk";

                        Context ctx = getActivity().getApplicationContext();
                        if (ctx != null) {
                            // CVIACApplication app = (CVIACApplication) getActivity().getApplicationContext();
                            String mobile = emp.getMobile();
                            // sendMobile(mobile, message);
                            sendsms(mobile, message);


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

    private void getEmployees() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
        Retrofit ret = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        CVIACApi api = ret.create(CVIACApi.class);
        final Call<List<EmployeeInfo>> call = api.getEmployees();
        call.enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                emplist = response.body();
                Employee.deleteAll();
                saveEmployeeInfo(emplist);
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity().getApplicationContext(), "your contacts list has been updated", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Throwable throwable) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity().getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                //emps = null;
            }
        });

    }

    private void saveEmployeeInfo(List<EmployeeInfo> empInfos) {
        for (EmployeeInfo empinfo : emplist) {
            Employee emp = new Employee();
            emp.setEmp_name(empinfo.getEmp_name());
            emp.setMobile(empinfo.getMobile());
            emp.setEmail(empinfo.getEmail());
            emp.setEmp_code(empinfo.getEmp_code());
            emp.setDepartment(empinfo.getDepartment());
            emp.setDesignation(empinfo.getDesignation());
            emp.setDob(empinfo.getDob());
            emp.setGender(empinfo.getGender());
            emp.setManager(empinfo.getManager());
            emp.setImage_url(empinfo.getImage_url());
            emp.setPush_id(empinfo.getPush_id());
            emp.setDoj(empinfo.getDoj());
            emp.setStatus(empinfo.getStatus());
            emp.save();

        }
    }

    @Override
    public void onRefresh() {
        getEmployees();

    }

    private void sendsms(String mobile, String msgBody) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CVIACApi api = retrofit.create(CVIACApi.class);
        SMSInfo emailinfo = new SMSInfo(mobile, msgBody);
        Call<GeneralResponse> call = api.sendMobile(emailinfo);
        call.enqueue(new retrofit.Callback<GeneralResponse>() {
            @Override
            public void onResponse(retrofit.Response<GeneralResponse> response, Retrofit retrofit) {
                int code;
                GeneralResponse rsp = response.body();
                code = rsp.getCode();
                if (code == 0) {
                    Toast.makeText(getActivity(), "invite Success", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void converseORinvite() {
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
                if (status.getStatus() == null || status.getStatus().isEmpty()) {
                    Smsinvite();
                } else {
                    OpenConversation(status);
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

                Toast.makeText(getActivity().getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void OpenConversation(GetStatus status) {
        Conversation cov = new Conversation();
        cov.setEmpid(emp.getEmp_code());
        cov.setName(emp.getEmp_name());
        cov.setImageurl(emp.getImage_url());
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
    private void setProgressDialog() {
        progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
