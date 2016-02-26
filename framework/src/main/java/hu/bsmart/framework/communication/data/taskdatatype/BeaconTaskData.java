package hu.bsmart.framework.communication.data.taskdatatype;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskData;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

public class BeaconTaskData extends TaskData {

	@Required
	private String uuid = null;

	@Required
	private int major = UNSET_INTEGER;

	@Required
	private int minor = UNSET_INTEGER;

	public BeaconTaskData(int id) {
		super(id);
	}

	@Override
	public Type getType() {
		return Type.BEACON;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (uuid == null) {
			throw ValidationException.missingField(this, "uuid");
		}

		if (major == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "major");
		}

		if (minor == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "minor");
		}
	}

}
