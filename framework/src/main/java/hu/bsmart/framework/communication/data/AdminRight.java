package hu.bsmart.framework.communication.data;

public enum AdminRight {
	COORDINATOR("coordinator"),
	PLAYER("player"),
	TASK_MANAGER("taskmanager");

	private String adminRightString;

	AdminRight(String adminRightString) {
		this.adminRightString = adminRightString;
	}

	public String getAdminRightString() {
		return adminRightString;
	}

	public static AdminRight from(String adminRightString) {
		for (AdminRight adminRight : AdminRight.values()) {
			if (adminRight.adminRightString.equals(adminRightString)) {
				return adminRight;
			}
		}

		throw new IllegalArgumentException("No AdminRight found for String: " + adminRightString);
	}

}
