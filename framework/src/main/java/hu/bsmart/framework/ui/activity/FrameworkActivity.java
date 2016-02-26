package hu.bsmart.framework.ui.activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hu.bsmart.framework.R;
import hu.bsmart.framework.application.FrameworkApplication;
import hu.bsmart.framework.communication.networkhelper.CommunicationError;
import hu.bsmart.framework.communication.networkhelper.NetworkHelper;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.gcm.GcmManager;
import hu.bsmart.framework.gcm.GcmRegistrationListener;
import hu.bsmart.framework.gcm.PushMessageListener;
import hu.bsmart.framework.gcm.pushmessage.PushMessage;
import hu.bsmart.framework.handler.PauseHandler;
import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;
import hu.bsmart.framework.navigation.Screen;
import hu.bsmart.framework.navigation.ScreenManager;
import hu.bsmart.framework.navigation.ScreenNavigator;
import hu.bsmart.framework.ui.activity.WorkerFragment.RequestStateListener;
import hu.bsmart.framework.ui.activity.NdefReaderTask.NfcResultHandler;
import hu.bsmart.framework.ui.fragment.FrameworkFragment;
import hu.bsmart.framework.ui.fragment.dialog.AlertDialogFragment;
import hu.bsmart.framework.ui.fragment.dialog.ProgressDialogFragment;
import hu.bsmart.framework.util.PermissionUtil;

