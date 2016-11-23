/*
package com.cviac.activity.cviacapp;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.cviac.adapter.cviacapp.ColleguesAdapter;
import com.cviac.adapter.cviacapp.EmployeeDetailsAdapter;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.Collegue;
import com.cviac.datamodel.cviacapp.Employee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import static com.cviac.activity.cviacapp.R.id.container;

public class ProfileActivity extends AppCompatActivity {
    private EmployeeDetailsAdapter empAdapter;
    private List<Employee> emps;
    Inflater inflater;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.profile);

      //View employees = inflater.inflate(R.layout.profile, container, false);
        final ListView lv = (ListView) findViewById(R.id.listviewdetails);
        emps=getemployee();
        emps= (List<Employee>) new EmployeeDetailsAdapter(emps,this);
lv.setAdapter(new EmployeeDetailsAdapter(emps,getApplicationContext()));
      //  lv.setAdapter(new EmployeeDetailsAdapter(emps,getApplicationContext()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
                                    long pos2) {

                Employee emp = emps.get(pos1);

                Toast.makeText(lv.getContext(), "clicked:"+ emp.getName(), Toast.LENGTH_SHORT).show();

            }});





    }




    private List<Employee> getemployee()
    {
        List<Employee> emps = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.setName("bala");
        emp.setEmpID("cc001");
        emp.setEmailID("bala.gp@gmail.com");
        emp.setMobile("31125466");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        Date date = new Date("1998/02/12");
        //String s2 = dateFormat.format(date);
        emp.setDob(date);
        emp.setGender("male");
        emp.setManagerID("cc02");
        emp.setdepartment("mobility");

        emp.setDegination("software engineer");
        int status = Integer.parseInt("1");
        emp.setstatus(status);
        return emps;
    }


}
*/
