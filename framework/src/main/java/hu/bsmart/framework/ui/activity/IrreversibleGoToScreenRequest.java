package hu.bsmart.framework.ui.activity;

import android.os.Bundle;

import hu.bsmart.framework.navigation.Screen;
import hu.bsmart.framework.navigation.ScreenNavigator;

class IrreversibleGoToScreenRequest extends GoToScreenRequest {

	public IrreversibleGoToScreenRequest(ScreenNavigator navigator, Screen screen, Bundle arguments) {
		super(navigator, screen, arguments);
	}

	@Override
	public void run() {
		getNavigator().goToScreenWithNoWayBack(screen, getArguments());
	}
}
