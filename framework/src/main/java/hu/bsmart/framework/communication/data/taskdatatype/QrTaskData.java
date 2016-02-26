package hu.bsmart.framework.communication.data.taskdatatype;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskData;

public class QrTaskData extends TaskData {

	@Required
	private String data = null;

	@Override
	public Type getType() {
		return Type.QR;
	}

	public QrTaskData(int id) {
		super(id);
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
