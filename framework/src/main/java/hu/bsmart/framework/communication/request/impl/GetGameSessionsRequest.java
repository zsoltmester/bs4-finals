package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.GetGameSessionsResponse;

public class GetGameSessionsRequest extends NetworkRequest {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/getGameSessions";

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public List<Pair<String, String>> getRequestParams() {
		return new ArrayList<>();
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return GetGameSessionsResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
	}
}
