package hu.bsmart.framework.communication.networkhelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import hu.bsmart.framework.communication.data.AdminRight;
import hu.bsmart.framework.communication.data.DataObject.ValidationException;
import hu.bsmart.framework.communication.data.FailingType;
import hu.bsmart.framework.communication.data.Progress;
import hu.bsmart.framework.communication.data.ResourceType;
import hu.bsmart.framework.communication.data.TaskData;
import hu.bsmart.framework.communication.data.taskdatatype.GpsTaskData.HelpType;
import hu.bsmart.framework.communication.data.taskdatatype.QuizTaskData.QuizType;
import hu.bsmart.framework.communication.deserializer.AdminRightDeserializer;
import hu.bsmart.framework.communication.deserializer.FailingTypeDeserializer;
import hu.bsmart.framework.communication.deserializer.GpsHelpTypeDeserializer;
import hu.bsmart.framework.communication.deserializer.ProgressDeserializer;
import hu.bsmart.framework.communication.deserializer.QuizTypeDeserializer;
import hu.bsmart.framework.communication.deserializer.ResourceTypeDeserializer;
import hu.bsmart.framework.communication.deserializer.TaskDataDeserializer;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;

/**
 * Helper class for transparent communication over the network
 * <p/>
 * This class is a singleton class, to get an instance, call
 * {@code NetworkHelper.getInstance()}.
 */
public class NetworkHelper {

	/**
	 * Name of the HandlerThread for handling network operations.
	 */
	private static final String HANDLER_THREAD_NAME = "NETWORK_HELPER";

	/**
	 * Timeout in milliseconds for establishing the connection
	 */
	private static final int CONNECTION_TIMEOUT = 5000;

	/**
	 * Key for the {@link NetworkRequest} object when sending network requests
	 * with the {@link Handler}
	 */
	private static final String EXTRA_REQUEST = "EXTRA_REQUEST";

	/**
	 * Key for the request code string when sending network requests with the
	 * {@link Handler}
	 */
	private static final String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE";

	/**
	 * Key for the {@link NetworkResponse} class when sending network requests
	 * with the {@link Handler}
	 */
	private static final String EXTRA_RESPONSE_CLASS = "EXTRA_RESPONSE_CLASS";

	/**
	 * Key for the {@link NetworkResultListener} object when sending network
	 * requests with the {@link Handler}
	 */
	private static final String EXTRA_LISTENER = "EXTRA_LISTENER";

	/**
	 * @deprecated Use {@link #SERVER_URL_SECURE} instead.
	 * URL of the Server
	 */
	@Deprecated
	private static final String SERVER_URL = "http://vm.ik.bme.hu:12097";

	/**
	 * Secure URL of the Server
	 */
	private static final String SERVER_URL_SECURE = "https://vm.ik.bme.hu:12233";

	/**
	 * Only instance of {@code NetworkHelper}.
	 */
	private static NetworkHelper instance;

	/**
	 * Logger for logging the operations (makes debug easier)
	 */
	private Logger logger;

	/**
	 * JSON serializer / deserializer
	 */
	private Gson gson;

	/**
	 * Handler object for processing requests in background
	 */
	private Handler requestHandler;

	/**
	 * {@link OperationMode} of the NetworkHelper
	 */
	private OperationMode operationMode = OperationMode.NORMAL;

	/**
	 * String representation of the last request that was going to be sent.
	 * Useful for testing and debugging.
	 */
	private String lastRequest;

	/**
	 * String representation of the last answer that was received.
	 */
	private String lastResponse;

	/**
	 * Private constructor that prevents direct instantiation, and defends
	 * against creating two instances by reflection.
	 */
	private NetworkHelper() {
		if (instance != null) {
			throw new IllegalAccessError("Only one instance is permitted!");
		}

		logger = LoggerProvider.createLoggerForClass(getClass());
		logger.d("Creating new NetworkHelper...");

		gson = createGson();
		requestHandler = createRequestHandler();
	}

	/**
	 * Gets the only instance of {@code NetworkHelper}.
	 *
	 * @return the {@code NetworkHelper} instance
	 */
	public static NetworkHelper getInstance() {
		if (instance == null) {
			instance = new NetworkHelper();
		}

		return instance;
	}