public abstract class FrameworkActivity extends AppCompatActivity implements ScreenNavigator,
		NfcResultHandler, LocationListener, RequestStateListener, AlertDialogFragment.OkClickListener,
		GcmRegistrationListener, PushMessageListener {

	private static final String SCREEN_FRAGMENT_TAG = "SCREEN_FRAGMENT";
	private static final String GCM_PROGRESS_DIALOG_FRAGMENT_TAG = "GCM_PROGRESS_DIALOG_FRAGMENT_TAG";
	private static final String NETWORK_PROGRESS_DIALOG_FRAGMENT_TAG = "NETWORK_PROGRESS_DIALOG_FRAGMENT_TAG";
	private static final String WORKER_FRAGMENT_TAG = "WORKER_FRAGMENT";
	private static final String PERMISSION_ERROR_ALERT_DIALOG_TAG = "PERMISSION_ERROR_ALERT_DIALOG_TAG";

	private static final String HOME_SCREEN_TRANSACTION = "HOME_SCREEN_TRANSACTION";

	private Toolbar toolbar;

	private Logger logger;
	private PauseHandler pauseHandler;
	private InternalNavigator internalNavigator = new InternalNavigator();
	private String waitMessage;

	private boolean playServicesOk;
	private LatLng targetLocation;

	// Advanced functions
	private NfcDelegate nfcDelegate;
	private GoogleApiClientDelegate googleApiClientDelegate;

	private boolean basePermissionGratntedCalled;

	/**
	 * Returns the starting {@link Screen} of the Activity.
	 * <p/>
	 * Subclasses should decide what screen to show initially by overriding this method.
	 * In addition, this screen will act as the home screen of this Activity, meaning that
	 * <code>goHome</code> calls will navigate here.
	 *
	 * @return the home screen
	 */
	protected abstract Screen getHomeScreen();

	/**
	 * Returns the required permissions for the application.
	 * <p/>
	 * Subclasses should collect every permission here that they will need
	 * for their tasks.
	 *
	 * @return the permissions needed by the concrete subclass
	 */
	protected abstract String[] getRequiredPermissions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		logger = LoggerProvider.createLoggerForClass(getClass());

		Fragment workerFragment = getSupportFragmentManager().findFragmentByTag(WORKER_FRAGMENT_TAG);

		if (workerFragment == null) {
			workerFragment = WorkerFragment.newInstance();
			workerFragment.setRetainInstance(true);
			getSupportFragmentManager().beginTransaction().add(workerFragment, WORKER_FRAGMENT_TAG).commit();
		}

		setContentView(R.layout.activity_layout);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		pauseHandler = new PauseHandler();

		waitMessage = getString(R.string.wait_dialog_message);

		nfcDelegate = new NfcDelegate(this);

		playServicesOk = GooglePlayServicesUtil.checkPlayServices(this);
		if (playServicesOk) {
			googleApiClientDelegate = new DefaultGoogleApiClientDelegate(this);
		} else {
			googleApiClientDelegate = new DummyGoogleApiClientDelegate();
		}

		if (savedInstanceState == null) {
			goHome();
		} else {
			ScreenManager.getInstance().restoreState(savedInstanceState);
			getSupportFragmentManager().executePendingTransactions();
			updateScreenData();
		}
	}

	protected GoogleApiClient getApiClient() {
		return googleApiClientDelegate.getClient();
	}

	@Override
	protected void onStart() {
		super.onStart();
		googleApiClientDelegate.connect();

		if (!PermissionUtil.areAllGranted(this, getRequiredPermissions())) {
			ActivityCompat.requestPermissions(this, getRequiredPermissions(), 0);
		} else {
			onPermissionsGranted();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		pauseHandler.resume();
		playServicesOk = GooglePlayServicesUtil.checkPlayServices(this);
		nfcDelegate.startTrackingNfc();
		if (getActualScreen() != null && getActualScreen().needsLocation()) {
			googleApiClientDelegate.startTrackingLocation();
		}
		googleApiClientDelegate.stopGeofence();
		if (playServicesOk) {
			GcmManager.getInstance().registerPushMessageListener(this);
		} else {
			showGcmRegistrationFailDialog();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		pauseHandler.pause();
		setupGeofenceIfNeeded();
		nfcDelegate.stopTrackingNfc();
		if (getActualScreen() != null && getActualScreen().needsLocation()) {
			googleApiClientDelegate.stopTrackingLocation();
		}
		GcmManager.getInstance().unregisterPushMessageListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		googleApiClientDelegate.disconnect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getActualScreen() != null && getActualScreen().isRefreshable()) {
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.refresh_menu, menu);
			return true;
		} else {
			return super.onCreateOptionsMenu(menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int menuItemId = item.getItemId();
		FrameworkFragment screenFragment = getActualScreenFragment();
		if (menuItemId == android.R.id.home) {
			if (screenFragment != null) {
				return screenFragment.onUpNavigationButtonPressed();
			}
		} else if (menuItemId == R.id.menu_refresh_item) {
			if (screenFragment != null) {
				screenFragment.onRefresh();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Called after all of the permissions are granted.
	 * You should start "initialization" tasks that require
	 * runtime permissions in this method.
	 * <p/>
	 * Subclass implementations should call the
	 * implementation of the base class.
	 */
	protected void onPermissionsGranted() {
		basePermissionGratntedCalled = true;
	}

	/**
	 * Initiates a GCM registration to obtain a GCM token.
	 * <p/>
	 * Subclasses should call this method every time when
	 * they want to send a token to the server. This method
	 * only requests new token from Android if the application
	 * doesn't have one yet.
	 * <p/>
	 * On successful registration {@link FrameworkActivity#onGcmTokenAvailable(String)} will be called.
	 * <p/>
	 * If anything happens with our token, {@link FrameworkActivity#onGcmTokenBecameInvalid()} will be called.
	 * See the method's documentation to learn what to do in that case.
	 * <p/>
	 * The best place to call this method is {@link FrameworkActivity#onPermissionsGranted()},
	 * because that way you can be sure that the app has the required permissions.
	 *
	 * @param showProgressDialog if {@code true}, a progress dialog will be shown during the registration process
	 * @see FrameworkActivity#onGcmTokenAvailable(String)
	 * @see FrameworkActivity#onGcmTokenBecameInvalid()
	 */
	protected final void startGcmRegistration(boolean showProgressDialog) {
		if (arePlayServicesOk()) {
			if (showProgressDialog) {
				showProgressDialog(GCM_PROGRESS_DIALOG_FRAGMENT_TAG);
			}
			GcmManager.getInstance().startGcmRegistration(this, this);
		} else {
			showGcmRegistrationFailDialog();
		}
	}

	/**
	 * Shows a dialog that notifies the user when
	 * something goes wrong with GCM registration.
	 */
	protected void showGcmRegistrationFailDialog() {
		AlertDialogFragment.newInstance(R.string.gcm_registration_error_title, R.string.gcm_registration_error_message)
				.showAllowingStateLoss(getSupportFragmentManager(), null);
	}

	@Override
	public final void onGcmRegistrationSucceeded(String gcmToken) {
		hideProgressDialog(GCM_PROGRESS_DIALOG_FRAGMENT_TAG);
		onGcmTokenAvailable(gcmToken);
	}

	@Override
	public final void onGcmRegistrationFailed() {
		hideProgressDialog(GCM_PROGRESS_DIALOG_FRAGMENT_TAG);
		showGcmRegistrationFailDialog();
	}

	@Override
	public boolean onPushMessageArrived(PushMessage pushMessage) {
		logger.d("Push message arrived into Activity: " + pushMessage.getPushType());
		return getActualScreenFragment().onPushMessageArrived(pushMessage);
	}

	@Override
	public boolean onPushMessageError(String errorMessage) {
		if (FrameworkApplication.BuildConfig.DEBUG) {
			AlertDialogFragment.newInstance(getString(R.string.push_message_error_title), errorMessage)
					.showAllowingStateLoss(getSupportFragmentManager(), null);
		}
		return true;
	}

	@Override
	public final void onGcmTokenChanged() {
		onGcmTokenBecameInvalid();
	}

	/**
	 * Called when we need a new GCM token.
	 * <p/>
	 * Subclasses should start another registration to obtain a new
	 * token. If possible, the new token should be sent to the server
	 * without bothering the user. However, if this cannot be accomplished,
	 * the implementation should notify the user somehow - like navigation
	 * back to a login screen, etc.
	 *
	 * @see FrameworkActivity#startGcmRegistration(boolean)
	 */
	protected abstract void onGcmTokenBecameInvalid();

	/**
	 * Called when we acquired a GCM token.
	 * <p/>
	 * Subclasses should send the token to the
	 * server in some way.
	 *
	 * @param gcmToken the GCM token
	 */
	protected abstract void onGcmTokenAvailable(String gcmToken);

	@Override
	protected void onNewIntent(Intent intent) {
		boolean handled = false;

		Screen actualScreen = getActualScreen();
		if (actualScreen != null && actualScreen.needsNfc()) {
			handled = nfcDelegate.handleNfcIntent(intent);
		}

		if (!handled) {
			super.onNewIntent(intent);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			finish();
		} else {
			updateScreenData();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (!PermissionUtil.areAllGranted(this, getRequiredPermissions())) {
			// If we don't have every permissions, we cannot run!
			Fragment previousDialog = getSupportFragmentManager().findFragmentByTag(PERMISSION_ERROR_ALERT_DIALOG_TAG);
			if (previousDialog != null) {
				getSupportFragmentManager().beginTransaction().remove(previousDialog).commit();
				getSupportFragmentManager().executePendingTransactions();
			}

			AlertDialogFragment permissionErrorDialog =
					AlertDialogFragment.newInstance(R.string.permission_error_title, R.string.permission_error_message);
			permissionErrorDialog.setOkClickListener(this);
			permissionErrorDialog.showAllowingStateLoss(getSupportFragmentManager(), PERMISSION_ERROR_ALERT_DIALOG_TAG);
		} else {
			basePermissionGratntedCalled = false;
			onPermissionsGranted();
			if (!basePermissionGratntedCalled) {
				throw new IllegalStateException("FrameworkActivity.onPermissionsGranted() was not called!");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		ScreenManager.getInstance().saveState(outState);
	}

	// NfcResultHandler interface implementation START

	@Override
	public void handleNfcResult(String data) {
		FrameworkFragment actualScreenFragment = getActualScreenFragment();
		if (actualScreenFragment != null) {
			actualScreenFragment.onNfcTagScanned(data);
		}
	}

	// NfcResultHandler interface implementation END

	private void setupGeofenceIfNeeded() {
		if (getActualScreen() == null || !getActualScreen().needsLocation()) {
			return;
		}

		if (targetLocation == null) {
			return;
		}

		googleApiClientDelegate.startGeofence(targetLocation, LocationConstants.GEOFENCE_RADIUS_IN_METERS);
	}

	public void setTargetLocation(LatLng targetLocation) {
		this.targetLocation = targetLocation;
	}

	// LocationListener interface implementation START

	@Override
	public void onLocationChanged(Location location) {
		Screen actualScreen = getActualScreen();
		if (actualScreen != null && actualScreen.needsLocation()) {
			FrameworkFragment actualScreenFragment = getActualScreenFragment();
			if (actualScreenFragment != null) {
				actualScreenFragment.onLocationChanged(location.getLatitude(), location.getLongitude());
			}
		}
	}

	// LocationListener interface implementation END

	// ScreenNavigator interface implementation START

	@Override
	public void goToScreen(Screen screen) {
		goToScreenWithArguments(screen, null);
	}

	@Override
	public void goToScreenWithArguments(Screen screen, Bundle arguments) {
		pauseHandler.postRequest(new GoToScreenRequest(internalNavigator, screen, arguments));
	}

	@Override
	public void goToScreenWithNoWayBack(Screen screen, Bundle arguments) {
		pauseHandler.postRequest(new IrreversibleGoToScreenRequest(internalNavigator, screen, arguments));
	}

	@Override
	public void goBack() {
		goBackWithArguments(null);
	}

	@Override
	public void goBackWithArguments(Bundle arguments) {
		pauseHandler.postRequest(new GoBackRequest(internalNavigator, arguments));
	}

	@Override
	public void goHome() {
		goHomeWithArguments(null);
	}

	@Override
	public void goHomeWithArguments(Bundle arguments) {
		pauseHandler.postRequest(new GoHomeRequest(internalNavigator, arguments));
	}

	// ScreenNavigator interface implementation END

	/**
	 * Returns the {@link FrameworkFragment} that is currently displayed.
	 *
	 * @return
	 */
	protected FrameworkFragment getActualScreenFragment() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm == null) {
			return null;
		}
		return (FrameworkFragment) fm.findFragmentByTag(SCREEN_FRAGMENT_TAG);
	}

	/**
	 * Returns the {@link Screen} that is currently being shown.
	 *
	 * @return
	 */
	protected Screen getActualScreen() {
		Fragment screenFragment = getActualScreenFragment();
		return screenFragment == null ? null : ScreenManager.getInstance().getScreenForFragment(screenFragment);
	}

	private WorkerFragment getWorkerFragment() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm == null) {
			return null;
		}

		Fragment worker = fm.findFragmentByTag(WORKER_FRAGMENT_TAG);
		if (worker == null) {
			throw new IllegalStateException("Worker fragment is missing!");
		}

		return (WorkerFragment) worker;
	}

	/**
	 * Replaces the currently shown fragment to another one selected
	 * by the given screen. This method is used for navigating between
	 * {@link Screen}s.
	 *
	 * @param screen        the target Screen whose Fragment should be displayed
	 * @param arguments     the arguments that need to be passed to the Screen
	 * @param transactionId the transaction ID to be used with the fragment back stack
	 */
	private void replaceScreenFragment(Screen screen, Bundle arguments, String transactionId) {
		replaceScreenFragment(screen, arguments, transactionId, false);
	}

	/**
	 * Replaces the currently shown fragment to another one selected
	 * by the given screen. This method is used for navigating between
	 * {@link Screen}s.
	 *
	 * @param screen        the target Screen whose Fragment should be displayed
	 * @param arguments     the arguments that need to be passed to the Screen
	 * @param transactionId the transaction ID to be used with the fragment back stack
	 * @param irreversible  if {@code true}, the transaction will never be saved
	 */
	private void replaceScreenFragment(Screen screen, Bundle arguments, String transactionId, boolean irreversible) {
		Fragment fragment = FrameworkFragment.newInstance(screen, arguments);
		ScreenManager.getInstance().registerMapping(fragment.getClass(), screen);

		FragmentManager fm = getSupportFragmentManager();

		Screen actualScreen = getActualScreen();

		boolean mustPopBackStack = irreversible || (actualScreen != null && !actualScreen.canNavigateBackHere());

		if (mustPopBackStack) {
			fm.popBackStackImmediate();
		}

		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragment_place, fragment, SCREEN_FRAGMENT_TAG);
		int fadeInAnimation = screen.getFadeInAnimation();
		int fadeOutAnimation = actualScreen != null ? actualScreen.getFadeOutAnimation() : 0;
		if (fadeInAnimation != 0 || fadeOutAnimation != 0) {
			int transition = fadeInAnimation != 0 ? FragmentTransaction.TRANSIT_FRAGMENT_OPEN : 0;
			transition |= fadeOutAnimation != 0 ? FragmentTransaction.TRANSIT_FRAGMENT_CLOSE : 0;
			ft.setTransition(transition);
			ft.setCustomAnimations(fadeInAnimation, fadeOutAnimation);
		}

		ft.addToBackStack(transactionId);

		ft.commit();
		fm.executePendingTransactions();
	}

	/**
	 * Updates the properties of the Activity to match the {@link Screen} that is shown.
	 */
	private void updateScreenData() {
		Screen actualScreen = getActualScreen();
		if (actualScreen == null) {
			logger.w("No screen is shown?");
			return;
		}
		setOrientationForScreen(actualScreen);
		setActionBarForScreen(actualScreen);
		setLocationTrackingForScreen(actualScreen);
		invalidateOptionsMenu();
	}

	private void setOrientationForScreen(Screen screen) {
		int orientation;

		switch (screen.getOrientation()) {
			case PORTRAIT:
				orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;
			case LANDSCAPE:
				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			case AUTO:
			default:
				orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
				break;
		}

		setRequestedOrientation(orientation);
	}

	private void setActionBarForScreen(Screen screen) {
		if (screen.hasActionBar()) {
			getSupportActionBar().show();
			if (!screen.hasDynamicTitle()) {
				setTitle(screen.getActionBarTitleResource());
			}
			getSupportActionBar()
					.setDisplayHomeAsUpEnabled(screen != getHomeScreen() && screen.canNavigateUpFromHere());
		} else {
			getSupportActionBar().hide();
		}
	}

	private void setLocationTrackingForScreen(Screen screen) {
		if (screen.needsLocation()) {
			googleApiClientDelegate.startTrackingLocation();
		} else {
			googleApiClientDelegate.stopTrackingLocation();
		}
	}

	/**
	 * Sets the title. This method can be used in custom title screens
	 * to set or modify their own title
	 *
	 * @param title the new title, cannot be {@code null}.
	 */
	public void setTitle(CharSequence title) {
		if (title == null) {
			throw new NullPointerException("Missing title!");
		}

		getSupportActionBar().setTitle(title);
	}

	@Override
	public void onRequestStarted(String requestCode, NetworkRequest request) {
		showProgressDialog(NETWORK_PROGRESS_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void onRequestSucceeded(String requestCode, NetworkResponse response) {
		hideProgressDialog(NETWORK_PROGRESS_DIALOG_FRAGMENT_TAG);
		FrameworkFragment actualScreenFragment = getActualScreenFragment();
		if (actualScreenFragment != null) {
			actualScreenFragment.onRequestSucceeded(requestCode, response);
		}
	}

	@Override
	public void onRequestFailed(String requestCode, CommunicationError error) {
		hideProgressDialog(NETWORK_PROGRESS_DIALOG_FRAGMENT_TAG);

		if (FrameworkApplication.BuildConfig.DEBUG &&
				NetworkHelper.getInstance().getOperationMode() == NetworkHelper.OperationMode.NORMAL) {
			AlertDialogFragment.newInstance("Communication error", error.toString())
					.showAllowingStateLoss(getSupportFragmentManager(), "comm_error");
		}
		FrameworkFragment actualScreenFragment = getActualScreenFragment();
		if (actualScreenFragment != null) {
			actualScreenFragment.onRequestFailed(requestCode, error);
		}
	}

	private void showProgressDialog(String dialogFragmentTag) {
		FragmentManager fm = getSupportFragmentManager();
		if (fm == null) {
			return;
		}
		Fragment dialogFragment = fm.findFragmentByTag(dialogFragmentTag);
		if (dialogFragment == null) {
			ProgressDialogFragment.newInstance(waitMessage).showAllowingStateLoss(fm, dialogFragmentTag);
			fm.executePendingTransactions();
		}
	}

	private void hideProgressDialog(String dialogFragmentTag) {
		FragmentManager fm = getSupportFragmentManager();
		if (fm == null) {
			return;
		}
		Fragment progressDialogFragment = fm.findFragmentByTag(dialogFragmentTag);
		if (progressDialogFragment != null) {
			((ProgressDialogFragment) progressDialogFragment).dismissAllowingStateLoss();
		}
	}

	/**
	 * Starts a network request based on the provided {@link NetworkRequest} object.
	 *
	 * @param requestCode the ID for the request
	 * @param request     the request object
	 */
	public final void startNetworkRequest(String requestCode, NetworkRequest request) {
		getWorkerFragment().startNetworkRequest(requestCode, request);
	}

	protected boolean arePlayServicesOk() {
		return playServicesOk;
	}

	/**
	 * Internal screen navigator that is used by the Activity.
	 * <p/>
	 * It is needed because it gives the ability to encapsulate the
	 * navigation requests into Runnable objects.
	 *
	 * @see NavigationRequest
	 * @see GoToScreenRequest
	 * @see GoHomeRequest
	 * @see GoBackRequest
	 */
	private class InternalNavigator implements ScreenNavigator {
		@Override
		public void goToScreenWithArguments(Screen screen, Bundle arguments) {
			replaceScreenFragment(screen, arguments, null);
			updateScreenData();
		}

		@Override
		public void goToScreenWithNoWayBack(Screen screen, Bundle arguments) {
			replaceScreenFragment(screen, arguments, null, true);
			updateScreenData();
		}

		@Override
		public void goToScreen(Screen screen) {
			this.goToScreenWithArguments(screen, null);
		}

		@Override
		public void goHomeWithArguments(Bundle arguments) {
			if (getActualScreenFragment() == null) {
				// Still no screens, need to add first
				replaceScreenFragment(getHomeScreen(), null, HOME_SCREEN_TRANSACTION);
			} else {
				int homeScreenEntryIndex = -1;
				for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
					BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
					if (HOME_SCREEN_TRANSACTION.equals(entry.getName())) {
						homeScreenEntryIndex = i;
						break;
					}
				}

				if (homeScreenEntryIndex == -1) {
					getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					replaceScreenFragment(getHomeScreen(), null, HOME_SCREEN_TRANSACTION);
				} else {
					// Just remove every other fragments from the back stack
					getSupportFragmentManager().popBackStackImmediate(homeScreenEntryIndex, 0);
				}
			}

			updateScreenData();
			if (arguments != null) {
				getActualScreenFragment().onBundleArrived(arguments);
			}
		}

		@Override
		public void goHome() {
			this.goHomeWithArguments(null);
		}

		@Override
		public void goBackWithArguments(Bundle arguments) {
			onBackPressed();
			getSupportFragmentManager().executePendingTransactions();
			if (arguments != null) {
				getActualScreenFragment().onBundleArrived(arguments);
			}
		}

		@Override
		public void goBack() {
			this.goBackWithArguments(null);
		}
	}

	@Override
	public void onOkClicked() {
		// Called when alertdialog about google services is closed.
		finish();
	}
}