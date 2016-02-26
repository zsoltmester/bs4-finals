package hu.bsmart.framework.communication.request;

import android.util.Pair;

import java.util.List;

import hu.bsmart.framework.communication.networkhelper.NetworkHelper;
import hu.bsmart.framework.communication.networkhelper.NetworkMessage;
import hu.bsmart.framework.communication.response.NetworkResponse;

/**
 * Base class for different requests over network
 */
public abstract class NetworkRequest extends NetworkMessage {

	private static final String NEW_LINE_CHARACTER = System.getProperty("line.separator");

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public MessageType getMessageType() {
		return MessageType.REQUEST;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendDoubleLine(sb);
		sb.append("Request class: " + getObjectName());
		appendSingleLine(sb);
		sb.append("Response class: " + getResponseClass().getSimpleName());
		appendSingleLine(sb);
		sb.append("Relative URL: " + getRequestUrl());
		appendSingleLine(sb);
		sb.append("Parameters: ");
		appendSingleLine(sb);

		for (Pair<String, String> param : getRequestParams()) {
			sb.append(param.first + " = " + param.second);
			sb.append(NEW_LINE_CHARACTER);
		}

		sb.append("====================================================");
		return sb.toString();
	}

	private void appendSingleLine(StringBuilder sb) {
		sb.append(NEW_LINE_CHARACTER);
		sb.append("----------------------------------------------------");
		sb.append(NEW_LINE_CHARACTER);
	}

	private void appendDoubleLine(StringBuilder sb) {
		sb.append(NEW_LINE_CHARACTER);
		sb.append("====================================================");
		sb.append(NEW_LINE_CHARACTER);
	}

	/**
	 * Subclasses should provide their request URL by implementing this method.
	 *
	 * @return the relative URL for this type of {@code NetworkRequest}
	 */
	public abstract String getRequestUrl();

	/**
	 * Subclasses should provide their request parameters that should be sent to the server
	 * by overriding this method.
	 *
	 * @return request parameters that will be sent in the request's body
	 */
	public abstract List<Pair<String, String>> getRequestParams();

	/**
	 * Subclasses should provide their response's class by overriding this method. This
	 * is required so that the {@link NetworkHelper} can parse the HTTP answer and instantiate
	 * the correct response class.
	 *
	 * @return the class of the {@link NetworkResponse} for this type of {@code NetworkRequest}
	 */
	public abstract Class<? extends NetworkResponse> getResponseClass();

}
