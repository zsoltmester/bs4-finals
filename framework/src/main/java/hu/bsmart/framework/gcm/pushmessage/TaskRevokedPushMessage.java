package hu.bsmart.framework.gcm.pushmessage;

import android.content.Context;
import android.support.annotation.NonNull;

import hu.bsmart.framework.R;
import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Push message indicating that a task has been revoked
 */
public class TaskRevokedPushMessage extends PushMessage {

	@Required
	private String comment;

	@Required
	private int gameSessionId = UNSET_INTEGER;

	private int[] playerIds = null;

	@Required
	private int[] taskInstanceIds = null;

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

	public int[] getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(int[] playerIds) {
		this.playerIds = playerIds;
	}

	public int[] getTaskInstanceIds() {
		return taskInstanceIds;
	}

	public void setTaskInstanceIds(int[] taskInstanceIds) {
		this.taskInstanceIds = taskInstanceIds;
	}

	@Override
	public PushMessageType getPushType() {
		return PushMessageType.TASK_REVOKED;
	}

	@NonNull
	@Override
	public String getEventSummary(Context context) {
		return context.getString(R.string.task_revoked_push_message_summary);
	}

	@NonNull
	@Override
	public String getEventDetails(Context context) {
		int resource = taskInstanceIds.length == 1 ? R.string.task_revoked_push_message_single_details :
				R.string.task_revoked_push_message_multi_details;
		return context.getString(resource, comment);
	}

	@Override
	public void validate() throws ValidationException {
		if (comment == null) {
			throw ValidationException.missingField(this, "comment");
		}

		if (gameSessionId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "gameSessionId");
		}

		if (taskInstanceIds == null) {
			throw ValidationException.missingField(this, "taskInstanceIds");
		}

		if (taskInstanceIds.length == 0) {
			throw new ValidationException("No revoked taskInstanceId in the revoke push message!");
		}
	}
}
