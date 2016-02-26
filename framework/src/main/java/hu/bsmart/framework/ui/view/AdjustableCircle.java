package hu.bsmart.framework.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import hu.bsmart.framework.R;

/**
 * <pre>
 * A custom View that draws a circle inside its boundaries following these rules:
 *  - you can set a ratio between 0 and 1.
 *  - the circle's diameter will be the view's smaller dimension (minus padding) multiplied by the ratio
 *  - the circle will be centered inside the view (minus padding)
 *  </pre>
 */
public class AdjustableCircle extends View {

	private static final float DEFAULT_CIRCLE_RATIO = 0.5f;
	private static final int DEFAULT_CIRCLE_COLOR = Color.argb(255, 0, 0, 0);

	private float circleRatio;
	private int alpha;
	private ColorStateList circleColors;

	private Paint circlePaint;

	/**
	 * Creates a new {@code AdjustableCircle}. You can use this constructor to create an instance from code.
	 *
	 * @param context Android {@link Context}
	 */
	public AdjustableCircle(Context context) {
		this(context, null);
	}

	/**
	 * Creates a new {@code AdjustableCircle} by inflating.
	 *
	 * @param context      Android {@link Context}
	 * @param attributeSet {@link AttributeSet} representing the XML attributes of this {@code AdjustableCircle}
	 */
	public AdjustableCircle(Context context, AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Creates a new {@code AdjustableCircle} by inflating and using a default style.
	 *
	 * @param context          Android {@link Context}
	 * @param attributeSet     {@link AttributeSet} representing the XML attributes of this {@code AdjustableCircle}
	 * @param defaultStyleAttr the style that has been assigned to this {@code AdjustableCircle} in the XML layout
	 */
	public AdjustableCircle(Context context, AttributeSet attributeSet, int defaultStyleAttr) {
		super(context, attributeSet, defaultStyleAttr);
		init(context, attributeSet, defaultStyleAttr);
	}

	private void init(Context context, AttributeSet attributes, int defStyleAttr) {
		TypedArray typedArray =
				context.obtainStyledAttributes(attributes, R.styleable.AdjustableCircle, defStyleAttr, 0);

		setCircleRatio(
				normalizeRatio(typedArray.getFloat(R.styleable.AdjustableCircle_circleRatio, DEFAULT_CIRCLE_RATIO)));

		ColorStateList colors = typedArray.getColorStateList(R.styleable.AdjustableCircle_circleColor);
		if (colors == null) {
			colors = ColorStateList
					.valueOf(typedArray.getColor(R.styleable.AdjustableCircle_circleColor, DEFAULT_CIRCLE_COLOR));
		}
		setColors(colors);

		typedArray.recycle();

		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Paint.Style.FILL);
	}

	private float normalizeRatio(float ratio) {
		if (ratio > 1.0f) {
			return 1.0f;
		}

		if (ratio < 0.0f) {
			return 0.0f;
		}

		return ratio;
	}

	/**
	 * Sets the color of the circle.
	 *
	 * @param color the new color
	 */
	public void setColor(int color) {
		this.circleColors = ColorStateList.valueOf(color);
		postInvalidate();
	}

	/**
	 * Sets the colors of the circle.
	 *
	 * @param colors the new color
	 */
	public void setColors(ColorStateList colors) {
		this.circleColors = colors;
		setClickable(colors.getColorForState(new int[]{android.R.attr.state_pressed}, -1) != -1);
		postInvalidate();
	}

	/**
	 * Returns the color of the circle.
	 *
	 * @return the color
	 */
	public int getColor() {
		return circleColors.getColorForState(getDrawableState(), circleColors.getDefaultColor());
	}

	/**
	 * Returns the colors of the circle.
	 *
	 * @return the colors
	 */
	public ColorStateList getColors() {
		return circleColors;
	}

	/**
	 * Sets the ratio for the circle's diameter.
	 *
	 * @param circleRatio the new ratio
	 */
	public void setCircleRatio(float circleRatio) {
		this.circleRatio = normalizeRatio(circleRatio);
		this.alpha = normalizeAlpha(circleRatio);
		postInvalidate();
	}

	private int normalizeAlpha(float ratio) {

		if (ratio > 1.0f) {
			return 20;
		}

		if (ratio < 0.0f) {
			return 255;
		}

		int alphaFromRatio = Math.round(((float) 1.0 - ratio) * 255);

		return (alphaFromRatio < 20) ? 20 : alphaFromRatio;
	}

	/**
	 * Returns the actual ratio for the circle's diameter.
	 *
	 * @return the ratio
	 */
	public float getCircleRatio() {
		return circleRatio;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (!(circleRatio >= 0.0f)) {
			return;
		}

		float widthSize = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		float heightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

		float minSize = widthSize < heightSize ? widthSize : heightSize;
		float diameter = minSize * circleRatio;

		float centerX = getPaddingLeft() + widthSize * 0.5f;
		float centerY = getPaddingTop() + heightSize * 0.5f;

		circlePaint.setColor(circleColors.getColorForState(getDrawableState(), circleColors.getDefaultColor()));
		circlePaint.setAlpha(alpha);
		canvas.drawCircle(centerX, centerY, diameter * 0.5f, circlePaint);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		postInvalidate();
	}
}
