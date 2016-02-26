package hu.bsmart.framework.communication.data;

import hu.bsmart.framework.communication.annotations.Composite;

@Composite
public enum ResourceType {
	TEXT(),
	IMAGE(),
	SOUND(),
	VIDEO(),
	URL();

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_IMAGE = 2;
	public static final int TYPE_SOUND = 3;
	public static final int TYPE_VIDEO = 4;
	public static final int TYPE_URL = 5;

	static {
		TEXT.id = TYPE_TEXT;
		IMAGE.id = TYPE_IMAGE;
		SOUND.id = TYPE_SOUND;
		VIDEO.id = TYPE_VIDEO;
		URL.id = TYPE_URL;
	}

	private int id;

	public int getId() {
		return id;
	}

	public static ResourceType from(int id) {
		for (ResourceType resourceType : ResourceType.values()) {
			if (resourceType.id == id) {
				return resourceType;
			}
		}

		throw new IllegalArgumentException("No ResourceType for id: " + id);
	}

}