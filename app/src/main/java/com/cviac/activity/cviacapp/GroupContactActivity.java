package com.cviac.activity.cviacapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cviac.com.cviac.app.adapaters.ColleguesAdapter;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EmployeeInfo;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.squareup.okhttp.OkHttpClient;


import java.util.ArrayList;
import java.util.List;


public class GroupContactActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
        private ListView lv;
    List<Employee> emps;
    ColleguesAdapter adapter;
    Employee emp;
    String mobile;
    String emp_namelogged;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_contact);
        //setTitle("");
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        lv = (ListView) findViewById(R.id.collegueslist);
        lv.setDivider(null);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("New Group");
        ab.setSubtitle("Add participants");
        emps = getCollegues();
        adapter = new ColleguesAdapter(emps, this.getApplicationContext());
        lv.setAdapter(adapter);
        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mobile = prefs.getString("mobile", "");
        Employee emplogged = Employee.getemployeeByMobile(mobile);

        emp_namelogged = emplogged.getEmp_name();

      /*  lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                lv.setItemChecked(position, true);
                return true;
            }
        });*/
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = lv.getCheckedItemCount();
                // Set the  CAB title according to total checked items
                mode.setTitle(checkedCount + "  Selected");
                // Calls  toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);


            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.multipleselect, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.selectAll:
                        //
                        final int checkedCount = emps.size();
                        // If item  is already selected or checked then remove or
                        // unchecked  and again select all
                        adapter.removeSelection();
                        for (int i = 0; i < checkedCount; i++) {
                            lv.setItemChecked(i, true);
                            //  listviewadapter.toggleSelection(i);
                        }

                        mode.setTitle(checkedCount + "  Selected");
                        return true;
                    case R.id.next:
                        // Add  dialog for confirmation to delete selected item

                        SparseBooleanArray selected = adapter.getSelectedIds();

                        ArrayList<String> selectedEmps = new ArrayList<String>();
                        for (int i = 0; i < selected.size(); i++) {
                            if (selected.valueAt(i)) {
                                int indx = selected.keyAt(i);
                                Employee ee = emps.get(indx);
                                selectedEmps.add(ee.getEmp_code());
                            }
                        }
                        Intent i = new Intent(GroupContactActivity.this, GroupNameActivity.class);
                        i.putStringArrayListExtra("selected", selectedEmps);
                        startActivity(i);


                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });

    }

    private List<Employee> getCollegues() {
        List<Employee> emplist = Employee.getemployees();
        return emplist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(GroupContactActivity.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.reloadFilterByChats(newText);
        return false;
    }

    public void reloadFilterByChats(String searchName) {
        List<Employee> emplist = Employee.getemployees(searchName);
        emps.clear();
        emps.addAll(emplist);
        adapter.notifyDataSetChanged();
    }

}
