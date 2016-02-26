package hu.bsmart.framework.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import hu.bsmart.framework.R;

/**
 * Custom View that shows an arrow. The angle of the arrow can be
 * adjusted.
 */
public class RotatingArrow extends View {

	private static final float SQRT_3 = (float) (Math.sqrt(3.0));

	private static final float DEFAULT_INITIAL_DEGREE = 0.0f;
	private static final float DEFAULT_LENGTH_RATIO = 0.8f;
	private static final float DEFAULT_THICKNESS_RATIO = 0.2f;
	private static final int DEFAULT_ARROW_COLOR = Color.argb(255, 0, 0, 0);
	private static final boolean DEFAULT_ANIMATE_BY_DEFAULT = true;
	private static final int DEFAULT_ANIMATION_DURATION = 1000;

	private float degree;
	private float degreeAfterAnimation;

	private float lengthRatio;
	private float thicknessRatio;

	private ColorStateList colors;

	private boolean animateByDefault;
	private long animationDuration;

	private Paint arrowPaint;
	private Path arrowPath;

	private Animation rotationAnimation;

	private AnimationListener rotationAnimationListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			synchronized (RotatingArrow.this) {
				rotationAnimation = null;
				degree = degreeAfterAnimation;
				postInvalidate();
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	};

	/**
	 * Constructs a new {@code RotatingArrow}.
	 *
	 * @param context the Android context
	 */
	public RotatingArrow(Context context) {
		this(context, null);
	}

	/**
	 * Constructs a new {@code RotatingArrow}.
	 *
	 * @param context the Android context
	 * @param attrs   the attribute set of the XML attributes
	 */
	public RotatingArrow(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Constructs a new {@code RotatingArrow}.
	 *
	 * @param context      the Android context
	 * @param attrs        the attribute set of the XML attributes
	 * @param defStyleAttr style attribute in the current theme to be applied
	 */
	public RotatingArrow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, AttributeSet attributes, int defStyleAttr) {
		TypedArray typedArray = context.obtainStyledAttributes(attributes, R.styleable.RotatingArrow, defStyleAttr, 0);

		degree = normalizeDegree(typedArray.getFloat(R.styleable.RotatingArrow_initialDegree, DEFAULT_INITIAL_DEGREE));
		lengthRatio = normalizeRatio(typedArray.getFloat(R.styleable.RotatingArrow_lengthRatio, DEFAULT_LENGTH_RATIO));
		thicknessRatio =
				normalizeRatio(typedArray.getFloat(R.styleable.RotatingArrow_thicknessRatio, DEFAULT_THICKNESS_RATIO));

		colors = typedArray.getColorStateList(R.styleable.RotatingArrow_arrowColor);
		if (colors == null) {
			colors = ColorStateList
					.valueOf(typedArray.getColor(R.styleable.RotatingArrow_arrowColor, DEFAULT_ARROW_COLOR));
		}

		animateByDefault =
				typedArray.getBoolean(R.styleable.RotatingArrow_animateByDefault, DEFAULT_ANIMATE_BY_DEFAULT);
		animationDuration = typedArray.getInt(R.styleable.RotatingArrow_animationDuration, DEFAULT_ANIMATION_DURATION);

		typedArray.recycle();

		arrowPaint = new Paint();
		arrowPaint.setAntiAlias(true);
		arrowPaint.setStyle(Style.FILL);

		arrowPath = new Path();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float widthSize = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		float heightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

		float size = widthSize < heightSize ? widthSize : heightSize;

		float halfLength = size * lengthRatio * 0.5f;
		float thickness = size * thicknessRatio;
		float halfThickness = thickness * 0.5f;

		float centerX = widthSize * 0.5f;
		float centerY = heightSize * 0.5f;

		canvas.save();
		canvas.rotate(degree, centerX, centerY);

		arrowPath.reset();
		arrowPath.moveTo(-halfThickness, halfLength);
		arrowPath.lineTo(halfThickness, halfLength);
		arrowPath.lineTo(halfThickness, SQRT_3 * thickness - halfLength);
		arrowPath.lineTo(thickness, SQRT_3 * thickness - halfLength);
		arrowPath.lineTo(0, -halfLength);
		arrowPath.lineTo(-thickness, SQRT_3 * thickness - halfLength);
		arrowPath.lineTo(-halfThickness, SQRT_3 * thickness - halfLength);
		arrowPath.lineTo(-halfThickness, halfLength);
		arrowPath.close();
		arrowPath.offset(centerX, centerY);

		arrowPaint.setColor(colors.getColorForState(getDrawableState(), colors.getDefaultColor()));

		canvas.drawPath(arrowPath, arrowPaint);
		canvas.restore();
	}

