package hu.bsmart.framework.ui.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.geofence.GeofenceTransitionsIntentService;
import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;

/**
 * Default implementation of {@link GoogleApiClientDelegate}.
 */
public class DefaultGoogleApiClientDelegate implements GoogleApiClientDelegate {
	private FrameworkActivity activity;
	private GoogleApiClient client;

	private boolean locationTrackingEnabled = false;

	private boolean geofenceEnabled = false;
	private LatLng geofenceTarget = null;
	private float geofenceRadius = 0.0f;

	private Logger logger;

	public DefaultGoogleApiClientDelegate(FrameworkActivity activity) {
		this.activity = activity;
		logger = LoggerProvider.createLoggerForClass(getClass());
		client = buildGoogleApiClient();
	}

	private GoogleApiClient buildGoogleApiClient() {
		return new GoogleApiClient.Builder(activity).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	public void connect() {
		client.connect();
	}

	@Override
	public void disconnect() {
		client.disconnect();
	}

	@Override
	public void onConnected(Bundle bundle) {
		synchronized (this) {
			if (locationTrackingEnabled) {
				startTrackingLocationInternal();
			} else {
				stopTrackingLocationInternal();
			}
			if (geofenceEnabled) {
				startGeofenceInternal();
			} else {
				stopGeofenceInternal();
			}
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	}

	@Override
	public GoogleApiClient getClient() {
		return client;
	}

	@Override
	public void startTrackingLocation() {
		synchronized (this) {
			locationTrackingEnabled = true;
			if (client.isConnected()) {
				startTrackingLocationInternal();
			}
		}
	}

	private synchronized void startTrackingLocationInternal() {
		Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
		if (lastLocation != null) {
			activity.onLocationChanged(lastLocation);
		}

		LocationServices.FusedLocationApi.requestLocationUpdates(client, createLocationRequest(), activity);
	}

	private LocationRequest createLocationRequest() {
		LocationRequest result = new LocationRequest();
		result.setInterval(LocationConstants.MAX_TIME_BW_UPDATES_MS);
		result.setFastestInterval(LocationConstants.MIN_TIME_BW_UPDATES_MS);
		result.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		return result;
	}

	@Override
	public void stopTrackingLocation() {
		synchronized (this) {
			locationTrackingEnabled = false;
			if (client.isConnected()) {
				stopTrackingLocationInternal();
			}
		}
	}

	private synchronized void stopTrackingLocationInternal() {
		LocationServices.FusedLocationApi.removeLocationUpdates(client, activity);
	}

	@Override
	public void startGeofence(LatLng targetLocation, float circularDistance) {
		synchronized (this) {
			geofenceEnabled = true;
			geofenceTarget = targetLocation;
			geofenceRadius = circularDistance;
			if (client.isConnected()) {
				startGeofenceInternal();
			}
		}
	}

	private synchronized void startGeofenceInternal() {
		if (geofenceTarget == null) {
			return;
		}

		if (!isGeofenceServiceRegisteredInManifest()) {
			logger.w(GeofenceTransitionsIntentService.class.getName() + " is not registered in the manifest!");
			return;
		}

		Geofence.Builder geofenceBuilder = new Geofence.Builder();
		geofenceBuilder.setRequestId(LocationConstants.GEOFENCE_REQUEST_ID);
		geofenceBuilder.setCircularRegion(geofenceTarget.latitude, geofenceTarget.longitude, geofenceRadius);
		geofenceBuilder.setExpirationDuration(Geofence.NEVER_EXPIRE);
		geofenceBuilder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);

		Geofence geofence = geofenceBuilder.build();

		GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
		geofencingRequestBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
		geofencingRequestBuilder.addGeofence(geofence);

		GeofencingRequest geofencingRequest = geofencingRequestBuilder.build();

		Intent intent = new Intent(activity.getApplicationContext(), GeofenceTransitionsIntentService.class);
		PendingIntent pendingIntent = PendingIntent
				.getService(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		LocationServices.GeofencingApi.addGeofences(client, geofencingRequest, pendingIntent);
	}

	private boolean isGeofenceServiceRegisteredInManifest() {
		ComponentName geofenceServiceName =
				new ComponentName(activity.getApplicationContext(), GeofenceTransitionsIntentService.class);
		try {
			ServiceInfo serviceInfo = activity.getPackageManager().getServiceInfo(geofenceServiceName, 0);
			return serviceInfo != null;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	@Override
	public void stopGeofence() {
		synchronized (this) {
			geofenceEnabled = false;
			if (client.isConnected()) {
				stopGeofenceInternal();
			}
		}
	}

	private void stopGeofenceInternal() {
		List<String> geofencingRequests = new ArrayList<>();
		geofencingRequests.add(LocationConstants.GEOFENCE_REQUEST_ID);
		LocationServices.GeofencingApi.removeGeofences(client, geofencingRequests);
	}

	@Override
	public Location getLastLocation() {
		if (client.isConnected()) {
			return LocationServices.FusedLocationApi.getLastLocation(client);
		} else {
			return null;
		}
	}
}
