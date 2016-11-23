package com.cviac.activity.cviacapp;

import java.util.ArrayList;
import java.util.List;

import com.cviac.adapter.cviacapp.ColleguesAdapter;
import com.cviac.datamodel.cviacapp.Collegue;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Collegues extends Fragment {
	private ListView lv;
	List<Collegue> emps;
	
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
					
					Collegue emp = emps.get(pos1);
				
					Toast.makeText(lv.getContext(), "clicked:"+ emp.getName(), Toast.LENGTH_SHORT).show();
					
				}});
		
		
		return collegues;
	}

	
	
	private List<Collegue> getCollegues()
	{
		List<Collegue> emps = new ArrayList<Collegue>();
		Collegue emp = new Collegue();
		emp.setName("Renuga");
		emp.setEmpID("CV0089");
		
		emp.setEmailID("renuga@cviac.com");
		emps.add(emp);
		
		emp = new Collegue();
		emp.setName("Renuga");
		emp.setEmpID("CV0089");
		
		emp.setEmailID("renuga@cviac.com");
		
		emps.add(emp);
		
		emp = new Collegue();
		emp.setName("Renuga2");
		emp.setEmpID("CV0090");
		
		emp.setEmailID("renuga2@cviac.com");
		emps.add(emp);
		
		
		emp = new Collegue();
		emp.setName("Renuga3");
		emp.setEmpID("CV0091");
		emp.setEmailID("renuga3@cviac.com");
		
		emps.add(emp);
		
		emp = new Collegue();
		emp.setName("Renuga4");
		emp.setEmpID("CV0092");
		emp.setEmailID("renuga4@cviac.com");
		emps.add(emp);
		
		return emps;
		
	}
	
	
}
