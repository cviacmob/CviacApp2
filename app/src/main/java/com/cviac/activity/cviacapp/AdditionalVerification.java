package com.cviac.activity.cviacapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.squareup.okhttp.OkHttpClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AdditionalVerification extends AppCompatActivity implements View.OnClickListener {
    EditText etempcode;
    EditText etdob;
    ImageView imdate;
    Button Sendbutton;
    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);
    private int mYear, mMonth, mDay;
    String regmobile, regempcode;
    String regdate;
    ProgressDialog progressDialog;
    AdditionalRegInfo regInfor;
    CVIACApi api;
    String date;
    String mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_verification);
        Intent i = getIntent();
        regmobile = i.getStringExtra("mobile");

        etempcode = (EditText) findViewById(R.id.input_empnumber);
        etdob = (EditText) findViewById(R.id.editTextdob);
        imdate = (ImageView) findViewById(R.id.imageViewdate);
        Sendbutton = (Button) findViewById(R.id.bsend);
        imdate.setOnClickListener(this);

        Sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CVIACApplication app = (CVIACApplication) AdditionalVerification.this.getApplication();

               //regmobile = etmobile.getText().toString();
                regempcode = etempcode.getText().toString();
                regdate = etdob.getText().toString();

            /*    if (app.isNetworkStatus()) {*/
                progressDialog = new ProgressDialog(AdditionalVerification.this, R.style.AppTheme_Dark_Dialog);
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
                regInfor.setEmp_code(regempcode);
                regInfor.setDob(regdate);
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
                        if (code == 0) {

                            Intent i = new Intent(AdditionalVerification.this, Verification.class);
                            i.putExtra("mobile", regmobile);
                            startActivity(i);
                            finish();


                        }else if(code==1003)
                        {
                            Toast.makeText(AdditionalVerification.this, "Employee_code not found ", Toast.LENGTH_LONG).show();
                        }
                        else if(code ==1013)
                        {
                            Toast.makeText(AdditionalVerification.this, "credentials not found ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(AdditionalVerification.this, "credentials not found ", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        finish();
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
            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfaMonth) {
                    // Display Selected date in textbox


                    etdob.setText(year + "-"
                            + (monthOfYear + 1) + "-" + dayOfaMonth);

                }
            }, mYear, mMonth, mDay);
            dpd.show();

        }

    }
}


