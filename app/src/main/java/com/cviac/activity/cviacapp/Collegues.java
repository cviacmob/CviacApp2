package com.cviac.activity.cviacapp;

import java.util.ArrayList;
import java.util.List;

import com.cviac.adapter.cviacapp.ColleguesAdapter;
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

public class Collegues extends Fragment {
	private ListView lv;
	List<Employee> emps;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View collegues = inflater.inflate(R.layout.collegues_frgs, container, false);
		
		lv=(ListView)collegues.findViewById(R.id.collegueslist);
		lv.setDivider(null);
		emps=getCollegues();
		   lv.setAdapter(new ColleguesAdapter(emps,getActivity().getApplicationContext()));


	        lv.setOnItemClickListener(new OnItemClickListener () {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
						long pos2) {
					
					Employee emp = emps.get(pos1);
					Conversation cov=new Conversation();
					cov.setEmpid(emp.getEmpID());
					cov.setName(emp.getName());
					Intent i = new Intent(getActivity().getApplicationContext(), FireChatActivity.class);
					i.putExtra("conversewith", cov);

					startActivity(i);
				
					//Toast.makeText(lv.getContext(), "clicked:"+ emp.getName(), Toast.LENGTH_SHORT).show();


				}});
		
		
		return collegues;
	}



	
	
	private List<Employee> getCollegues()
	{
		List<Employee> emplist = Employee.getemployees();
		return emplist;
	}
	
	
}
