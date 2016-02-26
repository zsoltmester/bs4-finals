package hu.bsmart.framework.communication.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import hu.bsmart.framework.communication.data.taskdatatype.QuizTaskData.QuizType;

public class QuizTypeDeserializer implements JsonDeserializer<QuizType> {
	@Override
	public QuizType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return QuizType.from(json.getAsString());
	}
}
