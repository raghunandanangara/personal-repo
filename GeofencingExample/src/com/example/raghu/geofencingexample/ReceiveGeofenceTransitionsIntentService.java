package com.example.raghu.geofencingexample;

import java.util.List;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ReceiveGeofenceTransitionsIntentService extends IntentService{
	
	static private final String TAG = "ReceiveGeofenceTransitionsIntentService";
	/**
     * Sets an identifier for the service
     */
    public ReceiveGeofenceTransitionsIntentService() {
        super("ReceiveGeofenceTransitionsIntentService");
        Log.i(TAG,"Constructor");
    }
    /**
     * Handles incoming intents
     *@param intent The Intent sent by Location Services. This
     * Intent is provided
     * to Location Services (inside a PendingIntent) when you call
     * addGeofences()
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // First check for errors
    	Log.i(TAG,"onHandleIntent");
        if (LocationClient.hasError(intent)) 
        {
            // Get the error code with a static method
            int errorCode = LocationClient.getErrorCode(intent);
            // Log the error
            Log.e("ReceiveGeofenceTransitionsIntentService",
                    "Location Services error: " +
                    Integer.toString(errorCode));
            /*
             * You can also send the error code to an Activity or
             * Fragment with a broadcast Intent
             */
        /*
         * If there's no error, get the transition type and the IDs
         * of the geofence or geofences that triggered the transition
         */
        }
        else
        {
            // Get the type of transition (entry or exit)
            int transitionType =
                    LocationClient.getGeofenceTransition(intent);
            // Test that a valid transition was reported
            if (
                (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER)
                 ||
                (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
               ) 
            {
                List <Geofence> triggerList =
                        LocationClient.getTriggeringGeofences(intent);

                String[] triggerIds = new String[triggerList.size()];

                for (int i = 0; i < triggerIds.length; i++) 
                {
                    // Store the Id of each geofence
                    triggerIds[i] = triggerList.get(i).getRequestId();
                    Log.i(TAG, "triggerId="+triggerIds[i]);
                }
                
                Log.i(TAG, "transitionType="+transitionType);
                
                for (Geofence geofence : triggerList) {
                    generateNotification(geofence.getRequestId(), "address you defined");
                }
                
                //Toast.makeText(getApplicationContext(), "Transition Type = "+transitionType, Toast.LENGTH_SHORT).show();
                /*
                 * At this point, you can store the IDs for further use
                 * display them, or display the details associated with
                 * them.
                 */
            }
	        // An invalid transition was reported
	        else
	        {
	            Log.e("ReceiveGeofenceTransitionsIntentService",
	                    "Geofence transition error: " +
	                    Integer.toString(transitionType));
	        }
        }
    }
    
    private void generateNotification(String locationId, String address) {
        long when = System.currentTimeMillis();
        Intent notifyIntent = new Intent(this, GeofencingExampleActivity.class);
        notifyIntent.putExtra("id", locationId);
        notifyIntent.putExtra("address", address);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(locationId)
                        .setContentText(address)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setWhen(when);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());
    }
}
