package hu.bsmart.framework.communication.response;

import hu.bsmart.framework.communication.networkhelper.NetworkMessage;

/**
 * Base class for different response objects for network requests
 */
public abstract class NetworkResponse extends NetworkMessage {

	@Override
	public MessageType getMessageType() {
		return MessageType.RESPONSE;
	}

}
