package hu.bsmart.framework.communication.data.taskdatatype;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskData;

public class GpsTaskData extends TaskData {

	private static final String HELP_TYPE_NO_HELP = "nohelp";
	private static final String HELP_TYPE_DIRECTION_AND_DISTANCE = "dirdist";
	private static final String HELP_TYPE_MAP = "map";

	public enum HelpType {

		NO_HELP(HELP_TYPE_NO_HELP),
		DIRECTION_AND_DISTANCE(HELP_TYPE_DIRECTION_AND_DISTANCE),
		MAP(HELP_TYPE_MAP),;

		private String typeString;

		HelpType(String typeString) {
			this.typeString = typeString;
		}

		public String getTypeString() {
			return typeString;
		}

		public static HelpType from(String typeString) {
			for (HelpType helpType : HelpType.values()) {
				if (helpType.typeString.equals(typeString)) {
					return helpType;
				}
			}

			throw new IllegalArgumentException("No HelpType found for String: " + typeString);
		}

	}

	@Required
	private HelpType helpType = null;

	@Required
	private double lat = Double.NaN;

	@Required
	private double lon = Double.NaN;

	@Required
	private double distance = Double.NaN;

	public GpsTaskData(int id) {
		super(id);
	}

	@Override
	public Type getType() {
		return Type.GPS;
	}

	public HelpType getHelpType() {
		return helpType;
	}

	public void setHelpType(HelpType helpType) {
		this.helpType = helpType;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();

		if (helpType == null) {
			throw ValidationException.missingField(this, "helpType");
		}

		if (Double.isNaN(lat)) {
			throw ValidationException.missingField(this, "lat");
		}

		if (Double.isNaN(lon)) {
			throw ValidationException.missingField(this, "lon");
		}

		if (Double.isNaN(distance)) {
			throw ValidationException.missingField(this, "distance");
		}
	}
}
