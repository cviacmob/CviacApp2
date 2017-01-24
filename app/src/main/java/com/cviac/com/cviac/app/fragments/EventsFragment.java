package com.cviac.com.cviac.app.fragments;

import java.util.List;

import com.cviac.activity.cviacapp.R;
import com.cviac.com.cviac.app.adapaters.EventsAdapter;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EventInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
        lv1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
                                    long pos2) {

                EventInfo emp = empss.get(pos1);

                // Toast.makeText(lv1.getContext(), "clicked:" + emp.getEvent_title(), Toast.LENGTH_SHORT).show();


            }
        });


        return events;

    }

    private List<EventInfo> getEvents() {
        return EventInfo.getevents();
    }


}
