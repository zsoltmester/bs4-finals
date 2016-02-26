package hu.bsmart.framework.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import hu.bsmart.framework.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

	private static final int[] ATTRS = new int[]{
			android.R.attr.listDivider
	};

	private static final int[] START_PADDING_ATTRS = new int[]{
			R.attr.dividerStartPadding
	};

	private static final int[] END_PADDING_ATTRS = new int[]{
			R.attr.dividerEndPadding
	};

	public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

	public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

	private Drawable mDivider;

	private int mOrientation;
	private int dividerStartPadding;
	private int dividerEndPadding;

	public DividerItemDecoration(Context context, int orientation) {
		final TypedArray a = context.obtainStyledAttributes(ATTRS);
		mDivider = a.getDrawable(0);
		a.recycle();

		final TypedArray dividerStartPaddingArray = context.obtainStyledAttributes(START_PADDING_ATTRS);
		dividerStartPadding = dividerStartPaddingArray.getDimensionPixelSize(0, -1);
		dividerStartPaddingArray.recycle();

		final TypedArray dividerEndPaddingArray = context.obtainStyledAttributes(END_PADDING_ATTRS);
		dividerEndPadding = dividerEndPaddingArray.getDimensionPixelSize(0, -1);
		dividerEndPaddingArray.recycle();

		setOrientation(orientation);
	}

	public void setOrientation(int orientation) {
		if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
			throw new IllegalArgumentException("invalid orientation");
		}
		mOrientation = orientation;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent) {
		if (mOrientation == VERTICAL_LIST) {
			drawVertical(c, parent);
		} else {
			drawHorizontal(c, parent);
		}
	}

	public void drawVertical(Canvas c, RecyclerView parent) {
		final int left = dividerStartPadding >= 0 ? dividerStartPadding : parent.getPaddingLeft();
		final int right = dividerEndPadding >= 0 ? dividerEndPadding : parent.getWidth() - parent.getPaddingRight();

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	public void drawHorizontal(Canvas c, RecyclerView parent) {
		final int top = dividerStartPadding >= 0 ? dividerStartPadding : parent.getPaddingTop();
		final int bottom = dividerEndPadding >= 0 ? dividerEndPadding : parent.getHeight() - parent.getPaddingBottom();

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int left = child.getRight() + params.rightMargin;
			final int right = left + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
		if (mOrientation == VERTICAL_LIST) {
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		} else {
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		}
	}
}