package hu.bsmart.framework.geofence;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;

/**
 * IntentService that will be notified about Geofence events.
 * <p/>
 * If you would like to use geofence, register this service in your application's manifest file.
 * <p/>
 */
public class GeofenceTransitionsIntentService extends IntentService {

	private static final String TAG = "GeofenceService";

	private Logger logger;

	/**
	 * This constructor is required, and calls the super IntentService(String)
	 * constructor with the name for a worker thread.
	 */
	public GeofenceTransitionsIntentService() {
		// Use the TAG to name the worker thread.
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		logger = LoggerProvider.createLoggerWithTag(TAG);
	}

	/**
	 * Handles incoming intents.
	 *
	 * @param intent sent by Location Services. This Intent is provided to Location
	 *               Services (inside a PendingIntent) when addGeofences() is called.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		if (geofencingEvent.hasError()) {
			String errorMessage = GeofenceStrings.getErrorString(this, geofencingEvent.getErrorCode());
			logger.e(errorMessage);
			return;
		}

		int transition = geofencingEvent.getGeofenceTransition();
		List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

		logger.v(GeofenceStrings.getTransitionDetails(this, transition, triggeringGeofences));

		if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
			GeofenceManager.getInstance().dispatchEnterEvent(getApplicationContext());
		} else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
			GeofenceManager.getInstance().dispatchExitEvent(getApplicationContext());
		}
	}
}
