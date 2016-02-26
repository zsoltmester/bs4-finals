package hu.bsmart.framework.communication.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import hu.bsmart.framework.communication.data.AdminRight;

public class AdminRightDeserializer implements JsonDeserializer<AdminRight> {

	@Override
	public AdminRight deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return AdminRight.from(json.getAsString());
	}
}
