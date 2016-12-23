package com.cviac.activity.cviacapp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.cviac.datamodel.cviacapp.ChatMessage;
import com.cviac.datamodel.cviacapp.Conversation;
import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.EventInfo;
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
        configurationBuilder.addModelClasses(EventInfo.class);
        configurationBuilder.addModelClasses(Conversation.class);
        ActiveAndroid.initialize(configurationBuilder.create());

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

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


