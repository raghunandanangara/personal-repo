package com.raghu.example.fusedlocationprovider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;

public class FusedLocationProviderActivity extends Activity
							implements GooglePlayServicesClient.ConnectionCallbacks, 
							           GooglePlayServicesClient.OnConnectionFailedListener,
							           LocationListener{
	
	private final static int GOOGLE_PLAY_NOT_AVAILABLE = 8092;
	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
	
	EditText lat, log, city;
	ProgressBar pg1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused_location_provider);
        
        lat = (EditText)findViewById(R.id.Latitude);
        log = (EditText)findViewById(R.id.Longitude);
        city = (EditText)findViewById(R.id.City);
        
        pg1 = (ProgressBar)findViewById(R.id.progressBar1);
        
        int connectionResult=GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if(connectionResult==ConnectionResult.SUCCESS)
		{
			FusedLocationProviderApplication.setGooglePlayAvailable(true);
    		Log.i("Raghu","ConnectionResult.SUCCESS");
		}
		else
		{
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionResult, this, GOOGLE_PLAY_NOT_AVAILABLE);
			Log.i("Raghu","ConnectionResult.FAILURE");
			if(dialog!=null)
				dialog.show();
		}
		
		if(FusedLocationProviderApplication.isGooglePlayAvailable()==true)
		{
			mLocationClient = new LocationClient(this, this, this);
			mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).setInterval(20000).setFastestInterval(5000);
		}
    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mLocationClient.connect();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mLocationClient.removeLocationUpdates(this);
		mLocationClient.disconnect();
		super.onStop();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		Log.i("Raghu","resultCode="+resultCode+" requestCode="+requestCode+" date="+data);
		
		switch(requestCode)
		{
			case GOOGLE_PLAY_NOT_AVAILABLE:
				if(resultCode==Activity.RESULT_OK) // This may not be true
				{
					Log.i("Raghu","onActivityResult RESULT_OK");
				}
				else if(resultCode==Activity.RESULT_CANCELED) //When user presses back button after installing/canceling of Play Services
				{
					Log.i("Raghu","onActivityResult RESULT_CANCELED");
					//Do Nothing here as you want to stay in this Main Actiivity
				}
				//else
					//finish();
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        GOOGLE_PLAY_NOT_AVAILABLE);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        	GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),this, GOOGLE_PLAY_NOT_AVAILABLE).show();
        }

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Connected to Location Client Services", Toast.LENGTH_SHORT).show();
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
		mCurrentLocation = mLocationClient.getLastLocation();
		Log.i("Raghu", "Current Location ="+mCurrentLocation);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disconnected to Location Client Services", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mCurrentLocation = location;
		getAddress();
		String lat = Double.toString(location.getLatitude());
		String log = Double.toString(location.getLongitude());
		String msg = "Updated Location: " + lat + "," + log;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        playSound();
        this.lat.setText(lat);
        this.log.setText(log);
	}
	
	public void playSound()
	{
		try {
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		    r.play();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
     * The "Get Address" button in the UI is defined with
     * android:onClick="getAddress". The method is invoked whenever the
     * user clicks the button.
     *
     * @param v The view object associated with this method,
     * in this case a Button.
     */
    public void getAddress() {
        // Ensure that a Geocoder services is available
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.GINGERBREAD
                            &&
                Geocoder.isPresent()) {
            // Show the activity indicator
            pg1.setVisibility(View.VISIBLE);
            /*
             * Reverse geocoding is long-running and synchronous.
             * Run it on a background thread.
             * Pass the current location to the background task.
             * When the task finishes,
             * onPostExecute() displays the address.
             */
            (new GetAddressTask(this)).execute(mCurrentLocation);
        }
    }

	
    /**
    * A subclass of AsyncTask that calls getFromLocation() in the
    * background. The class definition has these generic types:
    * Location - A Location object containing
    * the current location.
    * Void     - indicates that progress units are not used
    * String   - An address passed to onPostExecute()
    */
    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        
    	Context mContext;
        
        public GetAddressTask(Context context)
        {
            super();
            mContext = context;
        }
        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @params params One or more Location objects
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try 
            {
                /*
                 * Return 1 address.
                 */
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } 
            catch (IOException e1) 
            {
	            Log.e("LocationSampleActivity",
	                    "IO Exception in getFromLocation()");
	            e1.printStackTrace();
	            return ("IO Exception trying to get address");
            } 
            catch (IllegalArgumentException e2)
            {
	            // Error message to post in the log
	            String errorString = "Illegal arguments " +
	                    Double.toString(loc.getLatitude()) +
	                    " , " +
	                    Double.toString(loc.getLongitude()) +
	                    " passed to address service";
	            Log.e("LocationSampleActivity", errorString);
	            e2.printStackTrace();
	            return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                // Return the text
                return addressText;
            } 
            else 
            {
                return "No address found";
            }
        }
        
        /**
         * A method that's called once doInBackground() completes. Turn
         * off the indeterminate activity indicator and set
         * the text of the UI element that shows the address. If the
         * lookup failed, display the error message.
         */
        @Override
        protected void onPostExecute(String address) {
            // Set activity indicator visibility to "gone"
            pg1.setVisibility(View.GONE);
            // Display the results of the lookup.
            city.setText(address);
        }
    }
}
