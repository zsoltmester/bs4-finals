package hu.bsmart.framework.logging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.EnumMap;
import java.util.Map;

/**
 * Default implementation of the {@link Logger} interface
 * that logs to the Android log. See {@link android.util.Log}.
 */
class DefaultLogger implements Logger {
	private interface InternalLogger {
		void logTheMessage(String tag, String message);

		void logTheMessageAndTheThrowable(String tag, String message, Throwable throwable);
	}

	private static final Map<Logger.LoggingLevel, InternalLogger> internalLoggerMap;

	static {
		internalLoggerMap = new EnumMap<>(Logger.LoggingLevel.class);
		internalLoggerMap.put(Logger.LoggingLevel.VERBOSE, new InternalLogger() {
			@Override
			public void logTheMessage(String tag, String message) {
				Log.v(tag, message);
			}

			@Override
			public void logTheMessageAndTheThrowable(String tag, String message, Throwable throwable) {
				Log.v(tag, message, throwable);
			}
		});
		internalLoggerMap.put(Logger.LoggingLevel.DEBUG, new InternalLogger() {
			@Override
			public void logTheMessage(String tag, String message) {
				Log.d(tag, message);
			}

			@Override
			public void logTheMessageAndTheThrowable(String tag, String message, Throwable throwable) {
				Log.d(tag, message, throwable);
			}
		});
		internalLoggerMap.put(Logger.LoggingLevel.INFO, new InternalLogger() {
			@Override
			public void logTheMessage(String tag, String message) {
				Log.i(tag, message);
			}

			@Override
			public void logTheMessageAndTheThrowable(String tag, String message, Throwable throwable) {
				Log.i(tag, message, throwable);
			}
		});
		internalLoggerMap.put(Logger.LoggingLevel.WARNING, new InternalLogger() {
			@Override
			public void logTheMessage(String tag, String message) {
				Log.w(tag, message);
			}

			@Override
			public void logTheMessageAndTheThrowable(String tag, String message, Throwable throwable) {
				Log.w(tag, message, throwable);
			}
		});
		internalLoggerMap.put(Logger.LoggingLevel.ERROR, new InternalLogger() {
			@Override
			public void logTheMessage(String tag, String message) {
				Log.e(tag, message);
			}

			@Override
			public void logTheMessageAndTheThrowable(String tag, String message, Throwable throwable) {
				Log.e(tag, message, throwable);
			}
		});
	}

	/**
	 * Message that will be logged if you call
	 * a logger function with a null message parameter
	 */
	private static final String NULL_MSG = "<NULL MESSAGE>";

	/**
	 * State of this {@code Logger} instance (enabled/disabled)
	 */
	private boolean isEnabled = true;

	private String logTag;

	private Logger.LoggingLevel loggingLevel;

	/**
	 * Constructs a new {@code DefaultLogger} with the given tag and
	 * {@link LoggingLevel#VERBOSE VERBOSE} logging level.
	 *
	 * @param logTag the log tag, cannot be null
	 */
	DefaultLogger(@NonNull String logTag) {
		this(logTag, Logger.LoggingLevel.VERBOSE);
	}

	/**
	 * Constructs a new {@code DefaultLogger} with the given tag and logging level.
	 *
	 * @param logTag       the log tag, cannot be null
	 * @param loggingLevel the logging level, cannot be null
	 */
	DefaultLogger(@NonNull String logTag, @NonNull Logger.LoggingLevel loggingLevel) {
		if (logTag == null) {
			throw new NullPointerException("Log tag cannot be null!");
		}

		if (loggingLevel == null) {
			throw new NullPointerException("Logging level cannot be null!");
		}

		this.logTag = logTag;
		this.loggingLevel = loggingLevel;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	@Override
	public void setLoggingLevel(@NonNull LoggingLevel loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	@Override
	public void v(@Nullable String message) {
		log(LoggingLevel.VERBOSE, message);
	}

	@Override
	public void v(@Nullable String message, @Nullable Throwable throwable) {
		log(LoggingLevel.VERBOSE, message, throwable);
	}

	@Override
	public void d(@Nullable String message) {
		log(LoggingLevel.DEBUG, message);
	}

	@Override
	public void d(@Nullable String message, @Nullable Throwable throwable) {
		log(LoggingLevel.DEBUG, message, throwable);
	}

	@Override
	public void i(@Nullable String message) {
		log(LoggingLevel.INFO, message);
	}

	@Override
	public void i(@Nullable String message, @Nullable Throwable throwable) {
		log(LoggingLevel.INFO, message, throwable);
	}

	@Override
	public void w(@Nullable String message) {
		log(LoggingLevel.WARNING, message);
	}

	@Override
	public void w(@Nullable String message, @Nullable Throwable throwable) {
		log(LoggingLevel.WARNING, message, throwable);
	}

	@Override
	public void e(@Nullable String message) {
		log(LoggingLevel.ERROR, message);
	}

	@Override
	public void e(@Nullable String message, @Nullable Throwable throwable) {
		log(LoggingLevel.ERROR, message, throwable);
	}

	private void log(LoggingLevel loggingLevel, String message) {
		log(loggingLevel, message, null);
	}

	private void log(LoggingLevel loggingLevel, String message, Throwable throwable) {
		if (!LoggerProvider.isGlobalLoggingEnabled() || !isEnabled) {
			return;
		}

		if (loggingLevel.level < this.loggingLevel.level) {
			return;
		}

		String messageToLog = message == null ? NULL_MSG : message;

		if (throwable != null) {
			internalLoggerMap.get(loggingLevel).logTheMessageAndTheThrowable(logTag, messageToLog, throwable);
		} else {
			internalLoggerMap.get(loggingLevel).logTheMessage(logTag, messageToLog);
		}
	}
}
