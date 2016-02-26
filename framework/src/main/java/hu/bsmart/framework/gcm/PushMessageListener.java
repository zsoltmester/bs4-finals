package hu.bsmart.framework.gcm;

import hu.bsmart.framework.gcm.pushmessage.PushMessage;

/**
 * Listener interface for getting notifications about
 * the arriving GCM messages.
 * <p/>
 * The listener's methods will be called on the UI thread!
 */
public interface PushMessageListener {
	/**
	 * Called when a new push message arrived.
	 *
	 * @param pushMessage the push message object sent by the server
	 * @return {@code true} if the listener handled the push message
	 */
	boolean onPushMessageArrived(PushMessage pushMessage);

	/**
	 * Called when there is a problem with a new push message.
	 *
	 * @param errorMessage error message
	 * @return {@code true} if the listener handled the error
	 */
	boolean onPushMessageError(String errorMessage);

	/**
	 * Called when the GCM token changes
	 */
	void onGcmTokenChanged();
}
