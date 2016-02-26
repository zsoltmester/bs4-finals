package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.EmptyResponse;

public class RegisterPlayerDeviceRequest extends NetworkRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/registerPlayerDevice";

	/**
	 * Name of the IMEI in the HTTP request
	 */
	private static final String REQUEST_PARAM_DEVICE_ID = "deviceId";

	/**
	 * The IMEI number of the device
	 */
	@Required
	private String deviceId;

	/**
	 * Name of the GCM ID in the HTTP request
	 */
	private static final String REQUEST_PARAM_GCM_ID = "gcmId";

	/**
	 * The ID used for GCM
	 */
	@Required
	private String gcmId;

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceId() {
		return this.deviceId;
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
		requestParams.add(new Pair<>(REQUEST_PARAM_DEVICE_ID, this.deviceId));
		requestParams.add(new Pair<>(REQUEST_PARAM_GCM_ID, this.gcmId));
		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return EmptyResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.deviceId == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_DEVICE_ID);
		}

		if (this.gcmId == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_GCM_ID);
		}
	}

}
