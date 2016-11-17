package com.cviac.activity.cviacapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Register extends Activity {
EditText e;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final String verifycode = "123";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		WebView view = (WebView) findViewById(R.id.textContent);
		Button b=(Button)findViewById(R.id.buttonext);
		e=(EditText)findViewById(R.id.editTextphnum);
		String text;
		text = "<html><body><p align=\"justify\">";
		text+="<h3>";
		text+="<center>Verify phone number</center>";
		text+="</h3>";
		text+="<br>";
		text+="<h8>";
		text+= "<center>CviacApp Messanger will send an SMS message to verify your phone number. Enter your phone number</center>";
		text+="</h8>";
		text+= "</p></body></html>";
		view.loadData(text, "text/html", "utf-8");
		
		
			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
						if (e.getText().toString().equals(verifycode)) {
					Intent i = new Intent(Register.this, Verification.class);
					startActivity(i);
							finish();
				} else {
					e.setError("Invalid phone number");
				}
				}
			});
		
		
			}
}
