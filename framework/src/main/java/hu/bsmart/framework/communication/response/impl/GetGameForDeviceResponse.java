package hu.bsmart.framework.communication.response.impl;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.GameSession;
import hu.bsmart.framework.communication.request.impl.GetGameForDeviceRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Response for the {@link GetGameForDeviceRequest}
 */
public class GetGameForDeviceResponse extends NetworkResponse {

	private static final String RESPONSE_PARAM_PLAYER_ID = "playerId";
	private static final String RESPONSE_PARAM_ROLE_ID = "roleId";
	private static final String RESPONSE_PARAM_DEVICE_ID = "deviceId";
	private static final String RESPONSE_PARAM_GAME_SESSION = "gameSession";

	@Required
	private int playerId = UNSET_INTEGER;

	@Required
	private int roleId = UNSET_INTEGER;

	@Required
	private int deviceId = UNSET_INTEGER;

	@Required
	private GameSession gameSession = null;

	public int getPlayerId() {
		return playerId;
	}

	public int getRoleId() {
		return roleId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public GameSession getGameSession() {
		return gameSession;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public void setGameSession(GameSession gameSession) {
		this.gameSession = gameSession;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.playerId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_PLAYER_ID);
		}

		if (this.roleId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_ROLE_ID);
		}

		if (this.deviceId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_DEVICE_ID);
		}

		if (this.gameSession == null) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_GAME_SESSION);
		}
	}

}
