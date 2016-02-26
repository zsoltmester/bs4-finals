package hu.bsmart.framework.navigation;

import android.support.v7.widget.Toolbar;

import hu.bsmart.framework.ui.fragment.FrameworkFragment;

/**
 * Interface for the screens that can be used with the
 * {@link ScreenNavigator} interface.
 * <p/>
 * When defining a screen, specifying its controller fragment class
 * and its layout resource is required, the other options should be
 * optional.
 */
public interface Screen {

	/**
	 * Orientation of the screen
	 */
	enum ScreenOrientation {
		/**
		 * The screen always have portrait orientation
		 */
		PORTRAIT,

		/**
		 * The screen always have landscape orientation
		 */
		LANDSCAPE,

		/**
		 * The screen can be rotated
		 */
		AUTO
	}

	/**
	 * Gets the layout resource of the screen.
	 *
	 * @return the layout resource
	 */
	int getLayoutResource();

	/**
	 * Gets the class for the fragment containing the controller logic of the screen.
	 */
	Class<? extends FrameworkFragment> getControllerFragmentClass();

	/**
	 * Gets the orientation used by the screen.
	 */
	ScreenOrientation getOrientation();

	/**
	 * Determines whether the screen has {@link Toolbar} or not.
	 */
	boolean hasActionBar();

	/**
	 * If {@code true}, the screen manages its title on its own
	 * instead of having a static title.
	 */
	boolean hasDynamicTitle();

	/**
	 * Determines whether the Screen's content
	 * can be refreshed by the user.
	 */
	boolean isRefreshable();

	/**
	 * Gets the string resource ID to be used as the ActionBar title of the screen.
	 * <p/>
	 * Returning 0 means that the application title will be used.
	 */
	int getActionBarTitleResource();

	/**
	 * Indicates whether the user can navigate back to this screen with the back button.
	 * <p/>
	 * If this is false, the screen will never be added to the back stack.
	 */
	boolean canNavigateBackHere();

	/**
	 * True if the screen has an up navigation.
	 */
	boolean canNavigateUpFromHere();

	/**
	 * Gets the ID of the transition when entering into this screen.
	 * <p/>
	 * Returning 0 means no transaction will be applied.
	 */
	int getFadeInAnimation();

	/**
	 * Gets the ID of the transition when leaving this screen.
	 * <p/>
	 * Returning 0 means no transaction will be applied.
	 */
	int getFadeOutAnimation();

	/**
	 * Determines whether the Screen needs to get notified about
	 * scanned NFC tags or not.
	 */
	boolean needsNfc();

	/**
	 * Determines whether the Screen needs to get info
	 * about the actual location or not.
	 */
	boolean needsLocation();
}

