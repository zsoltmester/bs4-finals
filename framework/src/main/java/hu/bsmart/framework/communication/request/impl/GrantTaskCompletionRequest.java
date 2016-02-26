package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.EmptyResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Request for accepting a task by the coordinator
 */
public class GrantTaskCompletionRequest extends NetworkRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of the request
	 */
	private static final String REQUEST_URL = "/services/grantTaskCompletion";

	/**
	 * Name of the task instance ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_TASK_INSTANCE_ID = "taskInstanceId";

	/**
	 * The task instance ID
	 */
	@Required
	private int taskInstanceId = UNSET_INTEGER;

	/**
	 * Name of the score parameter in the HTTP request
	 */
	private static final String REQUEST_PARAM_SCORE = "score";

	/**
	 * The score for the task
	 */
	@Required
	private int score = UNSET_INTEGER;

	/**
	 * Name of the comment parameter in the HTTP request
	 */
	private static final String REQUEST_PARAM_COMMENT = "comment";

	/**
	 * Comment for the task completion (optional)
	 */
	private String comment = null;

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public List<Pair<String, String>> getRequestParams() {
		List<Pair<String, String>> requestParams = new ArrayList<>();
		requestParams.add(new Pair<>(REQUEST_PARAM_TASK_INSTANCE_ID, "" + this.taskInstanceId));
		requestParams.add(new Pair<>(REQUEST_PARAM_SCORE, "" + this.score));
		if (comment != null) {
			requestParams.add(new Pair<>(REQUEST_PARAM_COMMENT, this.comment));
		}
		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return EmptyResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.taskInstanceId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, REQUEST_PARAM_TASK_INSTANCE_ID);
		}

		if (this.score == UNSET_INTEGER) {
			throw ValidationException.missingField(this, REQUEST_PARAM_SCORE);
		}
	}

}
