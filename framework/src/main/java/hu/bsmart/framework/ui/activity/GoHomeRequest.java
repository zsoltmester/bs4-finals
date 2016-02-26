package hu.bsmart.framework.ui.activity;

import android.os.Bundle;

import hu.bsmart.framework.navigation.ScreenNavigator;

class GoHomeRequest extends NavigationRequest {
	public GoHomeRequest(ScreenNavigator navigator, Bundle arguments) {
		super(navigator, arguments);
	}

	@Override
	public void run() {
		getNavigator().goHomeWithArguments(getArguments());
	}

}