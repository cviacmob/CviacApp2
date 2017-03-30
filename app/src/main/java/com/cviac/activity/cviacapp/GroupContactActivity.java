package com.cviac.activity.cviacapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cviac.com.cviac.app.adapaters.ColleguesAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;


import java.util.ArrayList;
import java.util.List;


public class GroupContactActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ListView lv;
    List<Employee> emps;
    ColleguesAdapter adapter;
    Employee emp;
    String mobile;
    String emp_namelogged;
    Context mcontext;
    TextView mSearchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_contact);
        //setTitle("");

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        lv = (ListView) findViewById(R.id.collegueslist);
        lv.setDivider(null);
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

        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray selected = adapter.getSelectedIds();

                ArrayList<String> selectedEmps = new ArrayList<String>();
                for (int i = 0; i < selected.size(); i++) {
                    if (selected.valueAt(i)) {
                        int indx = selected.keyAt(i);
                        Employee ee = emps.get(indx);
                        selectedEmps.add(ee.getEmp_code());
                    }
                }
                if (!selectedEmps.isEmpty()) {
                    final int checkedCount = emps.size();
                    Intent i = new Intent(GroupContactActivity.this, GroupNameActivity.class);
                    i.putStringArrayListExtra("selected", selectedEmps);
                    i.putExtra("totalcontacts", checkedCount);
                    startActivity(i);
                } else {
                    Toast.makeText(GroupContactActivity.this, "select contacts", Toast.LENGTH_SHORT).show();
                }

            }
        });
        setuserSelectLisiner();

    }

    private void setuserSelectLisiner() {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                        mode.setTitle(lv.getCheckedItemCount() + " Selected");
                        adapter.toggleSelection(position);
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.menu_select, menu);
                        SearchManager searchManager =
                                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                        SearchView searchView =
                                (SearchView) menu.findItem(R.id.action_search).getActionView();
                        searchView.setSearchableInfo(
                                searchManager.getSearchableInfo(getComponentName()));
                        searchView.setSubmitButtonEnabled(true);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                reloadFilterByChats(newText);
                                return false;
                            }
                        });
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

                                }
                                mode.setTitle(checkedCount + "  Selected");


                                return true;


                            case R.id.action_search:
                                MenuItem searchItem = mode.getMenu().findItem(R.id.action_search);
                                SearchManager searchManager = (SearchManager) GroupContactActivity.this.getSystemService(Context.SEARCH_SERVICE);
                                SearchView searchView = null;
                                if (searchItem != null) {
                                    searchView = (SearchView) searchItem.getActionView();
                                }
                                if (searchView != null) {
                                    searchView.setSearchableInfo(searchManager.getSearchableInfo(GroupContactActivity.this.getComponentName()));
                                    searchView.setSubmitButtonEnabled(true);
                                    searchView.setOnQueryTextListener(GroupContactActivity.this);

                                }
                                return true;
                            default:
                                return false;
                        }

                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }
                });
                return false;
            }
        });
    }


    private List<Employee> getCollegues() {
        List<Employee> emplist = Employee.getemployees();
        return emplist;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    public void reloadFilterByChats(String searchName) {
        List<Employee> emplist = Employee.getemployees(searchName);
        emps.clear();
        emps.addAll(emplist);
        adapter.notifyDataSetChanged();
    }

}
