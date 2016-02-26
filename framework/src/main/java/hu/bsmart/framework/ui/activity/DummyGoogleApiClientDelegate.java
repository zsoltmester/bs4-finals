package hu.bsmart.framework.ui.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.Bundle;

/**
 * Implementation of {@link GoogleApiClientDelegate} that does nothing.
 *
 * Since it has no real Google API client, {@link #getClient()} returns {@code null}.
 */
public class DummyGoogleApiClientDelegate implements GoogleApiClientDelegate {
	@Override
	public void connect() {
	}

	@Override
	public void disconnect() {
	}

	@Override
	public GoogleApiClient getClient() {
		return null;
	}

	@Override
	public void startTrackingLocation() {
	}

	@Override
	public void stopTrackingLocation() {
	}

	@Override
	public void onConnected(Bundle bundle) {
	}

	@Override
	public void onConnectionSuspended(int i) {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	}

	@Override
	public void startGeofence(LatLng targetLocation, float circularDistance) {
	}

	@Override
	public void stopGeofence() {
	}

	@Override
	public Location getLastLocation() {
		return null;
	}
}
