package com.raghu.example.activityrecognitionexample;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;

public class ActivityRecognitionExample extends Activity implements ConnectionCallbacks, OnConnectionFailedListener{
	
	private final static int GOOGLE_PLAY_NOT_AVAILABLE = 8092;
	private final static String TAG = "ActivityRecognitionExample";
	
	private PendingIntent mActivityRecognitionPendingIntent;
	private ActivityRecognitionClient mActivityRecognitionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int connectionResult=GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(connectionResult==ConnectionResult.SUCCESS)
        {
             Log.i(TAG,"ConnectionResult.SUCCESS");
        }
        else
        {
	        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionResult, this, GOOGLE_PLAY_NOT_AVAILABLE);
	        Log.i("Raghu","ConnectionResult.FAILURE");
	        if(dialog!=null)
	        	dialog.show();
        }
        mActivityRecognitionClient = new ActivityRecognitionClient(this, this, this);
        Intent intent = new Intent(this, ActivityRecognitionIntentService.class);
        mActivityRecognitionPendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        mActivityRecognitionClient.connect();
        
        setContentView(R.layout.activity_activity_recognition_example);
    }

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mActivityRecognitionClient.requestActivityUpdates(10 * 1000, //10 seconds
														mActivityRecognitionPendingIntent);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		//mActivityRecognitionClient = null;
		mActivityRecognitionClient.removeActivityUpdates(
                mActivityRecognitionPendingIntent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		if(mActivityRecognitionClient!=null)
//			mActivityRecognitionClient.disconnect();
//		if(mActivityRecognitionClient!=null)
//		{
//		mActivityRecognitionClient.removeActivityUpdates(
//                mActivityRecognitionPendingIntent);
//		}
		mActivityRecognitionClient = null;
		mActivityRecognitionPendingIntent.cancel();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mActivityRecognitionClient.disconnect();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
    
	
}
