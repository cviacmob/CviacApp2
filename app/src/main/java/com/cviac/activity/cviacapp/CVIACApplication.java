package com.cviac.activity.cviacapp;


import android.app.Application;
import android.content.ContextWrapper;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.Event;

/**
 * Created by Cviac on 17/11/2016.
 */

public class CVIACApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(ChatMessage.class);
        configurationBuilder.addModelClasses(Employee.class);
        configurationBuilder.addModelClasses(Event.class);
        ActiveAndroid.initialize(configurationBuilder.create());

    }
}


