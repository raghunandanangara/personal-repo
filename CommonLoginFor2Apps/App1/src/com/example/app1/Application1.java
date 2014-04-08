package com.example.app1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Application1 extends Activity{
	
	SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        
        int value = mSharedPreferences.getInt(LoginActivity.PREFERENCE_KEY, 0);
        if(value==0)
        {
        	//Show the login activity as this is the first time
        	Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        	startActivity(i);
        }
        else
        {
        	Intent i = new Intent(getApplicationContext(),App1.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(i);
        }
        finish();
	}
}
