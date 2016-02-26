package hu.bsmart.framework.communication.data;

import java.io.Serializable;

public abstract class DataObject implements Serializable {

	/**
	 * Checks if the {@code DataObject} is valid or not.
	 *
	 * @throws ValidationException if the data object is invalid
	 */
	public abstract void validate() throws ValidationException;

	public String getObjectName() {
		return getClass().getSimpleName();
	}

	/**
	 * Exception indicating that a data object is not valid.
	 */
	public static class ValidationException extends Exception {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private static final String MISSING_FIELD_ERROR_START = "\"%s\" is missing from %s";

		public ValidationException() {
		}

		public ValidationException(String message) {
			super(message);
		}

		/**
		 * Creates a ValidationException that indicates a missing field in a DataObject.
		 *
		 * @param dataObject the object
		 * @param fieldName  the name of the missing field
		 * @return the ValidationException
		 */
		public static ValidationException missingField(DataObject dataObject, String fieldName) {
			String errorMessage = String.format(MISSING_FIELD_ERROR_START, fieldName, dataObject.getObjectName());
			return new ValidationException(errorMessage);
		}

	}
}
