package com.cviac.activity.cviacapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.fragments.cviacapp.Chats;
import com.cviac.fragments.cviacapp.Collegues;
import com.cviac.fragments.cviacapp.Events;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String mobile;

    private String empCode;

    private Chats chatFrag;

    private Collegues empFrag;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getString(R.string.app_name));
        getCollegues();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mobile = prefs.getString("mobile","");
        empCode = mobile;
        if (mobile != null) {
            Employee emplogged = Employee.getemployeeByMobile(mobile);
            if (emplogged != null) {
                empCode = emplogged.getEmp_code();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("empid", emplogged.getEmp_code());
                editor.putString("empname", emplogged.getEmp_name());
                editor.commit();
            }
        }

        new UpdateStatusTask().execute("online");
        new UpdatePushIDTask().execute();
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
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_profile) {
            final String MyPREFERENCES = "MyPrefs";
            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            String mobile = prefs.getString("mobile", "");
            Employee emplogged = Employee.getemployeeByMobile(mobile);
            if (emplogged != null) {
                Intent i = new Intent(HomeActivity.this, MyProfileActivity.class);
                i.putExtra("empcode", emplogged.getEmp_code());
                startActivity(i);
            }

            return true;
        }
        if(id==R.id.action_search)
        {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       int pos =  tabLayout.getSelectedTabPosition();
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        if (tab.getText().toString().equalsIgnoreCase("CHATS")) {
            chatFrag.reloadFilterByChats(newText);
        }
        else if (tab.getText().toString().equalsIgnoreCase("CONTACTS")) {
            empFrag.reloadFilterByName(newText);
        }
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position + 1) {
                case 1:
                    empFrag =  new Collegues();
                    return empFrag;
                case 2:
                    chatFrag = new Chats();
                    CVIACApplication app =  (CVIACApplication) getApplication();
                    app.setChatsFragment(chatFrag);
                    return chatFrag;
                case 3:
                    return new Events();
            }
            return null;

            // return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Contacts";
                case 1:
                    return "Chats";
                case 2:
                    return "Events";
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new UpdateStatusTask().execute("offline");
    }

    private void updatePresence(String status, String empCode) {

        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put("lastseen", new Date());
        updateValues.put("status", status);

        DatabaseReference mfiredbref = FirebaseDatabase.getInstance().getReference().child("presence");
        mfiredbref.child(empCode).updateChildren(
                updateValues,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                        if (firebaseError != null) {
                            Toast.makeText(HomeActivity.this,
                                    "Presence update failed: " + firebaseError.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(HomeActivity.this,
//                                    "Presence update success", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private class UpdateStatusTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... params) {
            updatePresence(params[0], empCode);
            return null;
        }
    }

    private void updatePushId(String empCode, String pushId) {

        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put("pushId", pushId);

        DatabaseReference mfiredbref = FirebaseDatabase.getInstance().getReference().child("presence");
        mfiredbref.child(empCode).updateChildren(
                updateValues,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                        if (firebaseError != null) {
                            Toast.makeText(HomeActivity.this,
                                    "PushID update failed: " + firebaseError.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            final String MyPREFERENCES = "MyPrefs";
                            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("pushIdsynced", "true");
                            editor.commit();
//                            Toast.makeText(HomeActivity.this,
//                                    "Presence update success", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private class UpdatePushIDTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... params) {
            final String MyPREFERENCES = "MyPrefs";
            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            String isSynced = prefs.getString("pushIdsynced","false");
            if (isSynced.equalsIgnoreCase("false")) {
                String pushId = prefs.getString("pushId","");
                updatePushId(empCode, pushId);
            }
            return null;
        }
    }

    private List<Employee> getCollegues()
    {
        List<Employee> emplist = Employee.getemployees();
        if (emplist != null && emplist.size() != 0) {
            return emplist;
        }


       /* List<Employee> emps = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.setName("Bala");
        emp.setEmpID("CV0089");
        emp.setEmailID("balachakravarthy@gmail.com");
        emp.setMobile("9791234809");
        emp.setGender("Male");
        emp.setManagername("Ramesh");
        emp.setDepartment("Mobility");
        emp.setDesignation("Software Engineer");
        emp.setImageurl(R.drawable.ic_launcher);
        emps.add(emp);
        emp.save();

        emp = new Employee();
        emp.setName("Sairam");
        emp.setEmpID("CV0090");

        emp.setEmailID("sairam_rangaraj@cviac.com");
        emp.setMobile("9894250016");
        emp.setGender("Male");
        emp.setManagername("Ramesh");
        emp.setDepartment("Mobility");
        emp.setDesignation("Software Engineer");
        emp.setImageurl(R.drawable.bala);
        emp.save();
        emps.add(emp);

        emp = new Employee();
        emp.setName("Gunaseelan");
        emp.setEmpID("CV0099");

        emp.setEmailID("gunaseelan_subburam@cviac.com");
        emp.setMobile("8489674524");
        emp.setGender("Male");
        emp.setManagername("Ramesh");
        emp.setDepartment("Mobility");
        emp.setDesignation("Software Engineer");
        emp.setImageurl(R.drawable.bala);
        emp.save();
        emps.add(emp);


        emp = new Employee();
        emp.setName("Shanmugam");
        emp.setEmpID("CV0091");
        emp.setEmailID("shanmugam_ekambaram@cviac.com");
        emp.setMobile("7871816364");
        emp.setGender("Male");
        emp.setManagername("Ramesh");
        emp.setDepartment("Mobility");
        emp.setDesignation("Software Engineer");
        emp.setImageurl(R.drawable.shan);
        emp.save();
        emps.add(emp);

        emp = new Employee();
        emp.setName("kadhiravan");
        emp.setEmpID("CV0098");
        emp.setEmailID("kathiravan_krishnan@cviac.com");
        emp.setMobile("9791402344");
        emp.setGender("male");
        emp.setManagername("Ramesh");
        emp.setDepartment("Mobility");
        emp.setDesignation("Software Engineer");
        emp.setImageurl(R.drawable.bala);
        emps.add(emp);
        emp.save();

        emp = new Employee();
        emp.setName("Vinoth Kumar");
        emp.setEmpID("CV0087");
        emp.setEmailID("vinothkumar_seenu@cviac.com");
        emp.setMobile("7092947730");
        emp.setGender("male");
        emp.setManagername("Ramesh");
        emp.setDepartment("Mobility");
        emp.setDesignation("Software Engineer");
        emp.setImageurl(R.drawable.bala);
        emps.add(emp);
        emp.save();

        emp = new Employee();
        emp.setName("Ramesh");
        emp.setEmpID("CV0100");
        emp.setEmailID("ramesh_ayyasamy@cviac.com");
        emp.setMobile("7893939008");
        emp.setGender("male");
        emp.setManagername("VC");
        emp.setDepartment("Mobility");
        emp.setDesignation("Software Engineer");
        emp.setImageurl(R.drawable.ic_launcher);
        emps.add(emp);
        emp.save();
*/
        return emplist;

    }

}
