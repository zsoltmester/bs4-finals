package hu.bsmart.framework.communication.response.impl;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.response.NetworkResponse;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

public class LoginResponse extends NetworkResponse {

	private static final String RESPONSE_PARAM_TOKEN = "token";

	@Required
	private String token = null;

	private static final String RESPONSE_PARAM_TOKEN_ID = "tokenId";

	@Required
	private int tokenId = UNSET_INTEGER;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public void validate() throws ValidationException {
		if (this.token == null) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_TOKEN);
		}

		if (this.tokenId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, RESPONSE_PARAM_TOKEN_ID);
		}
	}

}
