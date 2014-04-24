package com.example.raghu.geofencingexample;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationStatusCodes;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;

public class GeofencingExampleActivity extends Activity 
										implements GooglePlayServicesClient.ConnectionCallbacks
												, GooglePlayServicesClient.OnConnectionFailedListener
												, LocationClient.OnAddGeofencesResultListener{
	
	private final static int GOOGLE_PLAY_NOT_AVAILABLE = 8092;
	private final static String TAG = "GeofencingExampleActivity";
	
	private final String GeoId="1";
	private final double GeoLat=-37.81706238;
	private final double GeoLong=144.96366882;
	private final float GeoRadius=100;
	private final long GeoExpiration=Geofence.NEVER_EXPIRE; //1*60*1000; //1 hour expiration
	private final int GeoTransitionType=Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT;
	private List<Geofence> GeoFencingList = new ArrayList<Geofence>();
	
	LocationClient mLocationClient=null;
	
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
		
        setContentView(R.layout.activity_geofencing_example);
        
//        if(mLocationClient==null)
//        {
        	mLocationClient = new LocationClient(this, this, this);
        	mLocationClient.connect();
        	
        	Geofence GeoFencing1 =  new Geofence.Builder()
        						.setRequestId(GeoId)
        						.setTransitionTypes(GeoTransitionType)
        						.setCircularRegion(GeoLat, GeoLong, GeoRadius)
        						.setExpirationDuration(GeoExpiration)
        						.build();
        	GeoFencingList.add(GeoFencing1);
//        }
    }
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != mLocationClient) {
            mLocationClient.disconnect();
        }
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
//    	mLocationClient.connect();
    	Log.i(TAG, "mLocationClient.connect");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//mLocationClient.disconnect();
		Log.i(TAG, "mLocationClient.disconnect");
	}

	/*
     * Create a PendingIntent that triggers an IntentService in your
     * app when a geofence transition occurs.
     */
    private PendingIntent getGeofenceTransitionPendingIntent() {
    	Log.i(TAG, "getGeofenceTransitionPendingIntent");
        // Create an explicit Intent
        Intent intent = new Intent(this, ReceiveGeofenceTransitionsIntentService.class);
        /*
         * Return the PendingIntent
         */
        return PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onConnected addGeofences");
		mLocationClient.addGeofences(GeoFencingList, getGeofenceTransitionPendingIntent(), this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Log.i(TAG, "mLocationClient = null");
		//mLocationClient.disconnect();
		mLocationClient = null;
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// TODO Auto-generated method stub
		// If adding the geofences was successful
        if (LocationStatusCodes.SUCCESS == statusCode) 
        {
            /*
             * Handle successful addition of geofences here.
             * You can send out a broadcast intent or update the UI.
             * geofences into the Intent's extended data.
             */
        	Log.i(TAG, "onAddGeofencesResult LocationStatusCodes.SUCCESS");
        } 
        else 
        {
        // If adding the geofences failed
            /*
             * Report errors here.
             * You can log the error using Log.e() or update
             * the UI.
             */
        	Log.i(TAG, "onAddGeofencesResult LocationStatusCodes.ERROR = "+statusCode);
        }
        // Turn off the in progress flag and disconnect the client
        //mLocationClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.e(TAG, result.getErrorCode() + "");
	}

    
}
