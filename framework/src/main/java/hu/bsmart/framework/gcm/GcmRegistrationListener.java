package hu.bsmart.framework.gcm;

/**
 * Listener interface for getting notification about the result
 * of a GCM registration process.
 * <p/>
 * The listener's methods will be called on the UI thread by the GcmManager
 */
public interface GcmRegistrationListener {
	/**
	 * GCM registration succeeded.
	 *
	 * @param gcmToken the GCM token that the server can use to send requests.
	 */
	void onGcmRegistrationSucceeded(String gcmToken);

	/**
	 * Instance ID is available.
	 *
	 * @param instanceId the instance ID for the application
	 */
	void onInstanceIdAvailable(String instanceId);

	/**
	 * GCM registration failed.
	 */
	void onGcmRegistrationFailed();
}