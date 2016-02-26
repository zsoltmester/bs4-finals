package hu.bsmart.framework.gcm;

import com.google.android.gms.gcm.GcmListenerService;

import android.os.Bundle;

import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;

/**
 * An implementation of {@link GcmListenerService}
 * that handles incoming GCM messages, and propagates
 * them to the {@link GcmManager}.
 */
public class FrameworkGcmListenerService extends GcmListenerService {

	private static final String GCM_BUNDLE_PAYLOAD_KEY = "payload";

	private Logger logger;

	@Override
	public void onCreate() {
		super.onCreate();
		logger = LoggerProvider.createLoggerForClass(getClass());
	}

	@Override
	public void onMessageReceived(String from, Bundle data) {
		logger.d("GCM Intent arrived.");
		logBundle(data);
		String message = data.getString(GCM_BUNDLE_PAYLOAD_KEY);
		logger.d("Sender: " + from);
		logger.d("Message: " + message);
		if (message == null) {
			logger.e("Message is null!");
			return;
		}
		GcmManager.getInstance().dispatchPushMessage(this, message);
	}

	private void logBundle(Bundle bundle) {
		for (String key : bundle.keySet()) {
			Object value = bundle.get(key);
			if (value == null) {
				logger.d(String.format("%s: null", key));
			} else {
				logger.d(String.format("%s: %s (%s)", key, value.toString(), value.getClass().getName()));
			}
		}

	}
}
