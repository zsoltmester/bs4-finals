package hu.bsmart.framework.gcm.pushmessage;

import android.content.Context;
import android.support.annotation.NonNull;

import hu.bsmart.framework.R;
import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Push message indicating that a game session has started
 */
public class GameStartedPushMessage extends PushMessage {

	@Required
	private int gameSessionId = UNSET_INTEGER;

	@Required
	private String gameName = null;

	@Required
	private String teamName = null;

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

	@Override
	public PushMessageType getPushType() {
		return PushMessageType.GAME_STARTED;
	}

	@Override
	public
	@NonNull
	String getEventSummary(Context context) {
		return context.getString(R.string.game_started_push_message_summary);
	}

	@Override
	public
	@NonNull
	String getEventDetails(Context context) {
		return context.getString(R.string.game_started_push_message_details, teamName, gameName);
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
	}
}