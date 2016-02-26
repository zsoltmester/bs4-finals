package hu.bsmart.framework.ui.activity;

class LocationConstants {
	/**
	 * The most frequent times between updates in milliseconds
	 */
	static final long MIN_TIME_BW_UPDATES_MS = 1000; // 1 sec

	/**
	 * The least frequent times between updates in milliseconds
	 */
	static final long MAX_TIME_BW_UPDATES_MS = 1000 * 5; // 5 sec

	/**
	 * The geofence radius
	 */
	static final float GEOFENCE_RADIUS_IN_METERS = 1000;

	/**
	 * Geofence request ID
	 */
	static final String GEOFENCE_REQUEST_ID = "GEOFENCE_REQUEST_ID";
}
