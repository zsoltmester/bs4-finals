package hu.beesmarter.finalapp.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Utils class for navigation between {@link Fragment} and {@link Activity} classes.
 */
public class NavigationUtils {

	/**
	 * Navigate to a fragment.
	 *
	 * @param activity       the activity.
	 * @param fragment       the fragment.
	 * @param tag            the tag.
	 * @param addToBackStack boolean, {@code true}, if the current page needs to add to the navigation back stack, {@code false} otherwise.
	 */
	public static void navigateToFragment(Activity activity, View contentFrame, Fragment fragment, String tag, boolean addToBackStack, boolean withAnimation) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (withAnimation) {
//			fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left);
		}
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(null);
		}
		fragmentTransaction.replace(contentFrame.getId(), fragment, tag);
		fragmentTransaction.commit();
	}

	/**
	 * Navigate the last added page in the back stack.
	 *
	 * @param activity the activity.
	 */
	public static void navigateToBack(Activity activity) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		if (fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStack();
		}
	}

	/**
	 * Navigates to an activity.
	 *
	 * @param context     the context.
	 * @param newActivity the new activity.
	 */
	public static void navigateToActivity(Context context, Activity newActivity) {
		Intent intent = new Intent(context, newActivity.getClass());
		context.startActivity(intent);
	}

	/**
	 * Navigates to new activity, and destroys the old.
	 *
	 * @param context     the context.
	 * @param newActivity the new activity.
	 */
	public static void navigateToActivityWithoutBackStack(Context context, Activity newActivity) {
		Intent intent = new Intent(context, newActivity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}