package hu.bsmart.framework.communication.networkhelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import hu.bsmart.framework.communication.data.FailingType;
import hu.bsmart.framework.communication.data.ResourceType;
import hu.bsmart.framework.communication.data.TaskData.Type;
import hu.bsmart.framework.communication.data.taskdatatype.GpsTaskData.HelpType;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.communication.response.impl.EmptyResponse;
import hu.bsmart.framework.communication.response.impl.GetGameForDeviceResponse;
import hu.bsmart.framework.communication.response.impl.GetTaskResponse;
import hu.bsmart.framework.communication.response.impl.LoginResponse;
import hu.bsmart.framework.communication.response.impl.TaskDoneResponse;
import hu.bsmart.framework.communication.response.impl.TaskStartResponse;

/**
 * Utility class containing constants and helper methods
 * used in code related to network communication.
 */
public class NetworkMockUtil {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private NetworkMockUtil() {
	}

	private static Random random = new Random();

	public static final String TEXT_RESOURCE = "This is a text help";

	public static final String IMAGE_RESOURCE_URL = "http://ssl.gstatic.com/gb/images/i2_9ef0f6fa.png";

	public static final String SOUND_RESOURCE_URL = "http://users.hszk.bme.hu/~jg941/death.mp3";

	// TODO: Provide some valid URL to a video
	//public static final String VIDEO_RESOURCE_URL = "http://www.google.com";
	public static final String VIDEO_RESOURCE_URL = "https://www.youtube.com/watch?v=n--eh4O09Ys";

	public static final String URL_RESOURCE = "http://www.google.com";

	private static boolean taskNeeded = true;
	private static boolean gameSessionNotNeeded = false;

	private static Type getRandomTaskType() {
		Type[] types = Type.values();
		//return types[random.nextInt(types.length)];
		return Type.INPUT;
	}

	private static HelpType getRandomGpsHelpType() {
		HelpType[] helpTypes = HelpType.values();
		return helpTypes[random.nextInt(helpTypes.length)];
	}

	private static FailingType getRandomFailingType() {
		FailingType[] failingTypes = FailingType.values();
		return failingTypes[random.nextInt(failingTypes.length)];
	}

	private static int getRandomScore() {
		return random.nextInt(61) + 10;
	}

	private static int getRandomRetryNumber() {
		return random.nextInt(3) + 1;
	}

	private static int getRandomFailPenalty(int maxScore, int retryNumber) {
		int maxPenalty = maxScore / (retryNumber + 1) - 1;
		return random.nextInt(maxPenalty) + 1;
	}

	private static ResourceType getRandomResourceType() {
		return ResourceType.values()[random.nextInt(ResourceType.values().length)];
	}

	private static String getResourceNameForType(ResourceType type) {
		switch (type) {
			case TEXT:
				return "SZÖVEG";
			case IMAGE:
				return "KÉP";
			case SOUND:
				return "HANG";
			case VIDEO:
				return "VIDEÓ";
			case URL:
				return "URL";
			default:
				throw new IllegalArgumentException("Unknown resource type: " + type.name());
		}
	}

	private static String getResourceDataForType(ResourceType type) {
		switch (type) {
			case TEXT:
				return TEXT_RESOURCE;
			case IMAGE:
				return IMAGE_RESOURCE_URL;
			case SOUND:
				return SOUND_RESOURCE_URL;
			case VIDEO:
				return VIDEO_RESOURCE_URL;
			case URL:
				return URL_RESOURCE;
			default:
				throw new IllegalArgumentException("Unknown resource type: " + type.name());
		}
	}

	private static String getRandomResources() {
		StringBuilder resourceSb = new StringBuilder();
		resourceSb.append("[");

		int numOfResources = random.nextInt(6);

		for (int i = 0; i < numOfResources; i++) {
			if (i != 0) {
				resourceSb.append(", ");
			}
			resourceSb.append("{");
			ResourceType randomType = getRandomResourceType();
			resourceSb.append("\"title\": \"" + getResourceNameForType(randomType) + "\"");
			resourceSb.append(", \"data\": \"" + getResourceDataForType(randomType) + "\"");
			resourceSb.append(", \"resourceType\": " + randomType.getId());
			resourceSb.append("}");
		}

		resourceSb.append("]");
		return resourceSb.toString();
	}

