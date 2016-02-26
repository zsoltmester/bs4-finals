package hu.bsmart.framework.ui.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import android.support.v4.app.FragmentActivity;

import hu.bsmart.framework.R;
import hu.bsmart.framework.ui.fragment.dialog.AlertDialogFragment;

class GooglePlayServicesUtil {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 *
	 * @param activity
	 */
	public static boolean checkPlayServices(FragmentActivity activity) {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				AlertDialogFragment.newInstance(R.string.google_play_services_error_title,
						R.string.google_play_services_error_message).show(activity.getSupportFragmentManager(), null);
			}
			return false;
		}
		return true;
	}

}
