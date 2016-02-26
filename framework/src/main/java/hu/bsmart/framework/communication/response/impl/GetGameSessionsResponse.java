package hu.bsmart.framework.communication.response.impl;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.GameSession;
import hu.bsmart.framework.communication.request.impl.GetGameSessionsRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;

/**
 * Response for the {@link GetGameSessionsRequest}
 */
public class GetGameSessionsResponse extends NetworkResponse {

	private static final String RESPONSE_PARAM_GAME_SESSIONS = "gameSessions";

	@Required
	private GameSession[] gameSessions = null;

	public GameSession[] getGameSessions() {
		return gameSessions;
	}

	public void setGameSessions(GameSession[] gameSessions) {
		this.gameSessions = gameSessions;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.gameSessions == null) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_GAME_SESSIONS);
		}

		for (GameSession gameSession : gameSessions) {
			gameSession.validate();
		}
	}

}
