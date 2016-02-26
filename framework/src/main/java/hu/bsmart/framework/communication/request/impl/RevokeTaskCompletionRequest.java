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
 * Request for the coordinator to revoke a task
 */
public class RevokeTaskCompletionRequest extends NetworkRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/revokeTaskCompletion";

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
	 * Name of the comment parameter in the HTTP request
	 */
	private static final String REQUEST_PARAM_COMMENT = "comment";

	/**
	 * Comment for the task revocation
	 */
	@Required
	private String comment = null;

	public int getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(int taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
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
		requestParams.add(new Pair<>(REQUEST_PARAM_COMMENT, this.comment));
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

		if (this.comment == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_COMMENT);
		}
	}
}
