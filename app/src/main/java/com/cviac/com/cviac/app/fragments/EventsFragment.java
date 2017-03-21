package com.cviac.com.cviac.app.fragments;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.adapaters.EventsAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EventInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class EventsFragment extends Fragment {

    private ListView lv1;

    private List<EventInfo> empss;

    private EventsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View events = inflater.inflate(R.layout.events_frgs, container, false);
        //((TextView)event.findViewById(R.id.events)).setText("EventsFragment");
        lv1 = (ListView) events.findViewById(R.id.eventslist);
        lv1.setDivider(null);
        empss = getEvents();

        adapter = new EventsAdapter(empss, getActivity().getApplicationContext());
        lv1.setAdapter(adapter);
        return events;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    private List<EventInfo> getEvents() {
        return EventInfo.getevents();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.refresh_action).setVisible(false);
        menu.findItem(R.id.action_newgroup).setVisible(false);
        menu.findItem(R.id.action_profile).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

}
