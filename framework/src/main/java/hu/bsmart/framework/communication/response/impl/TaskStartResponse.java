package hu.bsmart.framework.communication.response.impl;

import java.util.Date;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.impl.TaskStartRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

/**
 * Response for the {@link TaskStartRequest}
 */
public class TaskStartResponse extends NetworkResponse {

	private static final String RESPONSE_PARAM_START_TIME = "startTime";

	@Required
	private Date startTime = null;

	private static final String RESPONSE_PARAM_MAX_TIME = "maxTime";

	private int maxTime = UNSET_INTEGER;

	private static final String RESPONSE_PARAM_NO_TIME = "noTime";

	private int noTime = UNSET_INTEGER;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public int getNoTime() {
		return noTime;
	}

	public void setNoTime(int noTime) {
		this.noTime = noTime;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.startTime == null) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_START_TIME);
		}
	}

}
