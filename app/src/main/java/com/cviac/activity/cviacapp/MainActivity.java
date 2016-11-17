/*
package com.cviac.activity.cviacapp;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.R.color;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import com.cviac.adapter.TabPagerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends FragmentActivity implements OnQueryTextListener {
    ViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;

    SearchManager searchManager;
    SearchView searchView;
    MenuItem searchMenuItem;
    */
/**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     *//*

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());

        Tab = (ViewPager) findViewById(R.id.pager1);
        Tab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                actionBar = getActionBar();
                actionBar.setSelectedNavigationItem(position);
            }
        });
        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();
        // Enable Tabs on Action Bar
       // actionBar.setBackgroundDrawable(new ColorDrawable(color.black));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                Tab.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub

            }
        };
        // Add New Tab
        actionBar.addTab(actionBar.newTab().setText("Contacts").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Chats").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Events").setTabListener(tabListener));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    */
/**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     *//*

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
*/
