package hu.bsmart.framework.communication.networkhelper;

import java.io.Serializable;

import hu.bsmart.framework.communication.response.NetworkResponse;

/**
 * Listener interface that clients can implement to receive notification
 * about the result of a request
 */
public interface NetworkResultListener extends Serializable {
	/**
	 * Called after a successfully processed request.
	 *
	 * @param requestCode the request code provided when starting the request
	 * @param response    the response for the request
	 */
	void onSuccess(String requestCode, NetworkResponse response);

	/**
	 * Called after a failed request.
	 *
	 * @param requestCode the code of the request that failed
	 * @param error       error object that contains information about the error
	 */
	void onFail(String requestCode, CommunicationError error);
}
