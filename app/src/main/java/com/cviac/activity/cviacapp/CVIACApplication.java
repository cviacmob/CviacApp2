package com.cviac.activity.cviacapp;


import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.Conversation;
import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.Event;
import com.cviac.fragments.cviacapp.Chats;


/**
 * Created by Cviac on 17/11/2016.
 */

public class CVIACApplication extends MultiDexApplication {

    private Chats chatsFragment;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(ChatMessage.class);
        configurationBuilder.addModelClasses(Employee.class);
        configurationBuilder.addModelClasses(Event.class);
        configurationBuilder.addModelClasses(Conversation.class);
        ActiveAndroid.initialize(configurationBuilder.create());

    }

    private boolean networkStatus = true;

    public boolean isNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(boolean networkStatus) {
        this.networkStatus = networkStatus;
    }

    public Chats getChatsFragment() {
        return chatsFragment;
    }

    public void setChatsFragment(Chats chatsFragment) {
        this.chatsFragment = chatsFragment;
    }
}


