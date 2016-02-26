package hu.bsmart.framework.communication.data;

import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

public class Task extends DataObject {
	@Required
	private String description;

	@Required
	private FailingType failingType = null;

	// Required if failingType != NO_FAIL
	private int failPenalty = UNSET_INTEGER;

	@Required
	private int maxScore = UNSET_INTEGER;

	private int maxScoreTime = UNSET_INTEGER;

	@Required
	private String name;

	private int noScoreTime = UNSET_INTEGER;

	@Required
	private boolean optional = false;

	private Resource[] resources = null;

	// Required if failingType == LIMITED_FAIL
	private int retryNumber = UNSET_INTEGER;

	private Role role = null;

	private TaskData taskData = null;

	@Required
	private TaskType taskType = null;

	public Task() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FailingType getFailingType() {
		return failingType;
	}

	public void setFailingType(FailingType failingType) {
		this.failingType = failingType;
	}

	public int getFailPenalty() {
		return failPenalty;
	}

	public void setFailPenalty(int failPenalty) {
		this.failPenalty = failPenalty;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getMaxScoreTime() {
		return maxScoreTime;
	}

	public void setMaxScoreTime(int maxScoreTime) {
		this.maxScoreTime = maxScoreTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoScoreTime() {
		return noScoreTime;
	}

	public void setNoScoreTime(int noScoreTime) {
		this.noScoreTime = noScoreTime;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public Resource[] getResources() {
		return resources;
	}

	public void setResources(Resource[] resources) {
		this.resources = resources;
	}

	public int getRetryNumber() {
		return retryNumber;
	}

	public void setRetryNumber(int retryNumber) {
		this.retryNumber = retryNumber;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public TaskData getTaskData() {
		return taskData;
	}

	public void setTaskData(TaskData taskData) {
		this.taskData = taskData;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	@Override
	public void validate() throws ValidationException {
		if (description == null) {
			throw ValidationException.missingField(this, "description");
		}

		if (failingType == null) {
			throw ValidationException.missingField(this, "failingType");
		}

		if (failingType != FailingType.SILENT_FAIL) {
			if (failPenalty == UNSET_INTEGER) {
				throw ValidationException.missingField(this, "failPenalty");
			}

			if (failPenalty <= 0) {
				throw new ValidationException("FailPenalty must be positive if failingType != SILENT_FAIL!");
			}
		}

		if (maxScore == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "maxScore");
		}

		if (noScoreTime != UNSET_INTEGER && maxScoreTime == UNSET_INTEGER) {
			// If there is noScoreTime, there should be maxScoreTime
			throw ValidationException.missingField(this, "maxScoreTime");
		}

		// TODO: checking optional validity!

		if (name == null) {
			throw ValidationException.missingField(this, "name");
		}

		if (maxScoreTime != UNSET_INTEGER && noScoreTime == UNSET_INTEGER) {
			// If there is maxScoreTime, there should be noScoreTime
			throw ValidationException.missingField(this, "noScoreTime");
		}

		if (resources != null) {
			for (Resource resource : resources) {
				resource.validate();
			}
		}

		if (failingType == FailingType.LIMITED_FAIL) {
			if (retryNumber == UNSET_INTEGER) {
				throw ValidationException.missingField(this, "retryNumber");
			}

			if (retryNumber <= 0) {
				throw new ValidationException("RetryNumber must be positive if failingType == LIMITED_FAIL!");
			}
		}

		if (role != null) {
			role.validate();
		}

		if (taskType == null) {
			throw ValidationException.missingField(this, "taskType");
		}

		taskType.validate();

		TaskData.Type type = TaskData.Type.from(taskType.getId());

		if (type != TaskData.Type.BRANCH && type != TaskData.Type.MERGE && type != TaskData.Type.OFFLINE) {
			if (taskData == null) {
				throw ValidationException.missingField(this, "taskData");
			}
		}

		if (taskData != null) {
			taskData.validate();
		}
	}
}
