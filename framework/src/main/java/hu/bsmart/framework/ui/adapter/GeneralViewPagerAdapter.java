package hu.bsmart.framework.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Custom {@link PagerAdapter} that shows the Views that are
 * given in its constructor.
 */
public class GeneralViewPagerAdapter extends PagerAdapter {

	private final View[] pages;

	private String[] pageTitles;

	public GeneralViewPagerAdapter(View... pageViews) {
		if (pageViews == null || pageViews.length == 0) {
			throw new IllegalArgumentException("You need to specify the views to show!");
		}

		for (int i = 0; i < pageViews.length; i++) {
			if (pageViews[i] == null) {
				throw new NullPointerException("The View at index " + i + " is null!");
			}
		}

		this.pages = pageViews;
	}

	@Override
	public int getCount() {
		return pages.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View result = pages[position];
		container.addView(result);
		return result;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(pages[position]);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (pageTitles == null || position < 0 || position >= pageTitles.length) {
			return "<UNTITLED>";
		}

		return pageTitles[position];
	}

	/**
	 * Sets the page titles.
	 *
	 * @param pageTitles
	 * @throws NullPointerException if titles are null
	 */
	public void setPageTitles(String[] pageTitles) {
		if (pageTitles == null) {
			throw new NullPointerException("Titles cannot be null!");
		}

		for (int i = 0; i < pageTitles.length; i++) {
			if (pageTitles[i] == null) {
				throw new NullPointerException("Title at index " + i + " is null!");
			}
		}

		this.pageTitles = pageTitles;
	}

}
