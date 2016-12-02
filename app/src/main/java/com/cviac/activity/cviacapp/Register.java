package com.cviac.activity.cviacapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cviac.cviacappapi.cviacapp.CVIACApi;
import com.cviac.datamodel.cviacapp.Employee;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class Register extends Activity {
    EditText e1;
String regmobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String verifycode = "123";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        e1 = (EditText) findViewById(R.id.editTextphnum);
        e1.setRawInputType(Configuration.KEYBOARD_12KEY);

        Button b = (Button) findViewById(R.id.buttonext);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CVIACApi api = retrofit.create(CVIACApi.class);

        final Call<Employee> call = api.getemployeeByMobile(regmobile);
        call.enqueue(new Callback<Employee>() {


            @Override
            public void onResponse(Response<Employee> response, Retrofit retrofit) {
                Employee esp=response.body();
               // esp.save();

            }

            @Override
            public void onFailure(Throwable throwable) {
             //   emps = null;
            }
        });
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (e1.getText().toString().length() >= 10 && e1.getText().toString().length() <= 13) {
                    Intent i = new Intent(Register.this, Verification.class);
                    i.putExtra("mobile", e1.getText().toString());
                    startActivity(i);
                    finish();
                } else {
                    e1.setError("Invalid phone number");
                }
            }
        });
    }
 /*   private CVIACApi getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final CVIACApi mInterfaceService = retrofit.create(CVIACApi.class);
        return mInterfaceService;
    }*/



}
