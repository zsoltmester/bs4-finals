package hu.bsmart.framework.communication.data.taskdatatype;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskData;

public class NfcTaskData extends TaskData {

	@Required
	private String data = null;

	public NfcTaskData(int id) {
		super(id);
	}

	@Override
	public Type getType() {
		return Type.NFC;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();

		if (data == null) {
			throw ValidationException.missingField(this, "data");
		}
	}
}
