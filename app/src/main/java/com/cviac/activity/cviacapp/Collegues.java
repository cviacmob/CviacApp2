package com.cviac.activity.cviacapp;

import java.util.List;

import com.cviac.adapter.cviacapp.ColleguesAdapter;
import com.cviac.cviacappapi.cviacapp.CVIACApi;
import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.Conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.GsonConverterFactory;

public class Collegues extends Fragment {
    private ListView lv;
    List<Employee> emps;
    ColleguesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View collegues = inflater.inflate(R.layout.collegues_frgs, container, false);

        lv = (ListView) collegues.findViewById(R.id.collegueslist);
        lv.setDivider(null);
        emps = getCollegues();
        adapter=new ColleguesAdapter(emps, getActivity().getApplicationContext());
        lv.setAdapter(adapter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CVIACApi api = retrofit.create(CVIACApi.class);
        final Call<List<Employee>> call = api.getEmployees();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Response<List<Employee>> response, Retrofit retrofit) {
            /*	Gson gson = new Gson();
				Employee[] users = gson.fromJson(response ,Employee[].class);
				System.out.println(Arrays.toString(users));
//or since we know that there will be only one object in array
				System.out.println(users[0]);*/
                List<Employee> esp = response.body();
                emps.addAll(esp);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Throwable throwable) {
                emps = null;
            }
        });
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
                                    long pos2) {

                Employee emp = emps.get(pos1);
                Conversation cov = new Conversation();
                cov.setEmpid(emp.getEmp_code());
                cov.setName(emp.getEmp_name());
                Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
                i.putExtra("conversewith", cov);

                startActivity(i);

                //Toast.makeText(lv.getContext(), "clicked:"+ emp.getName(), Toast.LENGTH_SHORT).show();


            }
        });


        return collegues;
    }


    private List<Employee> getCollegues() {
        List<Employee> emplist = Employee.getemployees();
        return emplist;
    }


}
