package hu.bsmart.framework.communication.data;

import hu.bsmart.framework.communication.annotations.Composite;
import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * This object describes a concrete "instance" of a Game with
 * a playing team.
 * <p>
 * For example: there is a Game called "hide 'n' seek". Whenever a team
 * starts to play, a new GameSession is created that is closed when
 * the team finishes their game.
 */
@Composite
public class GameSession extends DataObject {

	/**
	 * The admin right for the logged in person over this game session
	 */
	@Required
	private AdminRight adminRight = null;

	/**
	 * GameSession id
	 */
	@Required
	private int id = UNSET_INTEGER;

	/**
	 * ID of the Game that this session belongs to
	 */
	@Required
	private int gameId = UNSET_INTEGER;

	/**
	 * Name of the Game that this session belongs to
	 */
	@Required
	private String gameName = null;

	/**
	 * Name of the playing team
	 */
	@Required
	private String teamName = null;

	/**
	 * Progress of the game session.
	 */
	@Required
	private Progress progress = null;

	/**
	 * Total score of the team in this game session.
	 */
	@Required
	private int totalScore = UNSET_INTEGER;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
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

	public AdminRight getAdminRight() {
		return adminRight;
	}

	public void setAdminRight(AdminRight adminRight) {
		this.adminRight = adminRight;
	}

	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	@Override
	public void validate() throws ValidationException {
		if (adminRight == null) {
			throw ValidationException.missingField(this, "adminRight");
		}

		if (id == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "id");
		}

		if (gameId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "gameId");
		}

		if (gameName == null) {
			throw ValidationException.missingField(this, "gameName");
		}

		if (teamName == null) {
			throw ValidationException.missingField(this, "teamName");
		}

		if (progress == null) {
			throw ValidationException.missingField(this, "progress");
		}

		if (totalScore == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "totalScore");
		}

	}
}
