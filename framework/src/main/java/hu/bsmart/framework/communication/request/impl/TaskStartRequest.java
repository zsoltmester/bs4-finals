package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.TaskStartResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * This requests indicates to the server that the player has started
 * the task.
 */
public class TaskStartRequest extends NetworkRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/taskStart";

	/**
	 * Name of the Player ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_PLAYER_ID = "playerId";

	/**
	 * ID of the player who currently holds the device
	 */
	@Required
	private int playerId = UNSET_INTEGER;

	/**
	 * Name of the GameSession ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_GAME_SESSION_ID = "gameSessionId";

	/**
	 * ID of the GameSession that is currently played
	 */
	@Required
	private int gameSessionId = UNSET_INTEGER;

	/**
	 * Name of the TaskInstance ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_TASK_INSTANCE_ID = "taskInstanceId";

	/**
	 * ID of the TaksInstance that is being started
	 */
	@Required
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
		requestParams.add(new Pair<>(REQUEST_PARAM_PLAYER_ID, "" + this.playerId));
		requestParams.add(new Pair<>(REQUEST_PARAM_GAME_SESSION_ID, "" + this.gameSessionId));
		requestParams.add(new Pair<>(REQUEST_PARAM_TASK_INSTANCE_ID, "" + this.taskInstanceId));

		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return TaskStartResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.playerId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, REQUEST_PARAM_PLAYER_ID);
		}

		if (this.gameSessionId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, REQUEST_PARAM_GAME_SESSION_ID);
		}

		if (this.taskInstanceId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, REQUEST_PARAM_TASK_INSTANCE_ID);
		}

	}
}
