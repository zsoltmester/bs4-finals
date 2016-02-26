package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.GetTasksForGameSessionResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Request for getting tasks for a given game session.
 */
public class GetTasksForGameSessionRequest extends NetworkRequest {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/getTasksForGameSession";

	/**
	 * Name of the GameSession ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_GAME_SESSION_ID = "gameSessionId";

	/**
	 * ID of the GameSession that is currently played
	 */
	@Required
	private int gameSessionId = UNSET_INTEGER;

	public int getGameSessionId() {
		return gameSessionId;
	}

	public void setGameSessionId(int gameSessionId) {
		this.gameSessionId = gameSessionId;
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public List<Pair<String, String>> getRequestParams() {
		List<Pair<String, String>> requestParams = new ArrayList<>();
		requestParams.add(new Pair<>(REQUEST_PARAM_GAME_SESSION_ID, "" + this.gameSessionId));
		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return GetTasksForGameSessionResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.gameSessionId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, REQUEST_PARAM_GAME_SESSION_ID);
		}
	}
}
