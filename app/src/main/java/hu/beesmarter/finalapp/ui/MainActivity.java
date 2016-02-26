package hu.beesmarter.finalapp.ui;

import hu.beesmarter.finalapp.R;
import hu.bsmart.framework.navigation.Screen;
import hu.bsmart.framework.ui.activity.FrameworkActivity;

/**
 * The main activity of the application. This can be launched from a launcher.
 */
public class MainActivity extends FrameworkActivity {

	@Override
	protected Screen getHomeScreen() {
		return Screens.HOME;
	}

	@Override
	protected String[] getRequiredPermissions() {
		// TODO Add the permissions here.
		return null;
	}

	@Override
	protected void onGcmTokenBecameInvalid() {
		// Nothing to do here. We do not need GCM.
	}

	@Override
	protected void onGcmTokenAvailable(String gcmToken) {
		// Nothing to do here. We do not need GCM.
	}

	@Override
	protected void onDrawerItemClicked(final int id) {
		logger.d("onDrawerItemClicked: " + id);
		switch (id) {
			case R.id.options:
				if (getActualScreen() != Screens.OPTIONS) {
					goToScreen(Screens.OPTIONS);
				}
				break;
		}
	}

	@Override
	public void onInstanceIdAvailable(String instanceId) {
		// TODO What is this? Is it necessary?
	}
}