	/**
	 * Creates a Gson object for deserialization.
	 *
	 * @return
	 */
	private Gson createGson() {
		GsonBuilder builder = new GsonBuilder();

		builder.registerTypeAdapter(TaskData.class, new TaskDataDeserializer());
		builder.registerTypeAdapter(FailingType.class, new FailingTypeDeserializer());
		builder.registerTypeAdapter(ResourceType.class, new ResourceTypeDeserializer());
		builder.registerTypeAdapter(HelpType.class, new GpsHelpTypeDeserializer());
		builder.registerTypeAdapter(QuizType.class, new QuizTypeDeserializer());
		builder.registerTypeAdapter(AdminRight.class, new AdminRightDeserializer());
		builder.registerTypeAdapter(Progress.class, new ProgressDeserializer());
		builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return builder.create();
	}

	/**
	 * Creates a {@code RequestHandler} instance that can handle network
	 * requests.
	 *
	 * @return
	 */
	private RequestHandler createRequestHandler() {
		HandlerThread ht = new HandlerThread(HANDLER_THREAD_NAME);
		ht.start();
		RequestHandler requestHandler = new RequestHandler(ht.getLooper());
		return requestHandler;
	}

	/**
	 * Gets the operation mode of this {@code NetworkHelper}.
	 *
	 * @return
	 */
	public OperationMode getOperationMode() {
		return this.operationMode;
	}

	/**
	 * Sets the operation mode of this {@code NetworkHelper}.
	 *
	 * @param operationMode
	 */
	public void setOperationMode(OperationMode operationMode) {
		this.operationMode = operationMode;
	}

	/**
	 * Gets the last request that was going to be sent.
	 * <p/>
	 * Will return {@code null} if no request was initiated yet.
	 *
	 * @return
	 */
	public String getLastRequest() {
		return lastRequest;
	}

	/**
	 * Gets the last response that arrived.
	 * <p/>
	 * Will return {@code null} if no valid response arrived so far.
	 *
	 * @return
	 */
	public String getLastResponse() {
		return lastResponse;
	}

	/**
	 * Starts a network request.
	 *
	 * @param requestCode      request code for identifying the request
	 * @param request          the {@link NetworkRequest} that needs to be sent
	 * @param responseListener {@link NetworkResultListener} object for getting notified
	 *                         about the result
	 */
	public void startRequest(String requestCode, NetworkRequest request, NetworkResultListener responseListener) {
		if (requestCode == null) {
			throw new IllegalArgumentException("RequestCode must be set!");
		}

		if (request == null) {
			throw new IllegalArgumentException("Request cannot be NULL!");
		}

		if (responseListener == null) {
			throw new IllegalArgumentException(
					"You have to provide a ResponseListener");
		}

		Bundle data = new Bundle();
		data.putString(EXTRA_REQUEST_CODE, requestCode);
		data.putSerializable(EXTRA_REQUEST, request);
		data.putSerializable(EXTRA_RESPONSE_CLASS, request.getResponseClass());
		data.putSerializable(EXTRA_LISTENER, responseListener);

		Message msg = requestHandler
				.obtainMessage(RequestHandler.MSG_SEND_REQUEST);
		msg.setData(data);
		requestHandler.sendMessage(msg);
	}

	/**
	 * @param request the NetworkRequest that we want to send to the server
	 * @return returns the HTTP/HTTPS compliant HttpClient object
	 *
	 * @see HttpURLConnection
	 * @deprecated Use {@link #createSecureConnectionForRequest(NetworkRequest)} instead.
	 * Creates a HTTP URL Connection that executes the given {@link NetworkRequest}.
	 */
	@Deprecated
	private HttpURLConnection createConnectionForRequest(NetworkRequest request) throws IOException {
		URL url = new URL(SERVER_URL + request.getRequestUrl());

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIMEOUT);

		String queryString = createParameterStringForRequest(request);
		connection.setFixedLengthStreamingMode(queryString.getBytes().length);

		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		OutputStream os = connection.getOutputStream();
		PrintWriter pw = new PrintWriter(os);

		pw.print(queryString);
		pw.flush();
		pw.close();

