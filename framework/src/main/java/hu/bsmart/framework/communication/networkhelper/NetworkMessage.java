package hu.bsmart.framework.communication.networkhelper;

import hu.bsmart.framework.communication.data.DataObject;

/**
 * Common base class for messages that are sent and received over the network.
 */
public abstract class NetworkMessage extends DataObject {

	/**
	 * Gets the type of the message as a {@code MessageType} enum.
	 *
	 * @return
	 */
	public abstract MessageType getMessageType();

	/**
	 * Enumeration describing the possible types of the messages
	 */
	public enum MessageType {
		/**
		 * Simple network request that we will get a response for
		 */
		REQUEST("NetworkMessage:Request"),

		/**
		 * Response for a network request
		 */
		RESPONSE("NetworkMessage:Response");

		private final String name;

		MessageType(String name) {
			this.name = name;
		}

		/**
		 * Gets the name of the message type.
		 */
		public String getName() {
			return name;
		}
	}

}
