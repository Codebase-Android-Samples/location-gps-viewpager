package com.codebase.locationgpstest.utils;

/*Gps tracker Algorithm

 1. Initialize
 2. Scanning
 a 	clear all handler(stop all timers) and remove updates(stop updates)
 b	check gps or wifi to use for location updates
 c	set one provider and scan and start z timer(for step d)
 d  if  a "z" amount of time reaches switch provider goto "a" and "c" not b

 e	if scan found 

 	cancel all timers
 	wait for another update
 	start timer x(for step f)
 	
 	
 	
 
 f. if x amount of times reaches redo from a

 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;

import com.codebase.locationgpstest.app.LocationApplication;
import com.codebase.locationgpstest.utils.core.ResourceChecker;

public class LocationUpdater extends Service implements LocationListener {

	public static LocationUpdater locationUpdaterSingleInstance = null;

	private Context appContext;

	private String currentLocationUpdateProvider;
	private long DELAY_IN_NEXT_SCAN = 1000 * 60 * 1;
	private long LOCATION_UPDATE_FREQUENCY = 0;

	private float LOCATION_UPDATE_MIN_DISTANCE = 0;
	private LocationUpdateListner locationListener;
	private LocationManager locationManager;

	private Handler locationUpdateHandler;

	private LocationUpdater(Context context) {
		// TODO Auto-generated constructor stub
		this.appContext = context;
		this.locationManager = (LocationManager) appContext
				.getSystemService(LOCATION_SERVICE);
		this.locationUpdateHandler = new Handler();
		LocationApplication.LogError("locationUpdateHandler Constructor");

		setUpScanning(false);
	}

	private String getAvailableLocationProvider() {

		currentLocationUpdateProvider = LocationManager.NETWORK_PROVIDER;
		if (this.isGPSLocationUpdatesPossible()) {
			currentLocationUpdateProvider = LocationManager.GPS_PROVIDER;
		} else if (this.isNetworkLocationUpdatesPossible()) {
			currentLocationUpdateProvider = LocationManager.NETWORK_PROVIDER;
		}
		return currentLocationUpdateProvider;
	}

	private Location getBestLocationOnUpdate(Location aLocation) {
		Location locationAvailable = getLocationFromAvailableProvider();

		if (locationAvailable == null)
			return aLocation;
		if (aLocation.getAccuracy() >= locationAvailable.getAccuracy())
			return aLocation;
		return locationAvailable;

	}

	private Location getLocationFromAvailableProvider() {
		Location lastLocation = new Location(LocationManager.NETWORK_PROVIDER);
		Location gpsLocation = null, networkLocation = null;

		gpsLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		networkLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (gpsLocation != null && networkLocation != null) {
			lastLocation = (gpsLocation.getAccuracy() > networkLocation
					.getAccuracy()) ? gpsLocation : networkLocation;
		} else {
			lastLocation = (gpsLocation == null) ? ((networkLocation == null) ? lastLocation
					: networkLocation)
					: gpsLocation;
		}

		return lastLocation;
	}

	private String getSwitchedProvider() {
		LocationApplication.LogError("Current location provide is "
				+ currentLocationUpdateProvider);

		if (currentLocationUpdateProvider == null)
			return getAvailableLocationProvider();
		if (currentLocationUpdateProvider
				.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
			currentLocationUpdateProvider = LocationManager.NETWORK_PROVIDER;
		} else {
			currentLocationUpdateProvider = LocationManager.GPS_PROVIDER;
		}
		return currentLocationUpdateProvider;
	}

	private boolean isGPSLocationUpdatesPossible() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	private boolean isNetworkLocationUpdatesPossible() {

		// getting network status
		return locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		LocationApplication.LogError("On Location Found");
		locationListener.onLocationUpdate(getBestLocationOnUpdate(location));
		setDelayForNextScan();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

		LocationApplication.LogError("onProviderDisabled povider is "
				+ provider);

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

		LocationApplication
				.LogError("onProviderEnabled povider is " + provider);

	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

		LocationApplication.LogError("onStatusChanged povider is " + provider);

	}
	
	private void setDelayForNextScan() {

		stopLocationScanning();

		locationUpdateHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LocationApplication.LogError("Time set for next scan ended");
				setUpScanning(false);

			}
		}, DELAY_IN_NEXT_SCAN);
	}

	private void setUpScanning(boolean forceToAutoSwitchProviderAfterTimeout) {

		stopLocationScanning();
		LocationApplication.LogError("On setUpScanning()");
		String provider = (forceToAutoSwitchProviderAfterTimeout) ? getSwitchedProvider()
				: getAvailableLocationProvider();
		LocationApplication.LogError("Provider is " + provider);
		if (provider != null) {
			locationManager.requestLocationUpdates(provider,
					LOCATION_UPDATE_FREQUENCY, LOCATION_UPDATE_MIN_DISTANCE,
					LocationUpdater.this);
		}

		locationUpdateHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// LocationApplication.LogError(");
				LocationApplication
						.LogError("Time set for force scan ended so switching provider");
				locationListener
						.onLocationUpdate(getLocationFromAvailableProvider());
				LocationUpdater.this.setUpScanning(true);

			}
		}, DELAY_IN_NEXT_SCAN);

	}

	private void stopLocationScanning() {
		locationUpdateHandler.removeCallbacksAndMessages(null);
		locationManager.removeUpdates(this);

	}

	public static void checkForGpsWithAlert(final Activity activity,
			String message, String goToSettingsButtonTitle,
			String cancelButtonTitle) {
		String aMessage = (message == null) ? "GPS required." : message;
		String aGoToSettingsButtonTitle = (goToSettingsButtonTitle == null) ? "Enable GPS"
				: goToSettingsButtonTitle;
		String aCancelButtonTitle = (cancelButtonTitle == null) ? "Cancel"
				: cancelButtonTitle;

		if (!LocationUpdater.isGPSActivated(activity)) {
			new AlertDialog.Builder(activity)
					.setMessage(aMessage)
					.setCancelable(false)
					.setPositiveButton(aGoToSettingsButtonTitle,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									LocationUpdater.openGPSSettings(activity);
									LocationUpdater.toastGPSStatus(activity);
								}
							})
					.setNegativeButton(aCancelButtonTitle,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

								}
							}).create().show();
		}
	}

	public static LocationUpdater getSharedLocationUpdater(Context context) {
		if (locationUpdaterSingleInstance == null) {
			locationUpdaterSingleInstance = new LocationUpdater(context);
		}

		return locationUpdaterSingleInstance;
	}

	public static void getUpdate(Context context,
			LocationUpdateListner aLocationListener) {
		LocationUpdater locationUpdater = LocationUpdater
				.getSharedLocationUpdater(context);
		locationUpdater.locationListener = aLocationListener;
	}

	public static boolean isGPSActivated(Context context) {

		return ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE))
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static void openGPSSettings(Activity activity) {
		activity.startActivity(new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}

	public static void startScanning(Context context,boolean switchProvider)
	{
		getSharedLocationUpdater(context).setUpScanning(switchProvider);
	}

	public static void stopScanning(Context context)
	{
		getSharedLocationUpdater(context).stopLocationScanning();
	}

	private static void toastGPSStatus(Context context) {
		LocationApplication.makeToast("Gps is "
				+ ResourceChecker.isGPSActivated(context));
	}
}
