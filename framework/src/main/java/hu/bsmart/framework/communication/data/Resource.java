package hu.bsmart.framework.communication.data;

import hu.bsmart.framework.communication.annotations.Composite;
import hu.bsmart.framework.communication.annotations.Required;

@Composite
public class Resource extends DataObject {

	@Required
	private String title = null;

	@Required
	private String data = null;

	@Required
	private ResourceType resourceType = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public Resource() {
	}

	@Override
	public void validate() throws ValidationException {
		if (title == null) {
			throw ValidationException.missingField(this, "title");
		}

		if (data == null) {
			throw ValidationException.missingField(this, "data");
		}

		if (resourceType == null) {
			throw ValidationException.missingField(this, "resourceType");
		}
	}
}
