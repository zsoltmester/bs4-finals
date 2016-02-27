package hu.beesmarter.finalapp.ui;

import hu.beesmarter.finalapp.R;
import hu.bsmart.framework.navigation.Screen;
import hu.bsmart.framework.ui.fragment.FrameworkFragment;

/**
 * This enumeration contains our screens.
 */
public enum Screens implements Screen {

	HOME(HomeFragment.class, R.layout.fragment_home) {
		@Override
		public int getActionBarTitleResource() {
			return R.string.home_fragment_title;
		}
	},

	OPTIONS(OptionsFragment.class, R.layout.fragment_options) {
		@Override
		public int getActionBarTitleResource() {
			return R.string.options_fragment_title;
		}
	},;

	private Class<? extends FrameworkFragment> controllerFragmentClass;
	private int layoutResourceId;

	Screens(Class<? extends FrameworkFragment> clazz, int layoutResource) {
		this.controllerFragmentClass = clazz;
		this.layoutResourceId = layoutResource;
	}

	@Override
	public Class<? extends FrameworkFragment> getControllerFragmentClass() {
		return controllerFragmentClass;
	}

	public int getLayoutResource() {
		return layoutResourceId;
	}

	@Override
	public ScreenOrientation getOrientation() {
		// TODO decide after we will know the task
		return ScreenOrientation.PORTRAIT;
	}

	@Override
	public boolean hasActionBar() {
		return true;
	}

	@Override
	public boolean hasDynamicTitle() {
		return false;
	}

	@Override
	public boolean isRefreshable() {
		return false;
	}

	@Override
	public int getActionBarTitleResource() {
		return R.string.app_name;
	}

	@Override
	public boolean canNavigateBackHere() {
		return true;
	}

	@Override
	public boolean canNavigateUpFromHere() {
		return true;
	}

	@Override
	public int getFadeInAnimation() {
		// TODO should add a better animation
		return hu.bsmart.framework.R.anim.abc_fade_in;
	}

	@Override
	public int getFadeOutAnimation() {
		// TODO should add a better animation
		return hu.bsmart.framework.R.anim.abc_fade_out;
	}

	@Override
	public boolean needsNfc() {
		return false;
	}

	@Override
	public boolean needsLocation() {
		return false;
	}
}