		connection.connect();
		return connection;
	}

	/**
	 * Creates a HTTPS URL Connection that executes the given {@link NetworkRequest}.
	 *
	 * @param request the NetworkRequest that we want to send to the server
	 * @return returns the HTTPS compliant HttpClient object
	 *
	 * @see HttpsURLConnection
	 */
	private HttpsURLConnection createSecureConnectionForRequest(NetworkRequest request) throws IOException {
		URL url = new URL(SERVER_URL_SECURE + request.getRequestUrl());

		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIMEOUT);

		String queryString = createParameterStringForRequest(request);
		connection.setFixedLengthStreamingMode(queryString.getBytes().length);

		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		OutputStream os = connection.getOutputStream();
		PrintWriter pw = new PrintWriter(os);

		pw.print(queryString);
		pw.flush();
		pw.close();

		connection.connect();
		return connection;
	}

	/**
	 * Creates a query string for the parameters of the request
	 *
	 * @param request the request whose query string is needed
	 * @return
	 *
	 * @throws UnsupportedEncodingException
	 */
	private String createParameterStringForRequest(NetworkRequest request) throws UnsupportedEncodingException {
		List<Pair<String, String>> params = request.getRequestParams();

		if (params == null || params.size() == 0) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		int paramCount = params.size();

		Pair<String, String> actualParam;
		for (int i = 0; i < paramCount; i++) {
			actualParam = params.get(i);
			builder.append(actualParam.first + "=" + URLEncoder.encode(actualParam.second, "UTF-8"));
			if (i != paramCount - 1) {
				builder.append("&");
			}
		}

		return builder.toString();
	}

	private <T extends NetworkMessage> T createAndValidateNetworkMessageObject(String messageString,
			Class<T> messageClass) throws JsonSyntaxException, ValidationException {
		NetworkMessage networkMessage = gson.fromJson(messageString, messageClass);
		networkMessage.validate();

		// We have a valid NetworkMessage
		return (T) networkMessage;
	}

	private NetworkResponse createResponseObject(String responseString, Class<? extends NetworkResponse> responseClass)
			throws JsonSyntaxException, ValidationException {
		return createAndValidateNetworkMessageObject(responseString, responseClass);
	}

	/**
	 * Enumeration describing the operation mode of the {@code NetworkHelper}.
	 */
	public enum OperationMode {
		/**
		 * Normal mode means the requests are sent to the server, and the
		 * responses are received normally.
		 * <p/>
		 * This is the default mode.
		 */
		NORMAL,

		/**
		 * Test mode means the requests are not sent to the server. This mode is
		 * good for testing, because You can see the created JSON message in the
		 * LogCat window, logged as debug message.
		 * <p/>
		 * In this mode, every message will trigger a success event on the
		 * {@link NetworkResultListener}, but the {@link NetworkResponse} object
		 * will be a mocked answer.
		 */
		TEST
	}

	private class RequestHandler extends Handler {
		/**
		 * Message ID for sending network request through the {@link Handler}
		 */
		static final int MSG_SEND_REQUEST = 9;

		public RequestHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_SEND_REQUEST:
					processRequestData(new RequestData(msg.getData()));
					break;
			}
		}

		private void processRequestData(RequestData requestData) {
			lastRequest = requestData.request.toString();
			logger.d("Request: " + lastRequest);

			try {
				requestData.request.validate();
			} catch (ValidationException e) {
				e.printStackTrace();
				lastResponse = exceptionStacktraceToString(e);
				requestData.listener.onFail(requestData.requestCode,
						new CommunicationError(CommunicationError.Reason.INVALID_REQUEST, e.getMessage()));
				return;
			}

			if (operationMode == OperationMode.TEST) {
				logger.d("TEST MODE, trying to get a mock response.");
				String mockResponseString = NetworkMockUtil.getMockResponseForClass(requestData.responseClass);
				if (mockResponseString == null) {
					logger.e("TEST MODE: no mock response, calling onFail!");
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.COULD_NOT_RETRIEVE_RESPONSE,
									"Mock response is not prepared for class: " + requestData.responseClass));
					return;
				}

				lastResponse = "TEST: " + mockResponseString;

				logger.d("Mock response: " + mockResponseString);
				try {
					NetworkResponse mockedResponse =
							createResponseObject(mockResponseString, requestData.responseClass);
					requestData.listener.onSuccess(requestData.requestCode, mockedResponse);
					return;
				} catch (Exception e) {
					e.printStackTrace();
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.COULD_NOT_RETRIEVE_RESPONSE,
									"Mock response is bad for class: " + requestData.responseClass));
					return;
				}
			}

			HttpsURLConnection connection = null;
			try {
				try {
					connection = createSecureConnectionForRequest(requestData.request);
				} catch (IOException exception) {
					exception.printStackTrace();
					lastResponse = exceptionStacktraceToString(exception);
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.COULD_NOT_CREATE_CONNECTION,
									exception.getMessage()));
					return;
				}

				if (connection == null) {
					logger.d("Connection is null!");
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.COULD_NOT_CREATE_CONNECTION,
									"Connection is null!"));
					return;
				}

				try {
					if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
						lastResponse = "HTTP STATUS: " + connection.getResponseCode();
						logger.e(lastResponse);
						requestData.listener.onFail(requestData.requestCode,
								new CommunicationError(CommunicationError.Reason.BAD_STATUS_CODE, lastResponse));
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
					lastResponse = exceptionStacktraceToString(e);
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.COULD_NOT_RETRIEVE_RESPONSE,
									e.getMessage()));
					return;
				}

				String responseString = null;
				InputStream is = null;
				InputStreamReader isr = null;
				BufferedReader reader = null;

				try {
					String newLineChar = System.getProperty("line.separator");
					StringBuilder builder = new StringBuilder();
					is = connection.getInputStream();
					isr = new InputStreamReader(is, "UTF-8");
					reader = new BufferedReader(isr);
					for (String line; (line = reader.readLine()) != null; ) {
						builder.append(line).append(newLineChar);
					}
					responseString = builder.toString();
				} catch (UnsupportedEncodingException exception) {
					exception.printStackTrace();
					lastResponse = exceptionStacktraceToString(exception);
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.MISSING_ENCODING, exception.getMessage()));
					return;
				} catch (IOException exception) {
					exception.printStackTrace();
					lastResponse = exceptionStacktraceToString(exception);
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.COULD_NOT_RETRIEVE_RESPONSE,
									exception.getMessage()));
					return;
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (isr != null) {
						try {
							isr.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				if (TextUtils.isEmpty(responseString)) {
					lastResponse = "NULL";
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.COULD_NOT_RETRIEVE_RESPONSE,
									"Response is empty!"));
					return;
				}

				lastResponse = responseString;
				logger.d("Response: " + lastResponse);

				try {
					NetworkResponse networkResponse = createResponseObject(responseString, requestData.responseClass);

					// We have a valid NetworkResponse
					requestData.listener.onSuccess(requestData.requestCode, networkResponse);
					return;
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.BAD_JSON_RESPONSE, e.getMessage()));
					return;
				} catch (ValidationException e) {
					e.printStackTrace();
					requestData.listener.onFail(requestData.requestCode,
							new CommunicationError(CommunicationError.Reason.INVALID_RESPONSE, e.getMessage()));
					return;
				}
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}

		}

		private String exceptionStacktraceToString(Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			ps.close();
			return baos.toString();
		}

		private class RequestData {
			public final String requestCode;
			public final NetworkRequest request;
			public final NetworkResultListener listener;
			public final Class<? extends NetworkResponse> responseClass;

			@SuppressWarnings("unchecked")
			public RequestData(Bundle messageData) {
				checkMessageData(messageData);

				requestCode = messageData.getString(EXTRA_REQUEST_CODE);
				request = (NetworkRequest) messageData
						.getSerializable(EXTRA_REQUEST);
				listener = (NetworkResultListener) messageData
						.getSerializable(EXTRA_LISTENER);
				responseClass = (Class<? extends NetworkResponse>) messageData
						.getSerializable(EXTRA_RESPONSE_CLASS);

			}

			/**
			 * Checks that the given {@link Bundle} contains everything needed
			 * to create a {@code RequestData} instance.
			 *
			 * @param messageData
			 * @throws IllegalArgumentException if anything is missing
			 * @throws NullPointerException     if {@code messageData} or any value is null
			 */
			private void checkMessageData(Bundle messageData) {
				if (messageData == null) {
					throw new NullPointerException(
							"Message data cannot be NULL!");
				}

				if (!messageData.containsKey(EXTRA_REQUEST_CODE)) {
					throw new IllegalArgumentException(
							"Request code is missing!");
				}

				if (!messageData.containsKey(EXTRA_REQUEST)) {
					throw new IllegalArgumentException("Request is missing!");
				}

				if (!messageData.containsKey(EXTRA_LISTENER)) {
					throw new IllegalArgumentException("Listener is missing!");
				}

				if (!messageData.containsKey(EXTRA_RESPONSE_CLASS)) {
					throw new IllegalArgumentException(
							"Response class is missing!");
				}

			}
		}
	}

	/**
	 * Creates absolute URL from relative URL.
	 *
	 * @param relativeUrl relative URL
	 * @return the absolute URL or null if the relative URL was null
	 */
	public String absoluteFromRelativeUrl(String relativeUrl) {
		if (relativeUrl == null) {
			return null;
		}
		return SERVER_URL_SECURE + (relativeUrl.startsWith("/") ? relativeUrl : ("/" + relativeUrl));
	}

}
