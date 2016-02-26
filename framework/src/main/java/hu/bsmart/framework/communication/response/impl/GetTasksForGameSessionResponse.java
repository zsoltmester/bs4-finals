package hu.bsmart.framework.communication.response.impl;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskInstance;
import hu.bsmart.framework.communication.request.impl.GetTasksForGameSessionRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;

/**
 * Response for the {@link GetTasksForGameSessionRequest}
 */
public class GetTasksForGameSessionResponse extends NetworkResponse {

	private static final String RESPONSE_PARAM_TASKS = "taskInstances";

	@Required
	private TaskInstance[] taskInstances;

	public TaskInstance[] getTaskInstances() {
		return taskInstances;
	}

	public void setTaskInstances(TaskInstance[] taskInstances) {
		this.taskInstances = taskInstances;
	}

	@Override
	public void validate() throws ValidationException {
		if (taskInstances == null) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_TASKS);
		}

		for (TaskInstance taskInstance : taskInstances) {
			taskInstance.validate();
		}
	}
}
