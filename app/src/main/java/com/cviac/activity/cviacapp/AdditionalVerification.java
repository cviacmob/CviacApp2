package com.cviac.activity.cviacapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.cviac.cviacappapi.cviacapp.AdditinalRegisterResponse;
import com.cviac.cviacappapi.cviacapp.AdditionalRegInfo;
import com.cviac.cviacappapi.cviacapp.CVIACApi;
import com.cviac.cviacappapi.cviacapp.RegInfo;
import com.cviac.cviacappapi.cviacapp.RegisterResponse;
import com.squareup.okhttp.OkHttpClient;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AdditionalVerification extends AppCompatActivity implements View.OnClickListener {
    EditText etempcode,etmobile;
    EditText etdob;
    ImageView imdate;
    Button Sendbutton;
    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);
    private int mYear, mMonth, mDay;
    String regmobile,regempcode;
    Date regdate;
    ProgressDialog progressDialog;
    AdditionalRegInfo regInfor;
    CVIACApi api;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_verification);
        etmobile =(EditText)findViewById(R.id.input_Mobilenumber);
        etempcode =(EditText)findViewById(R.id.input_empnumber);
        etdob=(EditText)findViewById(R.id.editTextdob);
        imdate =(ImageView)findViewById(R.id.imageViewdate);
        Sendbutton=(Button)findViewById(R.id.bsend);
        imdate.setOnClickListener(this);

        Sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CVIACApplication app = (CVIACApplication) AdditionalVerification.this.getApplication();

                regmobile = etmobile.getText().toString();
                regempcode=etempcode.getText().toString();
                regdate= (Date) etdob.getText();
            /*    if (app.isNetworkStatus()) {*/
                progressDialog = new ProgressDialog(AdditionalVerification.this,R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                // progressDialog.setIndeterminateDrawable(R.drawable.custom_progress_dialog);
                //android:indeterminateDrawable="@drawable/custom_progress_dialog"
                progressDialog.setMessage("Registering...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
                okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://apps.cviac.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
                api = retrofit.create(CVIACApi.class);
                regInfor = new AdditionalRegInfo();
                regInfor.setMobile(regmobile);
                regInfor.setEmpcode(regempcode);
                regInfor.setDate(regdate);
                /*} else {
                    Toast.makeText(getApplicationContext(),
                            "Please Check Your Internet Connection and try again", Toast.LENGTH_LONG).show();
                }*/
                final Call<AdditinalRegisterResponse> call = api.registeradditionalverification(regInfor);
                call.enqueue(new Callback<AdditinalRegisterResponse>() {


                    @Override
                    public void onResponse(Response<AdditinalRegisterResponse> response, Retrofit retrofit) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        AdditinalRegisterResponse rsp = response.body();
                        int code = rsp.getCode();
                        if (code== 0) {
                            if (etmobile.getText().toString().length() >= 10 && etmobile.getText().toString().length() <= 13) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Intent i = new Intent(AdditionalVerification.this, Verification.class);
                                i.putExtra("mobile", regmobile);
                                startActivity(i);
                                finish();
                            } else {
                                etmobile.setError("Invalid mobile number");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(AdditionalVerification.this,"Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }

                });
            }
        });





    }

    @Override
    public void onClick(View v) {
            if (v == imdate) {

                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox


                         etdob.setText(dayOfMonth + "-"
                        + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
                dpd.show();

            }

        }
        }


