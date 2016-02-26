package hu.bsmart.framework.communication.data;

public enum FailingType {
	SILENT_FAIL("silentfail"),
	LIMITED_FAIL("limitedfail"),
	NO_FAIL("nofail");

	private String typeString;

	FailingType(String typeString) {
		this.typeString = typeString;
	}

	public String getTypeString() {
		return typeString;
	}

	public static FailingType from(String typeString) {
		for (FailingType failingType : FailingType.values()) {
			if (failingType.typeString.equals(typeString)) {
				return failingType;
			}
		}

		throw new IllegalArgumentException("No FailingType found for String: " + typeString);
	}

}
