package hu.beesmarter.finalapp.finalapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import hu.beesmarter.finalapp.finalapp.R;
import hu.beesmarter.finalapp.finalapp.utils.NavigationUtils;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		//Floating action button, ha kell
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		//Drawer layout
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		//Navigates to the main fragment.
		if (savedInstanceState == null) {
			NavigationUtils.navigateToFragment(this, getContentFrame(), new FragmentA(), FragmentA.FRAGMENT_A, false, false);
		}
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		int id = item.getItemId();

		Fragment fragment = null;
		String tag = null;
		switch (id) {
			case R.id.fragmentA:
				fragment = new FragmentA();
				tag = FragmentA.FRAGMENT_A;
				break;
			case R.id.fragmentB:
				fragment = new FragmentB();
				tag = FragmentB.FRAGMENT_B;
				break;
		}
		if (fragment != null) {
			NavigationUtils.navigateToFragment(this, getContentFrame(), fragment, tag, true, false);
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private View getContentFrame() {
		View appBarMain = findViewById(R.id.app_bar_main);
		View contentMain = appBarMain.findViewById(R.id.content_main);
		return contentMain.findViewById(R.id.content_frame);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
}
