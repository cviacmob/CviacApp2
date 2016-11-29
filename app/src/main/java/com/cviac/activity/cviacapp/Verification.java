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

import com.cviac.datamodel.cviacapp.Employee;

import static android.R.id.input;


public class Verification extends Activity {
    EditText e1;
    String verifycode = "123456";
    Button buttonverify, buttonresend;
    String mobile;

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
                // TODO Auto-generated method stub
                if (e1.getText().toString().equals(verifycode)) {
                    final String MyPREFERENCES = "MyPrefs";
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("isRegistered", "true");
                    editor.putString("mobile",mobile);
                    editor.commit();
                    Intent i = new Intent(Verification.this, HomeActivity.class);
                    i.putExtra("mobile",mobile);
                    startActivity(i);
                    finish();
                } else {
                    e1.setError("Invalid code");
                }
            }
        });
    }
}
