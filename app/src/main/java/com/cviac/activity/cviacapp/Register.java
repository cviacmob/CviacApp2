package com.cviac.activity.cviacapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.cviac.cviacappapi.cviacapp.CVIACApi;
import com.cviac.cviacappapi.cviacapp.RegInfo;
import com.cviac.cviacappapi.cviacapp.RegisterResponse;
import com.cviac.datamodel.cviacapp.Employee;
import com.google.gson.Gson;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class Register extends Activity {
    EditText e1;
    String regmobile;
    ProgressDialog progressDialog;
    RegInfo regInfo;
    CVIACApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  final String verifycode = "123";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        e1 = (EditText) findViewById(R.id.editTextphnum);

        e1.setRawInputType(Configuration.KEYBOARD_12KEY);

        Button b = (Button) findViewById(R.id.buttonext);


        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                CVIACApplication app = (CVIACApplication) Register.this.getApplication();

                regmobile = e1.getText().toString();
            /*    if (app.isNetworkStatus()) {*/
                progressDialog = new ProgressDialog(Register.this,R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
               // progressDialog.setIndeterminateDrawable(R.drawable.custom_progress_dialog);
                //android:indeterminateDrawable="@drawable/custom_progress_dialog"
                progressDialog.setMessage("Registering...");
                progressDialog.setCancelable(false);
                progressDialog.show();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://apps.cviac.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                api = retrofit.create(CVIACApi.class);
                    regInfo = new RegInfo();
                regInfo.setMobile(regmobile);
                /*} else {
                    Toast.makeText(getApplicationContext(),
                            "Please Check Your Internet Connection and try again", Toast.LENGTH_LONG).show();
                }*/
                final Call<RegisterResponse> call = api.registerMobile(regInfo);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Response<RegisterResponse> response, Retrofit retrofit) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        RegisterResponse rsp = response.body();
                        int code = rsp.getCode();
                        if (code== 0) {
                            if (e1.getText().toString().length() >= 10 && e1.getText().toString().length() <= 13) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Intent i = new Intent(Register.this, Verification.class);
                                i.putExtra("mobile", regmobile);
                                startActivity(i);
                                finish();
                            } else {
                                e1.setError("Invalid mobile number");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        t.printStackTrace();
                    }

                });
            }

        });


    }


}
