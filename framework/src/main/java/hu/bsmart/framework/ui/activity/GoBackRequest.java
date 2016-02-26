package hu.bsmart.framework.ui.activity;

import android.os.Bundle;

import hu.bsmart.framework.navigation.ScreenNavigator;

class GoBackRequest extends NavigationRequest {
	public GoBackRequest(ScreenNavigator navigator, Bundle arguments) {
		super(navigator, arguments);
	}

	@Override
	public void run() {
		getNavigator().goBackWithArguments(getArguments());
	}

}