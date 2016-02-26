package hu.bsmart.framework.navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;

public class ScreenManager {

	private static final String STATE_SCREEN_MAP = "STATE_SCREEN_MAP";

	private static ScreenManager instance = null;

	private Map<Class<? extends Fragment>, Screen> screenMap = new HashMap<>();
	private Logger logger;

	private ScreenManager() {
		logger = LoggerProvider.createLoggerForClass(getClass());
	}

	public static ScreenManager getInstance() {
		if (instance == null) {
			instance = new ScreenManager();
		}

		return instance;
	}

	public void registerMapping(Class<? extends Fragment> fragmentClass, Screen screen) {
		if (fragmentClass == null) {
			throw new NullPointerException("Fragment class is missing!");
		}

		if (screen == null) {
			throw new NullPointerException("Screen is missing!");
		}

		screenMap.put(fragmentClass, screen);
	}

	public Screen getScreenForFragment(Fragment fragment) {
		if (fragment == null) {
			throw new NullPointerException("Fragment is missing!");
		}

		Screen result = screenMap.get(fragment.getClass());

		if (result == null) {
			logger.w("No screen for class: " + fragment.getClass());
		}

		return result;
	}

	public void saveState(Bundle outState) {
		outState.putSerializable(STATE_SCREEN_MAP, (Serializable) screenMap);
	}

	public void restoreState(Bundle inState) {
		screenMap = (Map<Class<? extends Fragment>, Screen>) inState.getSerializable(STATE_SCREEN_MAP);
	}
}
