package hu.bsmart.framework.gcm.pushmessage;

import android.content.Context;
import android.support.annotation.NonNull;

import hu.bsmart.framework.R;
import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Push message indicating that a task has ended
 */
public class TaskDonePushMessage extends PushMessage {

	private String comment = null;

	@Required
	private int gameSessionId = UNSET_INTEGER;

	@Required
	private String gameName = null;

	@Required
	private String teamName = null;

	@Required
	private int playerId = UNSET_INTEGER;

	@Required
	private int taskInstanceId = UNSET_INTEGER;

	@Required
	private String taskName = null;

	@Required
	private int taskScore = UNSET_INTEGER;

	@Required
	private int totalScore = UNSET_INTEGER;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

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

	public int getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(int taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getTaskScore() {
		return taskScore;
	}

	public void setTaskScore(int taskScore) {
		this.taskScore = taskScore;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	@Override
	public PushMessageType getPushType() {
		return PushMessageType.TASK_DONE;
	}

	@Override
	public
	@NonNull
	String getEventSummary(Context context) {
		return context.getString(R.string.task_done_push_message_summary);
	}

	@Override
	public
	@NonNull
	String getEventDetails(Context context) {
		String newLine = System.getProperty("line.separator");
		StringBuilder builder = new StringBuilder();
		builder.append(context.getString(R.string.task_done_push_message_basic_details, teamName, taskName, gameName));
		builder.append(newLine).append(newLine);
		builder.append(context.getString(R.string.task_done_push_message_score_details, taskScore, totalScore));

		if (comment != null) {
			builder.append(newLine).append(newLine);
			builder.append(context.getString(R.string.task_done_push_message_comment_details, comment));
		}

		return builder.toString();
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

		if (taskInstanceId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "taskInstanceId");
		}

		if (taskName == null) {
			throw ValidationException.missingField(this, "taskName");
		}

		if (taskScore == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "taskScore");
		}

		if (totalScore == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "totalScore");
		}

	}

}
