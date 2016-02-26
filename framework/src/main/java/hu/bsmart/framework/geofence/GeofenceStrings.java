package hu.bsmart.framework.geofence;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.bsmart.framework.R;

/**
 * Geofence error codes mapped to error messages.
 */
public class GeofenceStrings {
	/**
	 * Prevents instantiation.
	 */
	private GeofenceStrings() {
		throw new UnsupportedOperationException("Cannot instantiate utility class!");
	}

	private static final Map<Integer, Integer> ERROR_CODE_MAP;
	private static final Map<Integer, Integer> TRANSITION_CODE_MAP;

	static {
		ERROR_CODE_MAP = new HashMap<>();
		ERROR_CODE_MAP.put(GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, R.string.geofence_not_available);
		ERROR_CODE_MAP.put(GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES, R.string.too_many_geofences);
		ERROR_CODE_MAP.put(GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS, R.string.too_many_pending_intents);

		TRANSITION_CODE_MAP = new HashMap<>();
		TRANSITION_CODE_MAP.put(Geofence.GEOFENCE_TRANSITION_ENTER, R.string.transition_enter);
		TRANSITION_CODE_MAP.put(Geofence.GEOFENCE_TRANSITION_EXIT, R.string.transition_exit);
	}

	/**
	 * Returns the error string for a geofencing error code.
	 *
	 * @param context   Android context
	 * @param errorCode a constant from {@link GeofenceStatusCodes}
	 * @return the error message
	 */
	public static String getErrorString(Context context, int errorCode) {
		Integer stringResourceId = ERROR_CODE_MAP.get(errorCode);

		if (stringResourceId == null) {
			stringResourceId = R.string.geofence_unknown_error;
		}

		return context.getString(stringResourceId);
	}

	/**
	 * Returns the transition string for a geofencing transition code
	 *
	 * @param context        the context
	 * @param transitionCode transition code, like {@link Geofence#GEOFENCE_TRANSITION_ENTER}
	 * @return a resolved string for the transition
	 */
	public static String getTransitionString(Context context, int transitionCode) {
		Integer stringResourceId = TRANSITION_CODE_MAP.get(transitionCode);

		if (stringResourceId == null) {
			stringResourceId = R.string.transition_unknown;
		}

		return context.getString(stringResourceId);
	}

	/**
	 * Gets transition details and returns them as a formatted string.
	 *
	 * @param context             The ID of the geofence transition.
	 * @param geofenceTransition  The ID of the geofence transition.
	 * @param triggeringGeofences The geofence(s) triggered.
	 * @return The transition details formatted as String.
	 */
	public static String getTransitionDetails(Context context, int geofenceTransition,
			List<Geofence> triggeringGeofences) {
		String geofenceTransitionString = GeofenceStrings.getTransitionString(context, geofenceTransition);

		// Get the Ids of each geofence that was triggered.
		List<String> triggeringGeofencesIdsList = new ArrayList<>();
		for (Geofence geofence : triggeringGeofences) {
			triggeringGeofencesIdsList.add(geofence.getRequestId());
		}
		String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

		return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
	}
}