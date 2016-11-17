package com.cviac.activity.cviacapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;

public class Splashscreen extends Activity {
	private final int SPLASH_DISPLAY_LENGTH = 5000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				final String MyPREFERENCES = "MyPrefs";
				SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
				String isRegistered = prefs.getString("isRegistered", "false");
				if (isRegistered.equals("false")) {

					Intent i = new Intent(Splashscreen.this, Register.class);
					startActivity(i);
					Splashscreen.this.finish();

				} else {
					Intent i = new Intent(Splashscreen.this, HomeActivity.class);
					startActivity(i);
					Splashscreen.this.finish();

				}

			}

		}, SPLASH_DISPLAY_LENGTH);
	}
	
	
}


