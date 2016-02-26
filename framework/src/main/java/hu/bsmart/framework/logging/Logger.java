package hu.bsmart.framework.logging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Logger interface for easier logging.
 * <p/>
 * Implementations should respect the global logging state
 * that is set in {@link LoggerProvider}.
 *
 * @see LoggerProvider#isGlobalLoggingEnabled()
 */
public interface Logger {
	/**
	 * Logging level that decides what kind of messages should be logged.
	 */
	enum LoggingLevel {
		VERBOSE(0),
		DEBUG(1),
		INFO(2),
		WARNING(3),
		ERROR(4);

		int level;

		LoggingLevel(int level) {
			this.level = level;
		}

	}

	/**
	 * Enables / disables this Logger.
	 *
	 * @param enabled {@code true} to enable, {@code false} to disable
	 */
	void setEnabled(boolean enabled);

	/**
	 * Sets the logging level for this Logger.
	 *
	 * @param loggingLevel the logging level to use
	 */
	void setLoggingLevel(@NonNull LoggingLevel loggingLevel);

	/**
	 * Logs a {@link Logger.LoggingLevel#VERBOSE VERBOSE} message.
	 *
	 * @param message the message to log
	 */
	void v(@Nullable String message);

	/**
	 * Logs a {@link Logger.LoggingLevel#VERBOSE VERBOSE}
	 * message and a {@link Throwable}.
	 *
	 * @param message   the message to log
	 * @param throwable the throwable to log, if {@code null}, it will be ignored
	 */
	void v(@Nullable String message, @Nullable Throwable throwable);

	/**
	 * Logs a {@link Logger.LoggingLevel#DEBUG DEBUG} message.
	 *
	 * @param message the message to log
	 */
	void d(@Nullable String message);

	/**
	 * Logs a {@link Logger.LoggingLevel#DEBUG DEBUG}
	 * message and a {@link Throwable}.
	 *
	 * @param message   the message to log
	 * @param throwable the throwable to log, if {@code null}, it will be ignored
	 */
	void d(@Nullable String message, @Nullable Throwable throwable);

	/**
	 * Logs a {@link Logger.LoggingLevel#INFO INFO} message.
	 *
	 * @param message the message to log
	 */
	void i(@Nullable String message);

	/**
	 * Logs a {@link Logger.LoggingLevel#INFO INFO}
	 * message and a {@link Throwable}.
	 *
	 * @param message   the message to log
	 * @param throwable the throwable to log, if {@code null}, it will be ignored
	 */
	void i(@Nullable String message, @Nullable Throwable throwable);

	/**
	 * Logs a {@link Logger.LoggingLevel#WARNING WARNING} message.
	 *
	 * @param message the message to log
	 */
	void w(@Nullable String message);

	/**
	 * Logs a {@link Logger.LoggingLevel#WARNING WARNING}
	 * message and a {@link Throwable}.
	 *
	 * @param message   the message to log
	 * @param throwable the throwable to log, if {@code null}, it will be ignored
	 */
	void w(@Nullable String message, @Nullable Throwable throwable);

	/**
	 * Logs a {@link Logger.LoggingLevel#ERROR ERROR} message.
	 *
	 * @param message the message to log
	 */
	void e(@Nullable String message);

	/**
	 * Logs a {@link Logger.LoggingLevel#ERROR ERROR}
	 * message and a {@link Throwable}.
	 *
	 * @param message   the message to log
	 * @param throwable the throwable to log, if {@code null}, it will be ignored
	 */
	void e(@Nullable String message, @Nullable Throwable throwable);
}
