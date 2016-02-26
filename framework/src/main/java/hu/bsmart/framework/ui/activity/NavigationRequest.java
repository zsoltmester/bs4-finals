package hu.bsmart.framework.ui.activity;

import android.os.Bundle;

import hu.bsmart.framework.navigation.ScreenNavigator;

abstract class NavigationRequest implements Runnable {

	private final ScreenNavigator navigator;
	private final Bundle arguments;

	protected NavigationRequest(ScreenNavigator navigator, Bundle arguments) {
		this.navigator = navigator;
		this.arguments = arguments;
	}

	protected Bundle getArguments() {
		return arguments;
	}

	protected ScreenNavigator getNavigator() {
		return navigator;
	}

}