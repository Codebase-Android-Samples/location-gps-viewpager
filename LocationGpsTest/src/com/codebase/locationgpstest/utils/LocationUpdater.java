package com.codebase.locationgpstest.utils;

/*Gps tracker Algorithm

1. Initialize
2. Scanning
	a 	clear all handler and remove updates(stop updates)
	b	check gps or wifi to use for location updates
	c	set one provider and scan
	d   after a "z" amount of time check the redo "a","b"

	e	if scan found 
			
			 cancel step "d"
			 goto to step "a"
			*/



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

public class LocationUpdater extends Service implements LocationListener {

	private LocationManager locationManager;
	
	private Handler locationUpdateHandler;
	
	public long LOCATION_UPDATE_FREQUENCY = 0;
	public float LOCATION_UPDATE_MIN_DISTANCE = 0;

	public static LocationUpdater locationUpdaterSingleInstance = null;
	private Context appContext;

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}
	private Location getBestKnownLocation()
	{
		Location lastLocation = null;
		if (isGPSLocationUpdatesPossible())
		    lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (isNetworkLocationUpdatesPossible())
		    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		return lastLocation;
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private LocationUpdater(Context context) {
		// TODO Auto-generated constructor stub
		this.appContext = context;
		this.locationManager = (LocationManager) appContext
				.getSystemService(LOCATION_SERVICE);
		this.locationUpdateHandler = new Handler();
		
		setUpScanning();
	}

	private void setUpScanning()
	{
		
		stopLocationScanning();
		
		String provider = getAvailableLocationProvider();
		if(provider!=null)
		{
			locationManager.requestLocationUpdates(provider, LOCATION_UPDATE_FREQUENCY, LOCATION_UPDATE_MIN_DISTANCE, this);
		}
		
		locationUpdateHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LocationUpdater.this.setUpScanning();
				
			}
		}, LOCATION_UPDATE_FREQUENCY);
		
	}
	private void stopLocationScanning()
	{
		locationUpdateHandler.removeCallbacksAndMessages(null);
		locationManager.removeUpdates(this);
		
	}
	
	private String getAvailableLocationProvider()
	{
		if(this.isGPSLocationUpdatesPossible())
		{
			return LocationManager.GPS_PROVIDER;
		}
		else if(this.isNetworkLocationUpdatesPossible())
		{
			return LocationManager.NETWORK_PROVIDER;
		}
		return null;
	}

	public static LocationUpdater initializeSharedLocationUpdater(
			Context context) {
		if (locationUpdaterSingleInstance == null) {
			locationUpdaterSingleInstance = new LocationUpdater(context);
		}

		return locationUpdaterSingleInstance;
	}

	private boolean isNetworkLocationUpdatesPossible() {

		// getting network status
		return locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	private boolean isGPSLocationUpdatesPossible() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

}
