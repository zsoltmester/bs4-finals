package hu.bsmart.framework.communication.response.impl;

import java.util.Date;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.impl.TaskDoneRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Response for the {@link TaskDoneRequest}
 */
public class TaskDoneResponse extends NetworkResponse {

	@Required
	private Date endTime = null;

	@Required
	private int score = UNSET_INTEGER;

	@Required
	private int totalScore = UNSET_INTEGER;

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	@Override
	public void validate() throws ValidationException {
		if (endTime == null) {
			throw ValidationException.missingField(this, "endTime");
		}

		if (score == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "score");
		}

		if (totalScore == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "totalScore");
		}
	}

}
