package hu.bsmart.framework.communication.networkhelper;

/**
 * An object that encapsulates the reason for communication failure,
 * and an optional error message with more detailed information.
 */
public class CommunicationError {

	/**
	 * Possible reasons of failure in the communication.
	 */
	public enum Reason {

		/**
		 * HTTP status code indicates there is
		 * something wrong.
		 */
		BAD_STATUS_CODE,

		/**
		 * The JSON sent by the server
		 * could not be parsed.
		 */
		BAD_JSON_RESPONSE,

		/**
		 * Connection to the server
		 * could not be created.
		 */
		COULD_NOT_CREATE_CONNECTION,

		/**
		 * Request could not be created.
		 */
		COULD_NOT_CREATE_REQUEST,

		/**
		 * Response could not be retrieved.
		 */
		COULD_NOT_RETRIEVE_RESPONSE,

		/**
		 * The encoding that should have been used
		 * in the communication was not present.
		 */
		MISSING_ENCODING,

		/**
		 * The request was not valid (for example,
		 * some required parameters were missing).
		 */
		INVALID_REQUEST,

		/**
		 * The response was not valid (for example
		 * some required parameters were missing).
		 */
		INVALID_RESPONSE,

	}

	private final Reason reason;
	private String errorMessage;

	/**
	 * Creates a new CommunicationError instance.
	 *
	 * @param reason       the main reason of the failure
	 * @param errorMessage an error message with more information (can be null)
	 */
	public CommunicationError(Reason reason, String errorMessage) {
		if (reason == null) {
			throw new NullPointerException("Reason is missing!");
		}

		this.reason = reason;
		this.errorMessage = errorMessage;
	}

	/**
	 * Returns the main failure reason.
	 *
	 * @return the reason
	 */
	public Reason getReason() {
		return reason;
	}

	@Override
	public String toString() {
		return reason.name() + (errorMessage != null ? (": " + errorMessage) : "");
	}
}
