package hu.bsmart.framework.communication.data;

import java.util.Date;

import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

public class TaskInstance extends DataObject {

	private Date endTime = null;

	@Required
	private String playerName = null;

	@Required
	private Progress progress = null;

	private Date startTime = null;

	@Required
	private Task task = null;

	@Required
	private int taskInstanceId = UNSET_INTEGER;

	private String taskManagerName = null;

	public TaskInstance() {
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public int getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(int taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public String getTaskManagerName() {
		return taskManagerName;
	}

	public void setTaskManagerName(String taskManagerName) {
		this.taskManagerName = taskManagerName;
	}

	@Override
	public void validate() throws ValidationException {
		if (playerName == null) {
			throw ValidationException.missingField(this, "playerName");
		}

		if (progress == null) {
			throw ValidationException.missingField(this, "progress");
		}

		if (task == null) {
			throw ValidationException.missingField(this, "task");
		}

		task.validate();

		if (taskInstanceId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "taskInstanceId");
		}
	}
}
