package hu.bsmart.framework.communication.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import hu.bsmart.framework.communication.data.TaskData;
import hu.bsmart.framework.communication.data.TaskData.Type;
import hu.bsmart.framework.communication.data.taskdatatype.ArTaskData;
import hu.bsmart.framework.communication.data.taskdatatype.BeaconTaskData;
import hu.bsmart.framework.communication.data.taskdatatype.GpsTaskData;
import hu.bsmart.framework.communication.data.taskdatatype.InputTaskData;
import hu.bsmart.framework.communication.data.taskdatatype.NfcTaskData;
import hu.bsmart.framework.communication.data.taskdatatype.QrTaskData;
import hu.bsmart.framework.communication.data.taskdatatype.QuizTaskData;

public class TaskDataDeserializer implements JsonDeserializer<TaskData> {

	@Override
	public TaskData deserialize(JsonElement element, java.lang.reflect.Type objectType,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = element.getAsJsonObject();

		int id = object.get(TaskData.ID).getAsInt();
		int dataTypeId = object.get(TaskData.DATA_TYPE_ID).getAsInt();

		Type type = Type.from(dataTypeId);

		switch (type) {
			case AR:
				return context.deserialize(element, ArTaskData.class);
			case BEACON:
				return context.deserialize(element, BeaconTaskData.class);
			case BRANCH:
				return null;
			case GPS:
				return context.deserialize(element, GpsTaskData.class);
			case INPUT:
				return context.deserialize(element, InputTaskData.class);
			case MERGE:
				return null;
			case NFC:
				return context.deserialize(element, NfcTaskData.class);
			case OFFLINE:
				return null;
			case QR:
				return context.deserialize(element, QrTaskData.class);
			case QUIZ:
				return context.deserialize(element, QuizTaskData.class);
			default:
				return null;
		}
	}
}
