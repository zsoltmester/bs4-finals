package hu.bsmart.framework.communication.data;

import hu.bsmart.framework.communication.annotations.Required;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

public abstract class TaskData extends DataObject {

	private static final int TYPE_ID_QR = 1;
	private static final int TYPE_ID_NFC = 2;
	private static final int TYPE_ID_BEACON = 3;
	private static final int TYPE_ID_AR = 4;
	private static final int TYPE_ID_GPS = 5;
	private static final int TYPE_ID_QUIZ = 6;
	private static final int TYPE_ID_INPUT = 7;
	private static final int TYPE_ID_BRANCH = 8;
	private static final int TYPE_ID_MERGE = 9;
	private static final int TYPE_ID_OFFLINE = 10;

	public static final String ID = "id";
	public static final String DATA_TYPE_ID = "dataTypeId";

	/**
	 * Type of the TaskData. It can be used at deserialization, and
	 * also to decide which task screen to navigate to.
	 */
	public enum Type {
		QR(TYPE_ID_QR),
		NFC(TYPE_ID_NFC),
		BEACON(TYPE_ID_BEACON),
		AR(TYPE_ID_AR),
		GPS(TYPE_ID_GPS),
		QUIZ(TYPE_ID_QUIZ),
		INPUT(TYPE_ID_INPUT),
		BRANCH(TYPE_ID_BRANCH),
		MERGE(TYPE_ID_MERGE),
		OFFLINE(TYPE_ID_OFFLINE);

		private int id;

		Type(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static Type from(int id) {
			for (Type type : Type.values()) {
				if (type.id == id) {
					return type;
				}
			}

			throw new IllegalArgumentException("No Type exists for the given ID: " + id);
		}
	}

	@Required
	protected int id = UNSET_INTEGER;

	protected TaskData(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public abstract Type getType();

	@Override
	public void validate() throws ValidationException {
		if (id == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "id");
		}
	}
}