	public static String getMockResponseForClass(Class<? extends NetworkResponse> clazz) {
		/*
        if (clazz == GetGameSessionsResponse.class) {
			return getGetGameSessionsMockResponse();
		}
		
		*/

		if (clazz == GetGameForDeviceResponse.class) {
			return getGetGameForDeviceMockResponse();
		}

		if (clazz == LoginResponse.class) {
			return getCoordinatorLoginMockResponse();
		}

		if (clazz == TaskStartResponse.class) {
			return getTaskStartMockResponse();
		}

		if (clazz == TaskDoneResponse.class) {
			return getTaskDoneMockResponse();
		}

		if (clazz == GetTaskResponse.class) {
			return getGetTaskMockResponse();
		}

		if (clazz == EmptyResponse.class) {
			return getEmptyMockResponse();
		}

		return null;
	}

	private static String getGetGameForDeviceMockResponse() {
		String response;

		if (gameSessionNotNeeded) {
			response = "{}";
		} else {
			StringBuilder sb = new StringBuilder();

			sb.append("{");

			sb.append("\"deviceId\":" + 123456);
			sb.append(", \"roleId\":" + 9);
			sb.append(", \"playerId\":" + 2);

			sb.append(", \"gameSession\": {");

			sb.append("\"id\":" + 3);
			sb.append(", \"gameId\":" + 9);
			sb.append(", \"gameName\":\"Teszt játék\"");
			sb.append(", \"teamName\":\"The expandables\"");
			sb.append(", \"isStarted\":false");
			sb.append(", \"isFinished\":false");

			sb.append("}");

			sb.append("}");
			response = sb.toString();
		}

		gameSessionNotNeeded = false;
		taskNeeded = true;

		return response;
	}

	private static String getCoordinatorLoginMockResponse() {
		StringBuilder sb = new StringBuilder();

		sb.append("{");
		sb.append("\"token\":\"sdg23t23z34zgkn34ltn34ltn54lk\"");
		sb.append("}");

		return sb.toString();
	}

	private static String getTaskStartMockResponse() {
		StringBuilder sb = new StringBuilder();

		boolean hasTimeLimit = random.nextBoolean();

		sb.append("{");
		sb.append("\"startTime\":\"2011-12-03T10:15:30\"");
		sb.append(", \"maxTime\":" + (hasTimeLimit ? 20 : 0));
		sb.append(", \"noTime\":" + (hasTimeLimit ? 50 : 0));
		sb.append("}");

		return sb.toString();
	}

	private static String getTaskDoneMockResponse() {
		StringBuilder sb = new StringBuilder();

		Date endDate = new Date(System.currentTimeMillis());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

		int score = random.nextInt(50) + 50;

		sb.append("{");
		sb.append("\"endTime\":\"" + format.format(endDate) + "\"");
		sb.append(", \"score\":" + score);
		sb.append(", \"totalScore\":" + score * (random.nextInt(3) + 1));
		sb.append("}");

		return sb.toString();
	}

