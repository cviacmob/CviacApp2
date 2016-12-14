package com.cviac.activity.cviacapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.cviac.cviacappapi.cviacapp.CVIACApi;
import com.cviac.cviacappapi.cviacapp.RegInfo;
import com.cviac.cviacappapi.cviacapp.RegisterResponse;
import com.cviac.cviacappapi.cviacapp.VerifyResponse;
import com.cviac.datamodel.cviacapp.Employee;
import com.cviac.datamodel.cviacapp.EmployeeInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.R.id.input;


public class Verification extends Activity {
    EditText e1;
    String verifycode;
    Button buttonverify, buttonresend;
    String mobile;
    List<EmployeeInfo> emplist;

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
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://apps.cviac.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CVIACApi api = retrofit.create(CVIACApi.class);
                RegInfo regInfo = new RegInfo();
                regInfo.setMobile(mobile);
                regInfo.setOtp(verifycode);
                final Call<VerifyResponse> call = api.verifyPin(regInfo);
                call.enqueue(new Callback<VerifyResponse>() {
                    @Override
                    public void onResponse(Response<VerifyResponse> response, Retrofit retrofit) {
                        VerifyResponse otp = response.body();
                        int code = otp.getCode();
                        if (code == 0) {
                           getEmployees();
                        }
                        else {
                            Toast.makeText(Verification.this, "Invalid PIN" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(Verification.this, "API Invoke Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });


                // TODO Auto-generated method stub

            }
        });
    }

    private void getEmployees() {
        Retrofit ret = new Retrofit.Builder()
                .baseUrl("http://apps.cviac.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CVIACApi api = ret.create(CVIACApi.class);
        final Call<List<EmployeeInfo>> call = api.getEmployees();
        call.enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                emplist = response.body();
                saveEmployeeInfo(emplist);
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
            emp.save();
        }
    }
}
