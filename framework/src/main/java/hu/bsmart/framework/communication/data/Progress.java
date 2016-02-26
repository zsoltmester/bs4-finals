package hu.bsmart.framework.communication.data;

public enum Progress {
	NOT_STARTED("notstarted"),
	STARTED("started"),
	FINISHED("finished");

	private String progressString;

	Progress(String progressString) {
		this.progressString = progressString;
	}

	public String getProgressString() {
		return progressString;
	}

	public static Progress from(String progressString) {
		for (Progress progress : Progress.values()) {
			if (progress.progressString.equals(progressString)) {
				return progress;
			}
		}

		throw new IllegalArgumentException("No Progress found for String: " + progressString);
	}

}
