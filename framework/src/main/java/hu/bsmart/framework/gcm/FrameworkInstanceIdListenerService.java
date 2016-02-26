package hu.bsmart.framework.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Implementation of {@link InstanceIDListenerService} that handles
 * the creation, rotation, and updating of registration tokens.
 */
public class FrameworkInstanceIdListenerService extends InstanceIDListenerService {
	@Override
	public void onTokenRefresh() {
		super.onTokenRefresh();
		GcmManager.getInstance().clearGcmToken(this);
	}
}
