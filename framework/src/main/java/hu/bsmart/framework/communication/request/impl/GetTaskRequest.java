package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.GetTaskResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Request for getting the next task (if any) for the active game on this device.
 */
public class GetTaskRequest extends NetworkRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/getTask";

	/**
	 * Name of the Player ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_PLAYER_ID = "playerId";

	/**
	 * ID of the player who currently holds the device
	 */
	private int playerId = UNSET_INTEGER;

	/**
	 * Name of the GameSession ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_GAME_SESSION_ID = "gameSessionId";

	/**
	 * ID of the GameSession that is currently played
	 */
	private int gameSessionId = UNSET_INTEGER;

	/**
	 * Name of the TaskInstance ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_TASK_INSTANCE_ID = "taskInstanceId";

	/**
	 * ID of the TaskInstance to retrieve. TaskInstanceID is optional,
	 * if not set, the next task is returned automatically.
	 */
	private int taskInstanceId = UNSET_INTEGER;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getGameSessionId() {
		return gameSessionId;
	}

	public void setGameSessionId(int gameSessionId) {
		this.gameSessionId = gameSessionId;
	}

	public int getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(int taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public List<Pair<String, String>> getRequestParams() {
		List<Pair<String, String>> requestParams = new ArrayList<>();
		if (playerId != UNSET_INTEGER) {
			requestParams.add(new Pair<>(REQUEST_PARAM_PLAYER_ID, "" + playerId));
		}
		if (gameSessionId != UNSET_INTEGER) {
			requestParams.add(new Pair<>(REQUEST_PARAM_GAME_SESSION_ID, "" + gameSessionId));
		}

		if (this.taskInstanceId != UNSET_INTEGER) {
			requestParams.add(new Pair<>(REQUEST_PARAM_TASK_INSTANCE_ID, "" + taskInstanceId));
		}

		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return GetTaskResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
		if (taskInstanceId == UNSET_INTEGER) {
			if (this.playerId == UNSET_INTEGER) {
				throw ValidationException.missingField(this, REQUEST_PARAM_PLAYER_ID);
			}

			if (this.gameSessionId == UNSET_INTEGER) {
				throw ValidationException.missingField(this, REQUEST_PARAM_GAME_SESSION_ID);
			}
		}
	}

}
