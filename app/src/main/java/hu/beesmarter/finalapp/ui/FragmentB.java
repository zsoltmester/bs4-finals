package hu.beesmarter.finalapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.beesmarter.finalapp.R;

/**
 * Screen for XXX
 */
public class FragmentB  extends Fragment {

	public final static String FRAGMENT_B = "fragment_b";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View viewContainer = inflater.inflate(R.layout.fragment_b, container, false);

		return viewContainer;
	}
}