package hu.bsmart.framework.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.bsmart.framework.R;
import hu.bsmart.framework.communication.data.DataObject;
import hu.bsmart.framework.gcm.pushmessage.PushMessage;
import hu.bsmart.framework.gcm.pushmessage.PushMessageDeserializer;
import hu.bsmart.framework.util.NotificationUtil;

/**
 * Helper class for GCM related operations.
 * <p/>
 * This class receives GCM messages, converts them to {@link PushMessage}
 * objects, and dispatches the objects to the listeners.
 * <p/>
 * If no listener handles a push message, the GcmManager shows
 * a notification to the user. (This typically happens when
 * the application is in the background.) Clicking on
 * the notification brings the application to front.
 * <p/>
 * In case there are no registered listeners, the GcmManager
 * can store {@link PushMessage} objects, and they can be replayed
 * to the next listener that registers. For details,
 * see {@link GcmManager#setReplayStrategy(ReplayStrategy)}.
 * The default replay srategy is {@link hu.bsmart.framework.gcm.GcmManager.ReplayStrategy#NONE NONE}.
 * <p/>
 * <pre>
 * To make GCM work, you have to define these permissions in the application's manifest:
 *  - android.permission.INTERNET
 *  - android.permission.WAKE_LOCK
 *  - com.google.android.c2dm.permission.RECEIVE
 *  - packageName + ".permission.C2D_MESSAGE"
 *
 * You also need a GcmReceiver:
 *
 * &lt;receiver
 *     android:name="com.google.android.gms.gcm.GcmReceiver"
 *     android:exported="true"
 *     android:permission="com.google.android.c2dm.permission.SEND"&gt;
 *         &lt;intent-filter&gt;
 *             &lt;action android:name="com.google.android.c2dm.intent.RECEIVE" /&gt;
 *             &lt;category android:name="[packageName]" /&gt;
 *         &lt;/intent-filter&gt;
 * &lt;/receiver&gt;
 *
 * In addition to that, you need to register {@link FrameworkGcmListenerService}
 * in the application's manifest. The Service needs an intent filter with
 * the following action: "com.google.android.c2dm.intent.RECEIVE". See the example:
 *
 * &lt;service
 *     android:name="com.google.android.gms.gcm.FrameworkGcmListenerService"
 *     android:exported="false"&gt;
 *         &lt;intent-filter&gt;
 *             &lt;action android:name="com.google.android.c2dm.intent.RECEIVE" /&gt;
 *         &lt;/intent-filter&gt;
 * &lt;/service&gt;
 *
 * The last thing you need is to register {@link FrameworkInstanceIdListenerService}
 * in the application's manifest, like this example:
 *
 * &lt;service
 *     android:name="com.google.android.gms.gcm.FrameworkInstanceIdListenerService"
 *     android:exported="false"&gt;
 *         &lt;intent-filter&gt;
 *             &lt;action android:name="com.google.android.gms.iid.InstanceID" /&gt;
 *         &lt;/intent-filter&gt;
 * &lt;/service&gt;
 *
 * </pre>
 */
public class GcmManager {

    private static GcmManager instance = null;

    private static final String GCM_PREFERENCES = "GCM_PREFERENCES";
    private static final String GCM_TOKEN_PREFERENCE_KEY = "GCM_TOKEN_PREFERENCE_KEY";

    private static final int GCM_NOTIFICATION_ID =
	   private static final int GCM_PENDING_INTENT_REQUEST_CODE = 99;

    private static SharedPreferences gcmPreferences;

    private HandlerThread gcmHandlerThread;
    private Handler uiHandler;
    private Handler gcmHandler;
    private Gson pushMessageGson;

    private final List<PushMessageListener> pushMessageListeners = new ArrayList<>();

    /**
     * Controls the replay strategy
     */
    private ReplayStrategy replayStrategy = ReplayStrategy.NONE;

    /**
     * PushMessages that arrived when the app was in the background.
     */
    private List<PushMessage> unhandledPushMessageList = new ArrayList<>();

    /**
     * Returns the only instance of GcmManager.
     *
     * @return the GcmManager instance
     */
    public static GcmManager getInstance() {
        if (instance == null) {
            instance = new GcmManager();
        }
        return instance;
    }

    /**
     * Replay strategy for unhandled GCM messages.
     */
    public enum ReplayStrategy {
        /**
         * Don't replay GCM messages.
         */
        NONE,
        /**
         * Only the last message should be replayed.
         */
        LAST_ONE,
        /**
         * Every unhandled GCM messages should be replayed.
         */
        ALL
    }