	private float normalizeDegree(float degree) {
		while (degree > 360.0f) {
			degree -= 360.0f;
		}

		while (degree < 0.0f) {
			degree += 360.0f;
		}

		return degree;
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
	 * Rotates the arrow to the given angle, in degrees. If the arrow
	 * is set to animate by default, then the rotation will be smooth.
	 *
	 * @param newDegree the new angle in degrees
	 */
	public void setDegree(float newDegree) {
		setDegree(newDegree, animateByDefault);
	}

	/**
	 * Rotates the arrow to the given angle, in degrees.
	 *
	 * @param newDegree the new angle in degrees
	 * @param animate   if {@code true}, the arrow will rotate smoothly
	 */
	public synchronized void setDegree(float newDegree, boolean animate) {
		while (newDegree > 360.0f) {
			newDegree -= 360.0f;
		}

		if (rotationAnimation != null) {
			rotationAnimation.cancel();
			rotationAnimation = null;
			degree = degreeAfterAnimation;
			postInvalidate();
		}

		if (!animate) {
			degree = newDegree;
			postInvalidate();
			return;
		}

		float difference = newDegree - degree;
		degreeAfterAnimation = newDegree;

		rotationAnimation =
				new RotateAnimation(0.0f, difference, getMeasuredWidth() * 0.5f, getMeasuredHeight() * 0.5f);
		rotationAnimation.setAnimationListener(rotationAnimationListener);
		rotationAnimation.setStartOffset(0);
		rotationAnimation.setDuration(animationDuration);
		rotationAnimation.setInterpolator(new LinearInterpolator());

		startAnimation(rotationAnimation);
	}

	/**
	 * Sets the color of the arrow.
	 *
	 * @param color the new color
	 */
	public void setColor(int color) {
		this.colors = ColorStateList.valueOf(color);
		postInvalidate();
	}

	/**
	 * Sets the colors of the arrow.
	 *
	 * @param colors the new color
	 */
	public void setColors(ColorStateList colors) {
		this.colors = colors;
		postInvalidate();
	}

	/**
	 * Returns the color of the arrow.
	 *
	 * @return the color
	 */
	public int getColor() {
		return colors.getDefaultColor();
	}

	/**
	 * Returns the colors of the arrow.
	 *
	 * @return the colors
	 */
	public ColorStateList getColors() {
		return colors;
	}

	/**
	 * Checks if the arrow is animating by default.
	 *
	 * @return {@code true} if the arrow is animating by default, {@code false} otherwise
	 */
	public boolean doesAnimateByDefault() {
		return animateByDefault;
	}

	/**
	 * Sets whether arrow should animate by default or not.
	 *
	 * @param animateByDefault {@code true} if the arrow should animate, {@code false} otherwise
	 */
	public void setAnimateByDefault(boolean animateByDefault) {
		this.animateByDefault = animateByDefault;
	}

	/**
	 * Gets the duration of the animation.
	 *
	 * @return the duration in ms
	 */
	public long getAnimationDuration() {
		return animationDuration;
	}

	/**
	 * Sets the duration of the animation.
	 *
	 * @param duration the duration in ms
	 */
	public void setAnimationDuration(long duration) {
		if (duration < 0) {
			duration = 0;
		}

		animationDuration = duration;
	}

}