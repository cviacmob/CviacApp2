package com.cviac.activity.cviacapp;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import com.cviac.com.cviac.app.datamodels.ConvMessage;
import com.cviac.com.cviac.app.datamodels.Conversation;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EventInfo;

import com.cviac.com.cviac.app.datamodels.GroupInfo;
import com.cviac.com.cviac.app.datamodels.GroupMemberInfo;
import com.cviac.com.cviac.app.datamodels.Groupmembers;
import com.cviac.com.cviac.app.fragments.ChatsFragment;


/**
 * Created by Cviac on 17/11/2016.
 */

public class CVIACApplication extends MultiDexApplication {


    private ChatsFragment chatsFragment;

    private XMPPChatActivity xmppChatActivity;

    private XMPPGroupChatActivity xmppGroupChatActivity;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(ConvMessage.class);
        configurationBuilder.addModelClasses(Employee.class);
        configurationBuilder.addModelClasses(EventInfo.class);
        configurationBuilder.addModelClasses(Conversation.class);
        configurationBuilder.addModelClasses(GroupInfo.class);
        configurationBuilder.addModelClasses(GroupMemberInfo.class);


        ActiveAndroid.initialize(configurationBuilder.create());
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

    public void setChatActivty(XMPPChatActivity xmppChatActivity) {
        this.xmppChatActivity = xmppChatActivity;
    }

    public XMPPChatActivity getChatActivty() {
       return xmppChatActivity;
    }

    public XMPPGroupChatActivity getGroupChatActivity() {
        return xmppGroupChatActivity;
    }

    public void setGroupChatActivity(XMPPGroupChatActivity act) {
        xmppGroupChatActivity = act;
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


