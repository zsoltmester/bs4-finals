package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.LoginResponse;

public class LoginRequest extends NetworkRequest {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/login";

	/**
	 * Name of the name parameter in the HTTP request
	 */
	private static final String REQUEST_PARAM_NAME = "name";

	@Required
	private String name = null;

	/**
	 * Name of the password parameter in the HTTP request
	 */
	private static final String REQUEST_PARAM_PASSWORD = "password";

	@Required
	private String password = null;

	/**
	 * Name of the installation ID parameter in the HTTP request
	 */
	private static final String REQUEST_PARAM_INSTALLATION_ID = "installationId";

	@Required
	private String installationId = null;

	/**
	 * Name of the GCM ID parameter in the HTTP request
	 */
	private static final String REQUEST_PARAM_GCM_ID = "gcmId";

	@Required
	private String gcmId = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getInstallationId() {
		return installationId;
	}

	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public List<Pair<String, String>> getRequestParams() {
		List<Pair<String, String>> requestParams = new ArrayList<>();
		requestParams.add(new Pair<>(REQUEST_PARAM_NAME, name));
		requestParams.add(new Pair<>(REQUEST_PARAM_PASSWORD, password));
		requestParams.add(new Pair<>(REQUEST_PARAM_INSTALLATION_ID, installationId));
		requestParams.add(new Pair<>(REQUEST_PARAM_GCM_ID, gcmId));
		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return LoginResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
		if (name == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_NAME);
		}

		if (password == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_PASSWORD);
		}

		if (installationId == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_INSTALLATION_ID);
		}

		if (gcmId == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_GCM_ID);
		}
	}

}
