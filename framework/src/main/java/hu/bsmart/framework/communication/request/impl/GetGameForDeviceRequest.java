package hu.bsmart.framework.communication.request.impl;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.GetGameForDeviceResponse;

/**
 * Request that gets the currently active game for the device (if any)
 */
public class GetGameForDeviceRequest extends NetworkRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL of this request
	 */
	private static final String REQUEST_URL = "/services/getGameForDevice";

	/**
	 * Name of the IMEI in the HTTP request
	 */
	private static final String REQUEST_PARAM_IMEI = "imei";

	/**
	 * The IMEI number of the device
	 */
	@Required
	private String imei = null;

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImei() {
		return this.imei;
	}

	@Override
	public String getRequestUrl() {
		return REQUEST_URL;
	}

	@Override
	public List<Pair<String, String>> getRequestParams() {
		List<Pair<String, String>> requestParams = new ArrayList<>();
		requestParams.add(new Pair<>(REQUEST_PARAM_IMEI, this.imei));
		return requestParams;
	}

	@Override
	public Class<? extends NetworkResponse> getResponseClass() {
		return GetGameForDeviceResponse.class;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.imei == null) {
			throw ValidationException.missingField(this, REQUEST_PARAM_IMEI);
		}
	}

}
