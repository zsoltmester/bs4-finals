package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.TaskDoneResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * This requests indicates to the server that the player has finished
 * the task.
 */
public class TaskDoneRequest extends NetworkRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/taskDone";

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
	 * ID of the TaksInstance that is being finished
	 */
	@Required
	private int taskInstanceId = UNSET_INTEGER;

	/**
	 * Name of the score in the HTTP request
	 */
	private static final String REQUEST_PARAM_SCORE = "score";

	/**
	 * Score of the finished task
	 */
	@Required
	private int score = UNSET_INTEGER;

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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public List<Pair<String, String>> getRequestParams() {
		List<Pair<String, String>> requestParams = new ArrayList<Pair<String, String>>(4);
		requestParams.add(new Pair<String, String>(REQUEST_PARAM_PLAYER_ID, "" + this.playerId));
		requestParams.add(new Pair<String, String>(REQUEST_PARAM_GAME_SESSION_ID, "" + this.gameSessionId));
		requestParams.add(new Pair<String, String>(REQUEST_PARAM_TASK_INSTANCE_ID, "" + this.taskInstanceId));
		requestParams.add(new Pair<String, String>(REQUEST_PARAM_SCORE, "" + this.score));

		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return TaskDoneResponse.class;
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

		if (this.score == UNSET_INTEGER) {
			throw ValidationException.missingField(this, REQUEST_PARAM_SCORE);
		}
	}

}