    /**
     * Runnable that starts the GCM registration process
     */
    private class GcmRegistrationRunnable implements Runnable {
        private Context context;
        private GcmRegistrationListener listener;

        public GcmRegistrationRunnable(Context context, GcmRegistrationListener listener) {
            if (context == null) {
                throw new NullPointerException("Context cannot be null!");
            }

            if (listener == null) {
                throw new NullPointerException("GcmRegistrationListener cannot be null!");
            }

            this.context = context;
            this.listener = listener;
        }

        @Override
        public void run() {
            InstanceID instanceId = InstanceID.getInstance(context);
            final String instanceIdString = instanceId.getId();

            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onInstanceIdAvailable(instanceIdString);
                }
            });

            final String gcmToken = getTheToken(instanceId);
            if (gcmToken == null) {
                // We failed to obtain a token
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onGcmRegistrationFailed();
                    }
                });
            } else {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onGcmRegistrationSucceeded(gcmToken);
                    }
                });
            }
        }

        private String getTheToken(@NonNull InstanceID instanceId) {
            // If we have a token, then no need to request another one
            String gcmToken = getGcmToken(context);
            if (gcmToken == null) {
                try {
                    gcmToken = instanceId.getToken(context.getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    if (gcmToken == null) {
                        throw new IOException("InstanceID gave null token!");
                    }
                    storeGcmToken(context, gcmToken);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return gcmToken;
        }
    }


    private GcmManager() {
        gcmHandlerThread = new HandlerThread("GCM");
        gcmHandlerThread.start();
        gcmHandler = new Handler(gcmHandlerThread.getLooper());
        uiHandler = new Handler(Looper.getMainLooper());
        pushMessageGson = createGson();
    }

    /**
     * Creates a Gson object for push message deserialization.
     *
     * @return the Gson for push messages
     */
    private Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(PushMessage.class, new PushMessageDeserializer());
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return builder.create();
    }

    /**
     * Starts a GCM registration.
     *
     * @param context  Android context
     * @param listener listener that will get the result
     */
    public void startGcmRegistration(@NonNull Context context, @NonNull GcmRegistrationListener listener) {
        gcmHandler.post(new GcmRegistrationRunnable(context, listener));
    }

    /**
     * Registers a {@code PushMessageListener} for getting
     * notifications about new push messages.
     *
     * @param listener the listener that wants to register
     */
    public void registerPushMessageListener(@NonNull final PushMessageListener listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must not be null!");
        }

        synchronized (pushMessageListeners) {
            pushMessageListeners.add(listener);
        }

        final List<PushMessage> messagesToDispatch = unhandledPushMessageList;
        if (replayStrategy != ReplayStrategy.NONE && messagesToDispatch.size() > 0) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    unhandledPushMessageList = new ArrayList<>();
                    if (replayStrategy == ReplayStrategy.LAST_ONE) {
                        listener.onPushMessageArrived(messagesToDispatch.get(messagesToDispatch.size() - 1));
                    } else if (replayStrategy == ReplayStrategy.ALL) {
                        for (PushMessage pushMessage : messagesToDispatch) {
                            listener.onPushMessageArrived(pushMessage);
                        }
                    }
                }
            });
        }
    }

    /**
     * Removes a {@code PushMessageListener} for getting
     * notifications about new push messages.
     *
     * @param listener the listener to unregister
     */
    public void unregisterPushMessageListener(@NonNull PushMessageListener listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must not be null!");
        }

        synchronized (pushMessageListeners) {
            pushMessageListeners.remove(listener);
        }
    }

    /**
     * Dispatches a GCM message to the registered listeners.
     * <p/>
     * If there isn't any listeners, or none of the listeners
     * handle the message, a notification will be shown on
     * the notification bar.
     *
     * @param context    Android context
     * @param rawMessage the raw message
     */
    void dispatchPushMessage(final Context context, @NonNull final String rawMessage) {
        try {
            final PushMessage pushMessage = createPushMessageFromRawString(rawMessage);
            // Now we have a valid PushMessage object
            synchronized (pushMessageListeners) {
                if (pushMessageListeners.size() == 0) {
                    showNotificationForPushMessage(context, pushMessage);
                    if (replayStrategy == ReplayStrategy.LAST_ONE) {
                        unhandledPushMessageList = new ArrayList<>();
                        unhandledPushMessageList.add(pushMessage);
                    } else if (replayStrategy == ReplayStrategy.ALL) {
                        unhandledPushMessageList.add(pushMessage);
                    }
                } else {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            boolean handled = false;
                            for (final PushMessageListener listener : pushMessageListeners) {
                                handled |= listener.onPushMessageArrived(pushMessage);
                            }

                            if (!handled) {
                                showNotificationForPushMessage(context, pushMessage);
                            }
                        }
                    });
                }
            }
        } catch (JsonSyntaxException | DataObject.ValidationException exception) {
            exception.printStackTrace();
            // We have to notify the listeners or the user about the error
            final String errorMessage = exception.getMessage();
            synchronized (pushMessageListeners) {
                if (pushMessageListeners.size() == 0) {
                    showNotificationForError(context, errorMessage);
                } else {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            boolean handled = false;
                            for (final PushMessageListener listener : pushMessageListeners) {
                                handled |= listener.onPushMessageError(errorMessage);
                            }

                            if (!handled) {
                                showNotificationForError(context, errorMessage);
                            }
                        }
                    });
                }
            }
        }

    }

    private PushMessage createPushMessageFromRawString(String rawPushMessage) throws JsonSyntaxException, DataObject.ValidationException {
        PushMessage result = pushMessageGson.fromJson(rawPushMessage, PushMessage.class);
        result.validate();
        return result;
    }

    private void showNotificationForPushMessage(Context context, PushMessage pushMessage) {
        PendingIntent pendingIntent = getAppPendingIntent(context);
        if (pendingIntent == null) {
            return;
        }
        NotificationUtil.showNotification(context, GCM_NOTIFICATION_ID,
                NotificationUtil.createNotification(context, pushMessage.getEventSummary(context), pushMessage.getEventDetails(context), pendingIntent));
    }

    private void showNotificationForError(Context context, String errorMessage) {
        PendingIntent pendingIntent = getAppPendingIntent(context);
        if (pendingIntent == null) {
            return;
        }
        NotificationUtil.showNotification(context, GCM_NOTIFICATION_ID, NotificationUtil.createNotification(context, context.getString(R.string.push_message_error_title), errorMessage, pendingIntent));
    }

    private PendingIntent getAppPendingIntent(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent appIntent = packageManager.getLaunchIntentForPackage(context.getApplicationInfo().packageName);
        if (appIntent == null) {
            return null;
        }
        appIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context.getApplicationContext(), GCM_PENDING_INTENT_REQUEST_CODE, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Sets the replay strategy. It also clears previously
     * stored push messages that are not necessary for
     * the new strategy.
     */
    public void setReplayStrategy(ReplayStrategy replayStrategy) {
        if (this.replayStrategy != replayStrategy) {
            this.replayStrategy = replayStrategy;
            switch (replayStrategy) {
                case NONE:
                    unhandledPushMessageList = new ArrayList<>();
                    break;
                case LAST_ONE:
                    PushMessage lastOne = unhandledPushMessageList.size() > 0 ? unhandledPushMessageList.get(unhandledPushMessageList.size() - 1) : null;
                    unhandledPushMessageList = new ArrayList<>();
                    if (lastOne != null) {
                        unhandledPushMessageList.add(lastOne);
                    }
                    break;
                case ALL:
                    break;
            }
        }
    }

    /**
     * Stores the GCM token in the application's preferences.
     *
     * @param context  Android context
     * @param gcmToken the GCM token to store
     */
    void storeGcmToken(Context context, String gcmToken) {
        getGcmPreferencesEditor(context).putString(GCM_TOKEN_PREFERENCE_KEY, gcmToken).apply();
    }

    /**
     * Removes the GCM token from the application's preferences.
     *
     * @param context Android context
     */
    void clearGcmToken(Context context) {
        getGcmPreferencesEditor(context).remove(GCM_TOKEN_PREFERENCE_KEY).apply();
        synchronized (pushMessageListeners) {
            for (PushMessageListener listener : pushMessageListeners) {
                listener.onGcmTokenChanged();
            }
        }
    }

    /**
     * Retrieves the GCM token from the application's preferences (if any).
     *
     * @param context Android context
     * @return the GCM token, or {@code null} if there is no token
     */
    String getGcmToken(Context context) {
        return getGcmTokenPreferences(context).getString(GCM_TOKEN_PREFERENCE_KEY, null);
    }

    private SharedPreferences getGcmTokenPreferences(Context context) {
        if (gcmPreferences == null) {
            synchronized (GcmManager.class) {
                if (gcmPreferences == null) {
                    gcmPreferences = context.getApplicationContext().getSharedPreferences(GCM_PREFERENCES, Context.MODE_PRIVATE);
                }
            }
        }

        return gcmPreferences;
    }

    private SharedPreferences.Editor getGcmPreferencesEditor(Context context) {
        return getGcmTokenPreferences(context).edit();
    }

}
