package hu.bsmart.framework.application;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

class FrameworkTrustMangerFactory {
	private static X509TrustManager defaultTrustManger = null;
	private static X509TrustManager frameworkTrustManager = null;
	private static X509TrustManager finalTrustManager = null;

	static void init(Context context, @NonNull int[] certificates)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		if (defaultTrustManger == null) {
			defaultTrustManger = getDefaultTrustManger();
		}

		if (frameworkTrustManager == null) {
			frameworkTrustManager = createFrameworkTrustManager(context, certificates);
		}

		if (finalTrustManager == null) {
			finalTrustManager = new X509TrustManager() {
				private X509Certificate[] acceptedIssuers = null;

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					try {
						defaultTrustManger.checkClientTrusted(chain, authType);
					} catch (CertificateException exception) {
						frameworkTrustManager.checkClientTrusted(chain, authType);
					}
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					try {
						defaultTrustManger.checkServerTrusted(chain, authType);
					} catch (CertificateException exception) {
						frameworkTrustManager.checkServerTrusted(chain, authType);
					}
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					if (acceptedIssuers == null) {
						X509Certificate[] defaultIssuers = defaultTrustManger.getAcceptedIssuers();
						X509Certificate[] digitAdventureIssuers = frameworkTrustManager.getAcceptedIssuers();
						acceptedIssuers = new X509Certificate[defaultIssuers.length + digitAdventureIssuers.length];

						int index = 0;

						for (X509Certificate issuer : defaultIssuers) {
							acceptedIssuers[index++] = issuer;
						}

						for (X509Certificate issuer : digitAdventureIssuers) {
							acceptedIssuers[index++] = issuer;
						}
					}

					return acceptedIssuers;
				}
			};
		}
	}

	private static X509TrustManager getDefaultTrustManger() throws NoSuchAlgorithmException, KeyStoreException {
		X509TrustManager result = null;
		TrustManagerFactory trustManagerFactory =
				TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init((KeyStore) null);

		for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
			if (trustManager instanceof X509TrustManager) {
				result = (X509TrustManager) trustManager;
				break;
			}
		}

		if (result == null) {
			throw new IllegalStateException("Default X509 TrustManager not found!");
		}

		return result;
	}

	private static X509TrustManager createFrameworkTrustManager(Context context, @NonNull int[] certificateResourceIds)
			throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

		X509Certificate[] certificates = new X509Certificate[certificateResourceIds.length];

		for (int i = 0; i < certificateResourceIds.length; i++) {
			InputStream publicKeyStream = context.getResources()
					.openRawResource(certificateResourceIds[i]);

			certificates[i] = (X509Certificate) certificateFactory.generateCertificate(publicKeyStream);
		}

		// Using a null input stream and null password to create an empty key store.
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);

		for (int i = 0; i < certificates.length; i++) {
			keyStore.setCertificateEntry(String.valueOf(i + 1), certificates[i]);
		}

		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);
		return (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
	}

	static X509TrustManager getFrameworkTrustManger() {
		if (defaultTrustManger == null || frameworkTrustManager == null || finalTrustManager == null) {
			throw new IllegalStateException("Not initialized! Call init(Context) first!");
		}

		return finalTrustManager;
	}

}
