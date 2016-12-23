package com.cviac.activity.cviacapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.cviac.com.cviac.app.restapis.CVIACApi;
import com.cviac.com.cviac.app.restapis.RegInfo;
import com.cviac.com.cviac.app.restapis.RegisterResponse;
import com.cviac.com.cviac.app.restapis.VerifyResponse;
import com.cviac.com.cviac.app.datamodels.Employee;
import com.cviac.com.cviac.app.datamodels.EmployeeInfo;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class Verification extends Activity {
    EditText e1;
    String verifycode;
    Button buttonverify, buttonresend;
    String mobile;
    List<EmployeeInfo> emplist;
    ProgressDialog progressDialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        Intent i = getIntent();
        mobile = i.getStringExtra("mobile");

        e1 = (EditText) findViewById(R.id.editpin);
        e1.setRawInputType(Configuration.KEYBOARD_12KEY);
        //e1.setSelection(e1.getText().length() / 2);
        buttonverify = (Button) findViewById(R.id.bverify);
        buttonresend = (Button) findViewById(R.id.bResend);

        //final String e2=e1.getText().toString();
        buttonverify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                verifycode = e1.getText().toString();
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
                okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://apps.cviac.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
                CVIACApi api = retrofit.create(CVIACApi.class);
                RegInfo regInfo = new RegInfo();
                regInfo.setMobile(mobile);
                regInfo.setOtp(verifycode);
                setProgressDialog();
                final Call<VerifyResponse> call = api.verifyPin(regInfo);
                call.enqueue(new Callback<VerifyResponse>() {
                    @Override
                    public void onResponse(Response<VerifyResponse> response, Retrofit retrofit) {
                        VerifyResponse otp = response.body();
                        int code = otp.getCode();
                        if (code == 0) {
                            progressDialog.setMessage("Retrieving Contacts from Server...");
                            getEmployees();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Verification.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(Verification.this, "API Invoke Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });


                // TODO Auto-generated method stub

            }
        });


        buttonresend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Verification.this,R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Processing.....");
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
                CVIACApi api = retrofit.create(CVIACApi.class);
                RegInfo regInfo = new RegInfo();
                regInfo.setMobile(mobile);

                final Call<RegisterResponse> call = api.registerMobile(regInfo);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Response<RegisterResponse> response, Retrofit retrofit) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        Toast.makeText(Verification.this, "OTP re-sent to your registered mobile ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(Verification.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }

                });

            }
        });


    }

    private void setProgressDialog() {
        progressDialog = new ProgressDialog(Verification.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    private void getEmployees() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(120000, TimeUnit.MILLISECONDS);
        Retrofit ret = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        CVIACApi api = ret.create(CVIACApi.class);
        final Call<List<EmployeeInfo>> call = api.getEmployees();
        call.enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                emplist = response.body();
                progressDialog.setMessage("Saving Contacts to database...");
                saveEmployeeInfo(emplist);
                progressDialog.dismiss();
                final String MyPREFERENCES = "MyPrefs";
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("isRegistered", "true");
                editor.putString("mobile", mobile);
                editor.commit();
                Intent i = new Intent(Verification.this, HomeActivity.class);
                i.putExtra("mobile", mobile);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(Verification.this, "API Invoke Error :" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                //emps = null;
            }
        });

    }

    private void saveEmployeeInfo(List<EmployeeInfo> empInfos) {
        for (EmployeeInfo empinfo : emplist) {
            Employee emp = new Employee();
            emp.setEmp_name(empinfo.getEmp_name());
            emp.setMobile(empinfo.getMobile());
            emp.setEmail(empinfo.getEmail());
            emp.setEmp_code(empinfo.getEmp_code());
            emp.setDepartment(empinfo.getDepartment());
            emp.setDesignation(empinfo.getDesignation());
            emp.setDob(empinfo.getDob());
            emp.setGender(empinfo.getGender());
            emp.setManager(empinfo.getManager());
            emp.setImage_url(empinfo.getImage_url());
            emp.setPush_id(empinfo.getPush_id());
            emp.setDoj(empinfo.getDoj());
            emp.save();
        }
    }
}
