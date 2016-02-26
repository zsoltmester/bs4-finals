package hu.bsmart.framework.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bsmart.framework.communication.networkhelper.CommunicationError;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.gcm.pushmessage.PushMessage;
import hu.bsmart.framework.navigation.Screen;
import hu.bsmart.framework.navigation.ScreenInstantiationException;
import hu.bsmart.framework.navigation.ScreenNavigator;
import hu.bsmart.framework.ui.activity.FrameworkActivity;

/**
 * Base Fragment for other Fragments in this application.
 * <p/>
 * This Fragment provides the functionality to easily start network requests
 * by calling {@code startNetworkRequest}.
 * <p/>
 * When the request is done, either {@code onRequestSucceeded} or
 * {@code onRequestFailed} will be called. These two methods will be called
 * on the UI thread, so no need for {@link Activity#runOnUiThread(Runnable)}
 * or {@link Handler}s when dealing with the answer.
 */
public class FrameworkFragment extends Fragment implements ScreenNavigator {

	/**
	 * Creates a Fragment based on the provided screen's properties.
	 *
	 * @param screen the {@link Screen} whose controller Fragment needs to be created
	 * @param args   {@link Bundle} that will be passed to the screen as its arguments
	 * @return the Fragment instance
	 */
	public static Fragment newInstance(Screen screen, Bundle args) {

		if (screen == null) {
			throw new NullPointerException("Screen is required!");
		}

		Class<? extends FrameworkFragment> clazz = screen.getControllerFragmentClass();

		if (clazz == null) {
			throw new NullPointerException("Concrete class is required!");
		}

		int layoutResource = screen.getLayoutResource();

		if (layoutResource == 0) {
			throw new IllegalArgumentException("Layout resource is invalid (it is 0). Maybe forgot to add that?");
		}

		try {
			FrameworkFragment result = clazz.newInstance();
			result.layoutResource = layoutResource;
			if (args != null) {
				result.setArguments(args);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScreenInstantiationException("Could not create instance of class: " + clazz);
		}
	}

	private static final String STATE_LAYOUT_RESOURCE = "STATE_LAYOUT_RESOURCE";

	private int layoutResource;

	@SuppressWarnings("serial")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_LAYOUT_RESOURCE)) {
			layoutResource = savedInstanceState.getInt(STATE_LAYOUT_RESOURCE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof FrameworkActivity)) {
			throw new IllegalStateException("This fragment should have a FrameworkActivity!");
		}
	}

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(layoutResource, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_LAYOUT_RESOURCE, layoutResource);
	}

	public FrameworkActivity getFrameworkActivity() {
		return (FrameworkActivity) getActivity();
	}

	@Override
	public final void goToScreen(Screen screen) {
		getFrameworkActivity().goToScreen(screen);
	}

	@Override
	public void goToScreenWithArguments(Screen screen, Bundle arguments) {
		getFrameworkActivity().goToScreenWithArguments(screen, arguments);
	}

	@Override
	public void goToScreenWithNoWayBack(Screen screen, Bundle arguments) {
		getFrameworkActivity().goToScreenWithNoWayBack(screen, arguments);
	}

	@Override
	public void goBack() {
		getFrameworkActivity().goBack();
	}

	@Override
	public void goBackWithArguments(Bundle arguments) {
		getFrameworkActivity().goBackWithArguments(arguments);
	}

	@Override
	public void goHome() {
		getFrameworkActivity().goHome();
	}

	@Override
	public void goHomeWithArguments(Bundle arguments) {
		getFrameworkActivity().goHomeWithArguments(arguments);
	}

	/**
	 * Called when the up button is pressed in the toolbar.
	 * <p/>
	 * Default behavior is to go back one screen. Subclasses can override
	 * the method and modify this behavior.
	 *
	 * @return {@code true} if the fragment handled the up navigation, false otherwise
	 */
	public boolean onUpNavigationButtonPressed() {
		goBack();
		return true;
	}

	/**
	 * Sets the title for the actual screen.
	 *
	 * @param title the title. Cannot be {@code null}.
	 */
	protected final void setScreenTitle(CharSequence title) {
		getFrameworkActivity().setTitle(title);
	}

	/**
	 * Called when the user navigates here with {@link ScreenNavigator#goBackWithArguments(Bundle)}
	 * or {@link ScreenNavigator#goHomeWithArguments(Bundle)}.
	 *
	 * @param bundle the arguments that have been sent
	 */
	public void onBundleArrived(Bundle bundle) {
	}

	/**
	 * Starts a network request based on the provided {@link NetworkRequest} object.
	 *
	 * @param requestCode the ID for the request
	 * @param request     the request object
	 */
	protected final void startNetworkRequest(String requestCode, NetworkRequest request) {
		getFrameworkActivity().startNetworkRequest(requestCode, request);
	}

	/**
	 * Called after a successful network request. Subclasses should override this method
	 * to do something with a successful network operation's response.
	 * <p/>
	 * The default implementation does nothing.
	 *
	 * @param requestCode the ID for the initial request
	 * @param response    the response object
	 */
	public void onRequestSucceeded(String requestCode, NetworkResponse response) {
	}

	/**
	 * Called if a network request failed. Subclasses should override this method
	 * to get notified about the error, and do something in case of that happens.
	 * <p/>
	 * The default implementation does nothing.
	 *
	 * @param requestCode the ID for the initial request
	 * @param error       the error that caused the failure
	 */
	public void onRequestFailed(String requestCode, CommunicationError error) {
	}

	/**
	 * Called when a PushMessage arrives from the server.
	 * <p/>
	 * The default implementation does nothing.
	 *
	 * @param pushMessage the push message
	 * @return {@code true} if the push message was handled
	 */
	public boolean onPushMessageArrived(PushMessage pushMessage) {
		return false;
	}

	/**
	 * Called when an NFC tag has been scanned by the device. Subclasses should override
	 * this method to get notified about this event, and do something in case of that happens.
	 * <p/>
	 * The default implementation does nothing.
	 * <p/>
	 * In the screen configuration, you have to declare that you need
	 * this info, otherwise you will not get the callbacks.
	 *
	 * @param data the scanned data
	 * @see Screen#needsNfc()
	 */
	public void onNfcTagScanned(String data) {
	}

	/**
	 * Called when new location info is available. Subclasses should override this
	 * method to get notified about this event, and do something in case of that happens.
	 * <p/>
	 * The default implementation does nothing.
	 * <p/>
	 * In the screen configuration, you have to declare that you need
	 * this info, otherwise you will not get the callbacks.
	 *
	 * @param newLatitude  the latitude of the new location
	 * @param newLongitude the longitude of the new location
	 * @see Screen#needsLocation()
	 */
	public void onLocationChanged(double newLatitude, double newLongitude) {
	}

	/**
	 * Called when the user initiates a refresh while interacting with this screen.
	 * <p/>
	 * Subclasses should refresh their content in their implementations.
	 * <p/>
	 * The default implementation does nothing.
	 * <p/>
	 * Only will be called if the corresponding screen configuration allows
	 * the user to refresh this screen.
	 *
	 * @see Screen#isRefreshable()
	 */
	public void onRefresh() {
	}

}
