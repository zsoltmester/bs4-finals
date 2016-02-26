package hu.bsmart.framework.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

/**
 * Utility class for handling Notifications.
 */
public class NotificationUtil {

	private NotificationUtil() {
		throw new UnsupportedOperationException("Cannot instantiate utility class!");
	}

	/**
	 * Retrieves a {@link NotificationManager} instance.
	 *
	 * @param context the context
	 * @return the notification manager
	 *
	 * @throws NullPointerException if {@code context} is null
	 */
	private static NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * Shows a notification.
	 *
	 * @param context        Android context
	 * @param notificationId the notification ID
	 * @param notification   the notification
	 */
	public static void showNotification(Context context, int notificationId, Notification notification) {
		getNotificationManager(context).notify(notificationId, notification);
	}

	/**
	 * Cancels a {@link Notification}.
	 *
	 * @param context        the Android context
	 * @param notificationId the ID for the Notification
	 * @throws NullPointerException if {@code context} is null
	 */
	public static void cancelNotification(Context context, int notificationId) {
		getNotificationManager(context).cancel(notificationId);
	}

	/**
	 * Creates a notification. The notification uses the default notification sound.
	 *
	 * @param context                   Android context
	 * @param iconResource              drawable resource for the notification icon (0 for no icon)
	 * @param titleResource             title resource ID for the notification
	 * @param textResource              text resource ID for the notification
	 * @param notificationPendingIntent pending intent to fire when the user clicks on the notification
	 * @return the notification
	 *
	 * @throws NullPointerException if pendingIntent is null
	 */
	public static Notification createNotification(
			Context context, @DrawableRes int iconResource, @StringRes int titleResource, @StringRes int textResource,
			@NonNull PendingIntent notificationPendingIntent) {
		return createNotification(context, iconResource, context.getString(titleResource),
				context.getString(textResource), notificationPendingIntent);
	}

	/**
	 * Creates a notification. The notification uses the default notification sound.
	 *
	 * @param context                   Android context
	 * @param iconResource              drawable resource for the notification icon (0 for no icon)
	 * @param title                     title for the notification
	 * @param text                      text for the notification
	 * @param notificationPendingIntent pending intent to fire when the user clicks on the notification
	 * @return the notification
	 *
	 * @throws NullPointerException if pendingIntent, title, text is null
	 */
	public static Notification createNotification(
			Context context, @DrawableRes int iconResource, @NonNull String title, @NonNull String text,
			@NonNull PendingIntent notificationPendingIntent) {
		return createNotification(context, iconResource, title, text,
				RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null, notificationPendingIntent);
	}

	/**
	 * Creates a notification. The notification uses the default notification sound.
	 *
	 * @param context                   Android context
	 * @param titleResource             title resource ID for the notification
	 * @param textResource              text resource ID for the notification
	 * @param notificationPendingIntent pending intent to fire when the user clicks on the notification
	 * @return the notification
	 *
	 * @throws NullPointerException if pendingIntent is null
	 */
	public static Notification createNotification(
			Context context, @StringRes int titleResource, @StringRes int textResource,
			@NonNull PendingIntent notificationPendingIntent) {
		return createNotification(context, context.getString(titleResource), context.getString(textResource),
				notificationPendingIntent);
	}

	/**
	 * Creates a notification. The notification uses the default notification sound.
	 *
	 * @param context                   Android context
	 * @param title                     title for the notification
	 * @param text                      text for the notification
	 * @param notificationPendingIntent pending intent to fire when the user clicks on the notification
	 * @return the notification
	 *
	 * @throws NullPointerException if pendingIntent, title, text is null
	 */
	public static Notification createNotification(
			Context context, @NonNull String title, @NonNull String text,
			@NonNull PendingIntent notificationPendingIntent) {
		return createNotification(context, 0, title, text,
				RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null, notificationPendingIntent);
	}

	/**
	 * Creates a notification.
	 *
	 * @param context                   Android context
	 * @param iconResource              drawable resource for the notification icon (0 for no icon)
	 * @param titleResource             title resource ID for the notification
	 * @param textResource              text resource ID for the notification
	 * @param soundUri                  {@link Uri} of the sound to play when notifying
	 * @param vibratePattern            vibration pattern of the notification (can be null)
	 * @param notificationPendingIntent pending intent to fire when the user clicks on the notification
	 * @return the notification
	 *
	 * @throws NullPointerException if pendingIntent or soundUri is null
	 */
	public static Notification createNotification(
			Context context, @DrawableRes int iconResource, @StringRes int titleResource,
			@StringRes int textResource, @NonNull Uri soundUri, @Nullable long[] vibratePattern,
			@NonNull PendingIntent notificationPendingIntent) {
		return createNotification(context, iconResource, context.getString(titleResource),
				context.getString(textResource), soundUri, vibratePattern, notificationPendingIntent);
	}

	/**
	 * Creates a notification.
	 *
	 * @param context                   Android context
	 * @param iconResource              drawable resource for the notification icon (0 for no icon)
	 * @param title                     title for the notification
	 * @param text                      text for the notification
	 * @param soundUri                  {@link Uri} of the sound to play when notifying
	 * @param vibratePattern            vibration pattern of the notification (can be null)
	 * @param notificationPendingIntent pending intent to fire when the user clicks on the notification
	 * @return the notification
	 *
	 * @throws NullPointerException if pendingIntent, title, text, or soundUri is null
	 */
	public static Notification createNotification(
			@NonNull Context context, @DrawableRes int iconResource, String title, String text,
			@NonNull Uri soundUri, @Nullable long[] vibratePattern, @NonNull PendingIntent notificationPendingIntent) {

		if (notificationPendingIntent == null) {
			throw new NullPointerException("PendingIntent cannot be null for Notification!");
		}

		if (title == null) {
			throw new NullPointerException("Title cannot be null for Notification!");
		}

		if (text == null) {
			throw new NullPointerException("Text cannot be null for Notification!");
		}

		if (soundUri == null) {
			throw new NullPointerException("Sound URI cannot be null for Notification!");
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle(title).setContentText(text).setAutoCancel(true).setSound(soundUri)
				.setContentIntent(notificationPendingIntent);

		if (iconResource == 0) {
			iconResource = context.getApplicationInfo().icon;
		}

		builder.setSmallIcon(iconResource);

		if (vibratePattern != null) {
			builder.setVibrate(vibratePattern);
		}

		builder.setLights(Color.argb(255, 255, 255, 255), 2000, 3000);
		builder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(text)
				.setSummaryText(context.getString(context.getApplicationInfo().labelRes)));
		return builder.build();
	}

}