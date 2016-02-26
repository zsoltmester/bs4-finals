package hu.bsmart.framework.geofence;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for processing Geofence events.
 */
public class GeofenceManager {

	/**
	 * Listener interface for getting notification about
	 * Geofence events.
	 */
	public interface GeofenceListener {
		/**
		 * Called when the user entered into the Geofence.
		 *
		 * @param context a Context for building notifications, etc.
		 */
		void onEnter(Context context);

		/**
		 * Called when the user exited the Geofence.
		 *
		 * @param context a Context for building notifications, etc.
		 */
		void onExit(Context context);
	}

	private static GeofenceManager instance = null;

	private List<GeofenceListener> listeners = new ArrayList<>();

	private GeofenceManager() {
	}

	public static GeofenceManager getInstance() {
		if (instance == null) {
			instance = new GeofenceManager();
		}

		return instance;
	}

	/**
	 * Registers a {@link GeofenceListener} to be notified about geofence events.
	 *
	 * @param listener the listener
	 */
	public void registerGeofenceListener(GeofenceListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must not be null!");
		}

		synchronized (GeofenceManager.class) {
			listeners.add(listener);
		}
	}

	/**
	 * Unregisters a {@link GeofenceListener}. It will not be notified about geofence events any more.
	 *
	 * @param listener the listener
	 */
	public void unregisterGeofenceListener(GeofenceListener listener) {
		if (listener == null) {
			return;
		}

		synchronized (GeofenceManager.class) {
			listeners.remove(listener);
		}
	}

	/**
	 * Unregisters all listeners.
	 */
	public void unregisterAllListeners() {
		synchronized (GeofenceManager.class) {
			listeners.clear();
		}
	}

	/**
	 * Dispatches a geofence enter event for registered listeners.
	 *
	 * @param context Android Context
	 */
	void dispatchEnterEvent(Context context) {
		if (context == null) {
			throw new NullPointerException("Context is missing!");
		}

		synchronized (GeofenceManager.class) {
			for (GeofenceListener listener : listeners) {
				listener.onEnter(context);
			}
		}
	}

	/**
	 * Dispatches a geofence exit event for registered listeners.
	 *
	 * @param context Android Context
	 */
	void dispatchExitEvent(Context context) {
		if (context == null) {
			throw new NullPointerException("Context is missing!");
		}

		synchronized (GeofenceManager.class) {
			for (GeofenceListener listener : listeners) {
				listener.onExit(context);
			}
		}
	}
}
