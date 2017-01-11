package com.cviac.activity.cviacapp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.cviac.com.cviac.app.datamodels.ChatMessage;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EventInfo;
import com.cviac.com.cviac.app.fragments.ChatsFragment;


/**
 * Created by Cviac on 17/11/2016.
 */

public class CVIACApplication extends MultiDexApplication {


    private ChatsFragment chatsFragment;

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

    public ChatsFragment getChatsFragment() {
        return chatsFragment;
    }

    public void setChatsFragment(ChatsFragment chatsFragment) {
        this.chatsFragment = chatsFragment;
    }

    /*public void sendMobile(String mobile, String msgBody) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CVIACApi api = retrofit.create(CVIACApi.class);
        MobileInfo emailinfo = new MobileInfo(mobile, msgBody);
        Call<MobileResponse> call = api.sendMobile(emailinfo);
        call.enqueue(new retrofit.Callback<MobileResponse>() {
            @Override
            public void onResponse(retrofit.Response<MobileResponse> response, Retrofit retrofit) {
                int code;
                MobileResponse rsp = response.body();
                code = rsp.getCode();
                if (code == 0) {
                    Toast.makeText(CVIACApplication.this, "invite Success", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }*/

}


