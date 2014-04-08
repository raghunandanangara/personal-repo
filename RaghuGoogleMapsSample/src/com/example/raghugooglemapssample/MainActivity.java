package com.example.raghugooglemapssample;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private GoogleMap gMap=null;
	static int mapTypeSelectedItem = -1;
	static int zoomControlSelectedItem = -1;
	static int compassControlSelectedItem = -1;
	static boolean[] gestureControlSelectedItem = {true,true,true,true};
	
	static String TAG = "RaghuMaps";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int connectionResult;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		connectionResult=GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if(connectionResult==ConnectionResult.SUCCESS)
		{
			gMap = initializeGoogleMap();
		}
		else
		{
			GooglePlayServicesUtil.getErrorDialog(connectionResult, this, 1);
		}
		//gMap.setMyLocationEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId())
		{
			case R.id.menu_MapType:
				showMapTypeDialog();
				break;
			case R.id.menu_ZoomControl:
				showZoomControlDialog();
				break;
			case R.id.menu_ZoomIn:
				gMap.animateCamera(CameraUpdateFactory.zoomIn());
				break;
			case R.id.menu_ZoomOut:
				gMap.animateCamera(CameraUpdateFactory.zoomOut());
				break;
			case R.id.menu_CompassControl:
				showCompassControlDialog();
				break;
			case R.id.menu_GestureControl:
				showGestureControlDialog();
				break;
			case R.id.menu_Hyderabad:
				LatLng hydLatLog = new LatLng(17.3660, 78.4760);
				gMap.animateCamera(CameraUpdateFactory.newLatLng(hydLatLog));
				break;
		}
		return true;
	}
	
	private void showZoomControlDialog()
	{
		final CharSequence[] items = {" Disable "," Enable" };
		
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Compass Control Options");
        builder.setSingleChoiceItems(items, compassControlSelectedItem, new DialogInterface.OnClickListener() {
        	
        	@Override
        	public void onClick(DialogInterface dialog, int item) {
        		if(item == 0)
        		{
        			gMap.getUiSettings().setZoomControlsEnabled(false);
        			Log.i(TAG, ""+item);
        		}
        		else
        		{
        			gMap.getUiSettings().setZoomControlsEnabled(true);
        		}
        		compassControlSelectedItem = item;
        		dialog.dismiss();   
            }
        });
        builder.show();
	}
	
	private void showMapTypeDialog()
	{
		final CharSequence[] items = {" Normal "," Hybrid "," Satellite "," Terrain "};
		
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Map Type");
        builder.setSingleChoiceItems(items, mapTypeSelectedItem, new DialogInterface.OnClickListener() {
        	
        	@Override
        	public void onClick(DialogInterface dialog, int item) {
        		gMap.setMapType(GoogleMap.MAP_TYPE_NONE+item+1);
        		mapTypeSelectedItem = item;
        		dialog.dismiss();   
            }
        });
        builder.show();
	}
	
	private void showGestureControlDialog()
	{
		//final CharSequence[] items = {" Disable "," Enable" };
		final String[] items = getResources().getStringArray(R.array.gesture_array);
		
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setTitle("Gesture Control Options");
        
        builder.setMultiChoiceItems(items, gestureControlSelectedItem, new DialogInterface.OnMultiChoiceClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int item, boolean isChecked) {
        		gestureControlSelectedItem[item]=isChecked; 
            }
        });
        
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for(int i=0;i<items.length;i++)
				{
					switch(i)
					{
						case 0:
							if(gestureControlSelectedItem[i]==true)
								gMap.getUiSettings().setZoomGesturesEnabled(true);
							else
								gMap.getUiSettings().setZoomGesturesEnabled(false);
							break;
						case 1:
							if(gestureControlSelectedItem[i]==true)
								gMap.getUiSettings().setScrollGesturesEnabled(true);
							else
								gMap.getUiSettings().setScrollGesturesEnabled(false);
							break;
						case 2:
							if(gestureControlSelectedItem[i]==true)
								gMap.getUiSettings().setTiltGesturesEnabled(true);
							else
								gMap.getUiSettings().setTiltGesturesEnabled(false);
							break;
						case 3:
							if(gestureControlSelectedItem[i]==true)
								gMap.getUiSettings().setRotateGesturesEnabled(true);
							else
								gMap.getUiSettings().setRotateGesturesEnabled(false);
							break;
					}
				}
			}
		});
        
        builder.show();
	}
	
	private void showCompassControlDialog()
	{
		final CharSequence[] items = {" Disable "," Enable" };
		
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zoom Control Options");
        builder.setSingleChoiceItems(items, zoomControlSelectedItem, new DialogInterface.OnClickListener() {
        	
        	@Override
        	public void onClick(DialogInterface dialog, int item) {
        		if(item == 0)
        		{
        			gMap.getUiSettings().setCompassEnabled(false);
        			Log.i(TAG, ""+item);
        		}
        		else
        		{
        			gMap.getUiSettings().setCompassEnabled(true);
        		}
        		zoomControlSelectedItem = item;
        		dialog.dismiss();   
            }
        });
        builder.show();
	}

	private GoogleMap initializeGoogleMap()
	{
		GoogleMap tempMap = null;
		if(gMap==null)
		{
			tempMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		else
		{
			tempMap = gMap;
		}
		return tempMap;
	}
	
}
