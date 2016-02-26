package hu.bsmart.framework.gcm.pushmessage;

import android.content.Context;
import android.support.annotation.NonNull;

import hu.bsmart.framework.communication.data.DataObject;

/**
 * Base class for push messages
 */
public abstract class PushMessage extends DataObject {
	public static final String PUSH_MESSAGE_COMMAND = "command";
	public static final String PUSH_MESSAGE_DATA = "data";

	/**
	 * Returns the type of the push message.
	 *
	 * @return the type
	 */
	public abstract PushMessageType getPushType();

	/**
	 * Returns a summary text about the
	 * event triggered the notification.
	 *
	 * @param context Android context
	 * @return the summary
	 */
	public abstract
	@NonNull
	String getEventSummary(Context context);

	/**
	 * Returns a description of the
	 * event that triggered the notification.
	 *
	 * @param context Android context
	 * @return the description
	 */
	public abstract
	@NonNull
	String getEventDetails(Context context);

	/**
	 * Enumeration describing the possible types of push messages
	 */
	public enum PushMessageType {
		/**
		 * Push message indicating that a game session has started
		 */
		GAME_STARTED("gameStarted", GameStartedPushMessage.class),

		/**
		 * Push message indicating that a game session has ended
		 */
		GAME_ENDED("gameEnded", GameEndedPushMessage.class),

		/**
		 * Push message indicating that a task has started
		 */
		TASK_START("taskStart", TaskStartPushMessage.class),

		/**
		 * Push message indicating that a task has ended
		 */
		TASK_DONE("taskDone", TaskDonePushMessage.class),

		/**
		 * Push message indicating that a task has been revoked
		 */
		TASK_REVOKED("taskRevoked", TaskRevokedPushMessage.class);

		private final String command;
		private final Class<? extends PushMessage> pushMessageClass;

		PushMessageType(String command, Class<? extends PushMessage> pushMessageClass) {
			this.command = command;
			this.pushMessageClass = pushMessageClass;
		}

		/**
		 * Gets the class of the push message
		 * corresponding to this type.
		 *
		 * @return the implementation class for the push message type
		 */
		Class<? extends PushMessage> getPushMessageClass() {
			return pushMessageClass;
		}

		static PushMessageType from(String commandString) {
			for (PushMessageType pushMessageType : PushMessageType.values()) {
				if (pushMessageType.command.equals(commandString)) {
					return pushMessageType;
				}
			}

			throw new IllegalArgumentException("No PushMessage for command: " + commandString);
		}
	}
}
