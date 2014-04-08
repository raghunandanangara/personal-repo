package com.example.app2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.Toast;

public class Application2 extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Context c = null;
		try {
			c = createPackageContext("com.example.app1", Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Please Intall App1 before starting this App2", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		SharedPreferences sp = c.getSharedPreferences("login", MODE_MULTI_PROCESS);
        int value = sp.getInt("isLoggedIn", 0);
        
        if(value==0)
        {
        	//Show the login activity as this is the first time
        	Intent startLoginActivity = new Intent("android.intent.action.MAIN");                
        	startLoginActivity.setComponent(new ComponentName("com.example.app1","com.example.app1.LoginActivity"));
        	startActivity(startLoginActivity);
        }
        else
        {
        	Intent i = new Intent(getApplicationContext(),App2.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(i);
        }
        finish();
	}
}
