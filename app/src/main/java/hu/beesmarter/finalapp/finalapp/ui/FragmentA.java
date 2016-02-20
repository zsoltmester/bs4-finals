package hu.beesmarter.finalapp.finalapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.beesmarter.finalapp.finalapp.R;

/**
 * Screen for XXX
 */
public class FragmentA  extends Fragment {

	public final static String FRAGMENT_A = "fragment_a";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View viewContainer = inflater.inflate(R.layout.fragment_a, container, false);

		return viewContainer;
	}
}
