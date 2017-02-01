package com.cviac.activity.cviacapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.cviac.com.cviac.app.receivers.AlarmReceiver;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EmployeeInfo;
import com.cviac.com.cviac.app.fragments.ChatsFragment;
import com.cviac.com.cviac.app.fragments.ContactsFragment;
import com.cviac.com.cviac.app.fragments.EventsFragment;
import com.cviac.com.cviac.app.restapis.GeneralResponse;
import com.cviac.com.cviac.app.restapis.PushInfo;
import com.cviac.com.cviac.app.xmpp.ChatMessage;
import com.cviac.com.cviac.app.xmpp.LocalBinder;
import com.cviac.com.cviac.app.xmpp.XMPPService;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.OkHttpClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "HomeActivity";

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
    List<EmployeeInfo> emplist;
    CoordinatorLayout coordinatorLayout;
    private String mobile;
    ProgressDialog progressDialog;

    private String empCode;
    Context mcontext;

    private ChatsFragment chatFrag;

    private ContactsFragment empFrag;
    private EventsFragment eventsFrag;

    TabLayout tabLayout;
    Employee emplogged;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private XMPPService mService;

    private boolean mBounded;

    private final ServiceConnection mConnection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mService = ((LocalBinder<XMPPService>) service).getService();
            mBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getString(R.string.app_name));
        getCollegues();

        setAlaram();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.main_content);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setupWithViewPager(mViewPager);

        final String MyPREFERENCES = "MyPrefs";
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mobile = prefs.getString("mobile", "");
        empCode = mobile;
        if (mobile != null) {
            emplogged = Employee.getemployeeByMobile(mobile);
            if (emplogged != null) {
                empCode = emplogged.getEmp_code();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("empid", emplogged.getEmp_code());
                editor.putString("empname", emplogged.getEmp_name());
                editor.commit();
                doBindService();
            }
        }

        new UpdateStatusTask().execute("online");
        new UpdatePushIDTask().execute();
    }

    private void setAlaram() {
        //List<Employee> emplist = Employee.eventsbydate();

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 0);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

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
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.refresh_action) {
            // DeleteEmployeeInfo(emplist);
            getEmployees();

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
        int pos = tabLayout.getSelectedTabPosition();
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        if (tab.getText().toString().equalsIgnoreCase("CHATS")) {
            chatFrag.reloadFilterByChats(newText);
        } else if (tab.getText().toString().equalsIgnoreCase("CONTACTS")) {
            if (empFrag != null) {
                empFrag.reloadFilterByName(newText);
            }

        } else if (tab.getText().toString().equalsIgnoreCase("EventsFragment")) {
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
                    empFrag = new ContactsFragment();

                    return empFrag;
                case 2:
                    chatFrag = new ChatsFragment();
                    CVIACApplication app = (CVIACApplication) getApplication();
                    app.setChatsFragment(chatFrag);
                    return chatFrag;
                case 3:
                    eventsFrag = new EventsFragment();
                    return eventsFrag;
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
        //new UpdateStatusTask().execute("offline");
        doUnbindService();
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

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
        Retrofit ret = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        CVIACApi api = ret.create(CVIACApi.class);
        PushInfo info = new PushInfo();
        info.setEmp_code(empCode);
        info.setPush_id(pushId);
        final Call<GeneralResponse> call = api.updatePushId(info);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Response<GeneralResponse> response, Retrofit retrofit) {
                GeneralResponse rsp = response.body();
                if (rsp.getCode() == 0) {
                    Toast.makeText(HomeActivity.this, "PushId Registered", Toast.LENGTH_SHORT).show();
                    final String MyPREFERENCES = "MyPrefs";
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("pushIdsynced", "true");
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });

    }


//    private void updatePushId(String empCode, String pushId) {
//
//        Map<String, Object> updateValues = new HashMap<>();
//        updateValues.put("pushId", pushId);
//
//        DatabaseReference mfiredbref = FirebaseDatabase.getInstance().getReference().child("presence");
//        mfiredbref.child(empCode).updateChildren(
//                updateValues,
//                new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
//                        if (firebaseError != null) {
//                            Toast.makeText(HomeActivity.this,
//                                    "PushID update failed: " + firebaseError.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        } else {
//                            final String MyPREFERENCES = "MyPrefs";
//                            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefs.edit();
//                            editor.putString("pushIdsynced", "true");
//                            editor.commit();
////                            Toast.makeText(HomeActivity.this,
////                                    "Presence update success", Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//                });
//    }

    private class UpdatePushIDTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... params) {
            final String MyPREFERENCES = "MyPrefs";
            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            String isSynced = prefs.getString("pushIdsynced", "false");
            if (isSynced.equalsIgnoreCase("false")) {
                String pushId = prefs.getString("pushId", "");
                updatePushId(empCode, pushId);
            }
            return null;
        }
    }

    private List<Employee> getCollegues() {
        List<Employee> emplist = Employee.getemployees();
        if (emplist != null && emplist.size() != 0) {
            return emplist;
        }

        return emplist;

    }

    private void setProgressDialog() {
        progressDialog = new ProgressDialog(HomeActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);

        progressDialog.setMessage("Refreshing your contacts...");
        progressDialog.setCancelable(false);
        progressDialog.show();
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
        setProgressDialog();
        final Call<List<EmployeeInfo>> call = api.getEmployees();
        call.enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                emplist = response.body();
                progressDialog.setMessage("Saving Contacts to database...");
                Employee.deleteAll();
                saveEmployeeInfo(emplist);
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "your contacts list has been updated", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "API Invoke Error :" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

            emp.save();

        }
    }

    void doBindService() {
        bindService(new Intent(this, XMPPService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    public XMPPService getmService() {
        return mService;
    }
public  void getsnackbar()
{
    if(getApplicationContext()!=null){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "XMPP server connected!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
    }

}
}
