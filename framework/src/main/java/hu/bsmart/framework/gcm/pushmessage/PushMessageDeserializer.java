package hu.bsmart.framework.gcm.pushmessage;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PushMessageDeserializer implements JsonDeserializer<PushMessage> {
	@Override
	public PushMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject pushMessageJsonObject = json.getAsJsonObject();
		PushMessage.PushMessageType pushMessageType = PushMessage.PushMessageType
				.from(pushMessageJsonObject.get(PushMessage.PUSH_MESSAGE_COMMAND).getAsString());
		return context.deserialize(pushMessageJsonObject.get(PushMessage.PUSH_MESSAGE_DATA),
				pushMessageType.getPushMessageClass());
	}
}
