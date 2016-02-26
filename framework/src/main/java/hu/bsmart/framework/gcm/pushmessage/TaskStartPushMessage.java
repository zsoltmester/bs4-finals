package hu.bsmart.framework.gcm.pushmessage;

import android.content.Context;
import android.support.annotation.NonNull;

import hu.bsmart.framework.R;
import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Push message indicating that a task has started
 */
public class TaskStartPushMessage extends PushMessage {

	@Required
	private int gameSessionId = UNSET_INTEGER;

	@Required
	private String gameName = null;

	@Required
	private String teamName = null;

	@Required
	private int playerId = UNSET_INTEGER;

	@Required
	private String taskName = null;

	@Required
	private int taskInstanceId = UNSET_INTEGER;

	public int getGameSessionId() {
		return gameSessionId;
	}

	public void setGameSessionId(int gameSessionId) {
		this.gameSessionId = gameSessionId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(int taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	@Override
	public PushMessageType getPushType() {
		return PushMessageType.TASK_START;
	}

	@Override
	public
	@NonNull
	String getEventSummary(Context context) {
		return context.getString(R.string.task_start_push_message_summary);
	}

	@Override
	public
	@NonNull
	String getEventDetails(Context context) {
		return context.getString(R.string.task_start_push_message_details, teamName, taskName, gameName);
	}

	@Override
	public void validate() throws ValidationException {
		if (gameSessionId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "gameSessionId");
		}

		if (gameName == null) {
			throw ValidationException.missingField(this, "gameName");
		}

		if (teamName == null) {
			throw ValidationException.missingField(this, "teamName");
		}

		if (playerId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "playerId");
		}

		if (taskName == null) {
			throw ValidationException.missingField(this, "taskName");
		}

		if (taskInstanceId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "taskInstanceId");
		}

	}

}