	private static String getGetTaskMockResponse() {
		float noValidResponse = random.nextFloat();

		if (!taskNeeded && noValidResponse < 0.3f) {
			gameSessionNotNeeded = true;
			return "{}";
		}

		taskNeeded = false;

		StringBuilder sb = new StringBuilder();

		Type taskType = getRandomTaskType();

		boolean hasTimeLimit = random.nextBoolean();

		sb.append("{");

		sb.append("\"taskInstance\":{");

		sb.append("\"taskInstanceId\":" + 2);

		sb.append(", \"playerName\":" + "\"Pista\"");

		sb.append(", \"progress\":" + "\"notstarted\"");

		sb.append(", \"task\":{");

		sb.append("\"name\":\"" + taskType.name() + "\"");
		sb.append(", \"description\":\"" + taskType.name() + " típusú taszk\"");
		sb.append(", \"optional\":\"false\"");

		int maxScore = getRandomScore();
		int retryNumber = getRandomRetryNumber();

		sb.append(", \"maxScore\":" + maxScore);
		sb.append(", \"maxScoreTime\":" + (hasTimeLimit ? 20 : 0));
		sb.append(", \"noScoreTime\":" + (hasTimeLimit ? 50 : 0));

		FailingType failingType = getRandomFailingType();
		sb.append(", \"failingType\":\"" + failingType.getTypeString() + "\"");
		sb.append(", \"retryNumber\":" + retryNumber);
		sb.append(", \"failPenalty\":" + getRandomFailPenalty(maxScore, retryNumber));

		sb.append(", \"role\": {");
		sb.append("\"id\":" + 9);
		sb.append(", \"name\":\"Pró gyerek\"");
		sb.append("}");

		sb.append(", \"taskManagerName\":\"Főnök Ferenc\"");

		sb.append(", \"taskType\": {");
		sb.append("\"id\":" + taskType.getId());
		sb.append(", \"name\":\"" + taskType.name() + "\"");
		sb.append("}");

		sb.append(", \"resources\": " + getRandomResources());

		sb.append(", \"taskData\": {");

		sb.append("\"id\": " + 98);
		sb.append(", \"dataTypeId\": " + taskType.getId());

		switch (taskType) {
			case AR:
				sb.append(", \"frameMarkerId\":2");
				sb.append(", \"modelUri\":\"/public/models/shuttle3.obj\"");
				sb.append(", \"textureUri\":\"ajajj, baj van\"");
				break;
			case BEACON:
				sb.append(", \"uuid\":\"f7826da6-4fa2-4e98-8024-bc5b71e0893e\"");
				sb.append(", \"major\":23091");
				sb.append(", \"minor\":11610");
				break;
			case BRANCH:
				// Nothing to add
				break;
			case GPS:
				HelpType randomGpsHelpType = getRandomGpsHelpType();

				sb.append(", \"helpType\":\"" + randomGpsHelpType.getTypeString() + "\"");
				sb.append(", \"lat\":47.481506");
				sb.append(", \"lon\":19.0555556");
				sb.append(", \"distance\":150.0");
				break;
			case INPUT:
				sb.append(", \"data\":" + (/*random.nextBoolean()*/ false ? "\"alma\"" : "\"\""));
				break;
			case MERGE:
				// Nothing to add
				break;
			case NFC:
				sb.append(", \"data\":\"alma\"");
				break;
			case OFFLINE:
				// Nothing to add
				break;
			case QR:
				sb.append(", \"data\":\"alma\"");
				break;
			case QUIZ:
				int quizMaxScore;
				int quizRetryNumber;

				if (NetworkMockUtil.random.nextBoolean()) {
					sb.append(", \"type\":\"single\"");
					sb.append(", \"data\":[");

					// Question 1
					sb.append("{");

					sb.append("\"index\":1");
					sb.append(", \"question\":\"Mennyi 5 + 5?\"");

					sb.append(", \"answers\":[");
					sb.append("{\"text\":\"8\", \"right\":false}");
					sb.append(", {\"text\":\"9\", \"right\":false}");
					sb.append(", {\"text\":\"10\", \"right\":true}");
					sb.append(", {\"text\":\"11\", \"right\":false}");
					sb.append("]");

					quizMaxScore = getRandomScore();
					quizRetryNumber = getRandomRetryNumber();

					sb.append(", \"failingType\":\"" + getRandomFailingType().getTypeString() + "\"");
					sb.append(", \"score\":" + quizMaxScore);
					sb.append(", \"retryNumber\":" + quizRetryNumber);
					sb.append(", \"failPenalty\":" + getRandomFailPenalty(quizMaxScore, quizRetryNumber));

					sb.append("}");

					// Question 2
					sb.append(", {");

					sb.append("\"index\":2");
					sb.append(", \"question\":\"Hányadik kérdés ez?\"");

					sb.append(", \"answers\":[");
					sb.append("{\"text\":\"1\", \"right\":false}");
					sb.append(", {\"text\":\"2\", \"right\":true}");
					sb.append(", {\"text\":\"3\", \"right\":false}");
					sb.append(", {\"text\":\"4\", \"right\":false}");
					sb.append("]");

					quizMaxScore = getRandomScore();
					quizRetryNumber = getRandomRetryNumber();

					sb.append(", \"failingType\":\"" + getRandomFailingType().getTypeString() + "\"");
					sb.append(", \"score\":" + quizMaxScore);
					sb.append(", \"retryNumber\":" + quizRetryNumber);
					sb.append(", \"failPenalty\":" + getRandomFailPenalty(quizMaxScore, quizRetryNumber));

					sb.append("}");

					sb.append("]");
				} else {
					sb.append(", \"type\":\"multi\"");
					sb.append(", \"data\":[");

					// Question 1
					sb.append("{");

					sb.append("\"index\":1");
					sb.append(", \"question\":\"Mely(ek) a magánhangzók?\"");

					sb.append(", \"answers\":[");
					sb.append("{\"text\":\"A\", \"right\":true}");
					sb.append(", {\"text\":\"B\", \"right\":false}");
					sb.append(", {\"text\":\"C\", \"right\":false}");
					sb.append(", {\"text\":\"D\", \"right\":false}");
					sb.append(", {\"text\":\"E\", \"right\":true}");
					sb.append(", {\"text\":\"F\", \"right\":false}");
					sb.append("]");

					quizMaxScore = getRandomScore();
					quizRetryNumber = getRandomRetryNumber();

					sb.append(", \"failingType\":\"" + getRandomFailingType().getTypeString() + "\"");
					sb.append(", \"score\":" + quizMaxScore);
					sb.append(", \"retryNumber\":" + quizRetryNumber);
					sb.append(", \"failPenalty\":" + getRandomFailPenalty(quizMaxScore, quizRetryNumber));

					sb.append("}");

					// Question 2
					sb.append(", {");

					sb.append("\"index\":2");
					sb.append(", \"question\":\"Mely(ek) páratlan számok?\"");

					sb.append(", \"answers\":[");
					sb.append("{\"text\":\"1\", \"right\":true}");
					sb.append(", {\"text\":\"2\", \"right\":false}");
					sb.append(", {\"text\":\"3\", \"right\":true}");
					sb.append(", {\"text\":\"4\", \"right\":false}");
					sb.append("]");

					quizMaxScore = getRandomScore();
					quizRetryNumber = getRandomRetryNumber();

					sb.append(", \"failingType\":\"" + getRandomFailingType().getTypeString() + "\"");
					sb.append(", \"score\":" + quizMaxScore);
					sb.append(", \"retryNumber\":" + quizRetryNumber);
					sb.append(", \"failPenalty\":" + getRandomFailPenalty(quizMaxScore, quizRetryNumber));

					sb.append("}");

					// Question 3
					sb.append(", {");

					sb.append("\"index\":3");
					sb.append(", \"question\":\"Mely(ek) prím számok?\"");

					sb.append(", \"answers\":[");
					sb.append("{\"text\":\"6\", \"right\":false}");
					sb.append(", {\"text\":\"7\", \"right\":true}");
					sb.append(", {\"text\":\"8\", \"right\":false}");
					sb.append(", {\"text\":\"10\", \"right\":false}");
					sb.append("]");

					quizMaxScore = getRandomScore();
					quizRetryNumber = getRandomRetryNumber();

					sb.append(", \"failingType\":\"" + getRandomFailingType().getTypeString() + "\"");
					sb.append(", \"score\":" + quizMaxScore);
					sb.append(", \"retryNumber\":" + quizRetryNumber);
					sb.append(", \"failPenalty\":" + getRandomFailPenalty(quizMaxScore, quizRetryNumber));

					sb.append("}");

					sb.append("]");
				}

				break;
			default:
				break;
		}

		// End of taskData
		sb.append("}");
		// End of task
		sb.append("}");
		// End of taskInstance
		sb.append("}");
		// End of response
		sb.append("}");
		return sb.toString();
	}

	private static String getEmptyMockResponse() {
		return "{}";
	}

}
