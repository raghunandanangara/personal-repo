package com.example.app1;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class LoginActivity extends Activity {
	
	SharedPreferences mSharedPreferences;
	final static String PREFERENCE_KEY="isLoggedIn";

	final int LOGGED_IN = 101;
	final int LOGGED_OUT = 102;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mSharedPreferences = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
		
		Button login = (Button) findViewById(R.id.button2);
        
        login.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSharedPreferences.edit().putInt(PREFERENCE_KEY, LOGGED_IN).commit();
	        	Intent i = new Intent(getApplicationContext(),App1.class);
	        	startActivity(i);
	        	finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	
}
