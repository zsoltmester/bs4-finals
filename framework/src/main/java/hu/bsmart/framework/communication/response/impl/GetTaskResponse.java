package hu.bsmart.framework.communication.response.impl;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskInstance;
import hu.bsmart.framework.communication.request.impl.GetTaskRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;

/**
 * Response for the {@link GetTaskRequest}
 */
public class GetTaskResponse extends NetworkResponse {

	private static final String RESPONSE_PARAM_TASK_INSTANCE = "taskInstance";

	@Required
	private TaskInstance taskInstance = null;

	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	@Override
	public void validate() throws ValidationException {
		if (taskInstance == null) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_TASK_INSTANCE);
		}

		taskInstance.validate();
	}
}
