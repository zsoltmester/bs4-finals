package hu.bsmart.framework.communication.data.taskdatatype;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskData;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

public class ArTaskData extends TaskData {
	@Required
	private int frameMarkerId = UNSET_INTEGER;

	@Required
	private String modelUri = null;

	@Required
	private String textureUri = null;

	public ArTaskData(int id) {
		super(id);
	}

	@Override
	public Type getType() {
		return Type.AR;
	}

	public int getFrameMarkerId() {
		return frameMarkerId;
	}

	public void setFrameMarkerId(int frameMarkerId) {
		this.frameMarkerId = frameMarkerId;
	}

	public String getModelUri() {
		return modelUri;
	}

	public void setModelUri(String modelUri) {
		this.modelUri = modelUri;
	}

	public String getTextureUri() {
		return textureUri;
	}

	public void setTextureUri(String textureUri) {
		this.textureUri = textureUri;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (frameMarkerId == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "frameMarkerId");
		}

		if (modelUri == null) {
			throw ValidationException.missingField(this, "modelUri");
		}

		if (textureUri == null) {
			throw ValidationException.missingField(this, "textureUri");
		}
	}
}
