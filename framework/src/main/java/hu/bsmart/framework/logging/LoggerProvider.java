package hu.bsmart.framework.logging;

import android.support.annotation.NonNull;

/**
 * Provider class for creating logger and
 * controlling their global behaviour.
 */
public class LoggerProvider {
	/**
	 * Global state of logging (enabled/disabled)
	 */
	private static boolean globalLoggingEnabled = true;

	/**
	 * Disables all logging (all {@code Logger} instances will be disabled)
	 */
	public static void disableAllLogging() {
		globalLoggingEnabled = false;
	}

	/**
	 * Enables all logging (all {@code Logger} instances will be enabled)
	 */
	public static void enableAllLogging() {
		globalLoggingEnabled = true;
	}

	/**
	 * Returns the global enabled state of logging.
	 *
	 * @return {@code true} if {@link Logger}s should log, {@code false} if the shouldn't
	 */
	public static boolean isGlobalLoggingEnabled() {
		return globalLoggingEnabled;
	}

	/**
	 * This factory method creates a {@code Logger} with the given log tag and Verbose level logging.
	 *
	 * @param logTag log tag for this {@code Logger}, cannot be {@code null}
	 * @return the new {@code Logger} that has just been created
	 */
	@NonNull
	public static Logger createLoggerWithTag(@NonNull String logTag) {
		return new DefaultLogger(logTag);
	}

	/**
	 * This factory method creates a {@code Logger} with the given log tag and the given log level.
	 *
	 * @param logTag       the own log tag of this {@code Logger} instance
	 * @param loggingLevel the log level for this {@code Logger} instance
	 * @return the new {@code Logger} that has just been created
	 */
	@NonNull
	public static Logger createLoggerWithTagAndLevel(String logTag, Logger.LoggingLevel loggingLevel) {
		return new DefaultLogger(logTag, loggingLevel);
	}

	/**
	 * This factory method creates a {@code Logger} for the given {@code class}.
	 *
	 * @param clazz the class who needs the {@code Logger}: the log tag will be the name of the class
	 * @return the new {@code Logger} that has just been created
	 */
	@NonNull
	public static Logger createLoggerForClass(Class<?> clazz) {
		return createLoggerWithTag(clazz.getSimpleName());
	}

}
