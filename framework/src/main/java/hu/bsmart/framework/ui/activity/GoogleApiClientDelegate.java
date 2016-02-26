package hu.bsmart.framework.ui.activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.model.LatLng;

import android.location.Location;

/**
 * Delegate interface for handling operations related to Google API Clients.
 */
interface GoogleApiClientDelegate extends ConnectionCallbacks, OnConnectionFailedListener {

	/**
	 * Connects the Google API client.
	 */
	void connect();

	/**
	 * Disconnects the Google API client.
	 */
	void disconnect();

	/**
	 * Returns the {@link GoogleApiClient} behind the delegate.
	 *
	 * @return the client
	 */
	GoogleApiClient getClient();

	/**
	 * Starts location tracking.
	 */
	void startTrackingLocation();

	/**
	 * Stops location tracking.
	 */
	void stopTrackingLocation();

	/**
	 * Starts geofencing.
	 */
	void startGeofence(LatLng targetLocation, float circularDistance);

	/**
	 * Stops geofencing.
	 */
	void stopGeofence();

	/**
	 * Returns the last known location.
	 */
	Location getLastLocation();
}
