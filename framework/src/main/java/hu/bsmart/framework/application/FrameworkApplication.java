package hu.bsmart.framework.application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import hu.bsmart.framework.logging.LoggerProvider;

/**
 * Base class for applications that use the framework.
 * <p/>
 * This base class sets the default SSL Socket factory
 * that accepts the certificate(s) that the app use.
 */
public class FrameworkApplication extends Application {
	private static Context appContext;

	/**
	 * The purpose of this field is to workaround an issue that arises when trying to use this information in library
	 * projects. This does not solve everything (like flavor related resources), but solves the case when debug /
	 * release needs to be checked from code.
	 *
	 * @see <a href="https://code.google.com/p/android/issues/detail?id=52962">Bug in the build system</a>
	 */
	public static final class BuildConfig {
		public static boolean DEBUG = false;
	}

	private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

	private Thread.UncaughtExceptionHandler digitAdventureUncaughtExceptionHandler =
			new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread thread, Throwable throwable) {
					String msg = exceptionStacktraceToString(throwable);

					long time = System.currentTimeMillis();

					File root = android.os.Environment.getExternalStorageDirectory();
					File dir = new File(root.getAbsolutePath() + "/bsmart");
					dir.mkdirs();
					File file = new File(dir, "bsmart-crash-" + time + ".log");

					try {
						PrintWriter pw = new PrintWriter(file, "UTF-8");
						pw.print(msg);
						pw.flush();
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
				}
			};

	@Override
	public void onCreate() {
		super.onCreate();
		initBuildConfigField(getApplicationContext());
		appContext = getApplicationContext();
		defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(digitAdventureUncaughtExceptionHandler);
		int[] certificates = getCertificates();
		if (certificates != null) {
			HttpsURLConnection
					.setDefaultSSLSocketFactory(createSSLSocketFactory(getApplicationContext(), certificates));
		}
		if (!BuildConfig.DEBUG) {
			LoggerProvider.disableAllLogging();
		}
	}

	/**
	 * In case an application needs HTTPS communication, it can return resource IDs for its certificates to make the
	 * framework install them into its trust manager.
	 *
	 * @return the resource IDs for the certificates.
	 */
	@Nullable
	protected int[] getCertificates() {
		return null;
	}

	public static Context getAppContext() {
		return appContext;
	}

	/**
	 * Creates an SSL SocketFactory for our certificate.
	 *
	 * @param context Android context
	 * @return the SSLSocketFactory
	 */
	private SSLSocketFactory createSSLSocketFactory(Context context, @NonNull int[] certificates) {
		try {
			FrameworkTrustMangerFactory.init(context, certificates);
			X509TrustManager frameworkTrustManager = FrameworkTrustMangerFactory.getFrameworkTrustManger();
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{frameworkTrustManager}, null);
			return sslContext.getSocketFactory();
		} catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
			e.printStackTrace();
		}

		return (SSLSocketFactory) SSLSocketFactory.getDefault();
	}

	private String exceptionStacktraceToString(Throwable t) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}

	/**
	 * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
	 * are used at the project level to set custom fields.
	 * @param context       Used to find the correct file
	 * @param fieldName     The name of the field-to-access
	 * @return The value of the field, or {@code null} if the field is not found.
	 */

	/**
	 * Initializes {@link FrameworkApplication.BuildConfig}.
	 *
	 * @param context Context for retrieving package name
	 */
	private static void initBuildConfigField(Context context) {
		try {
			Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
			Field field = clazz.getField("DEBUG");
			field.setAccessible(true);
			Boolean value = (Boolean) field.get(null);
			if (value == null) {
				throw new NullPointerException("Value is null!");
			}
			BuildConfig.DEBUG = value;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Could not get debug state of application!", e);
		}
	}

}
