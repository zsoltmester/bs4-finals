package hu.bsmart.framework.communication.data;

import hu.bsmart.framework.communication.annotations.Composite;
import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

@Composite
public class Role extends DataObject {

	@Required
	private int id = UNSET_INTEGER;

	@Required
	private String name = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void validate() throws ValidationException {
		if (id == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "id");
		}

		if (name == null) {
			throw ValidationException.missingField(this, "name");
		}
	}
}
