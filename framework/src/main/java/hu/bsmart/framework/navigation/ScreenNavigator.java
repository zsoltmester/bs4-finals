package hu.bsmart.framework.navigation;

import android.os.Bundle;

/**
 * Interface for navigating between {@code Screen}s
 */
public interface ScreenNavigator {

	/**
	 * Navigates to the provided screen.
	 *
	 * @param screen the target screen
	 */
	void goToScreen(Screen screen);

	/**
	 * Navigates to the provided screen,
	 * and passes the given arguments to it.
	 *
	 * @param screen    the target screen
	 * @param arguments the arguments for the screen
	 */
	void goToScreenWithArguments(Screen screen, Bundle arguments);

	/**
	 * Navigates to the provided screen,
	 * and passes the given arguments to it.
	 * It is impossible to navigate back to the
	 * previous screen from the target screen.
	 *
	 * @param screen    the target screen
	 * @param arguments the arguments for the screen
	 */
	void goToScreenWithNoWayBack(Screen screen, Bundle arguments);

	/**
	 * Navigates to the previous screen.
	 */
	void goBack();

	/**
	 * v	 * Navigates to the previous screen,
	 * and passes the given arguments to it.
	 *
	 * @param arguments the arguments for the screen
	 */
	void goBackWithArguments(Bundle arguments);

	/**
	 * Navigates to the first (home) screen.
	 */
	void goHome();

	/**
	 * v	 * Navigates to the first (home) screen,
	 * and passes the given arguments to it.
	 *
	 * @param arguments the arguments for the screen
	 */
	void goHomeWithArguments(Bundle arguments);
}