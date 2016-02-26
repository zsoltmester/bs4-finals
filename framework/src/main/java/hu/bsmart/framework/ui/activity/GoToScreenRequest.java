package hu.bsmart.framework.ui.activity;

import android.os.Bundle;

import hu.bsmart.framework.navigation.Screen;
import hu.bsmart.framework.navigation.ScreenNavigator;

class GoToScreenRequest extends NavigationRequest {
	protected final Screen screen;

	public GoToScreenRequest(ScreenNavigator navigator, Screen screen, Bundle arguments) {
		super(navigator, arguments);
		this.screen = screen;
	}

	@Override
	public void run() {
		getNavigator().goToScreenWithArguments(screen, getArguments());
	}
}