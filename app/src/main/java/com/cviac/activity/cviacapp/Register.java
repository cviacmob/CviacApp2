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

public class Register extends Activity {
EditText e1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final String verifycode = "123";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		e1=(EditText)findViewById(R.id.editTextphnum);
e1.setRawInputType(Configuration.KEYBOARD_12KEY);

		Button b=(Button)findViewById(R.id.buttonext);



			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
						if (e1.getText().toString().equals(verifycode)) {
					Intent i = new Intent(Register.this, Verification.class);
					startActivity(i);
							finish();
				} else {
					e1.setError("Invalid phone number");
				}
				}
			});
		
		
			}
}
