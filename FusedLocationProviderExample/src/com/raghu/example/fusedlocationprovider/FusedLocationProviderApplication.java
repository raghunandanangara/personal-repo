package com.raghu.example.fusedlocationprovider;

import android.app.Application;

public class FusedLocationProviderApplication extends Application{
	
	static boolean isGooglePlayAvailable=false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}
	
	public static boolean isGooglePlayAvailable() {
		return isGooglePlayAvailable;
	}

	public static void setGooglePlayAvailable(boolean isGooglePlayAvailable) {
		FusedLocationProviderApplication.isGooglePlayAvailable = isGooglePlayAvailable;
	}

}
