package hu.bsmart.framework.communication.response.impl;

import hu.bsmart.framework.communication.response.NetworkResponse;

public class EmptyResponse extends NetworkResponse {
	@Override
	public void validate() throws ValidationException {
	}
}
