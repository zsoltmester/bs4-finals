package hu.bsmart.framework.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom View for showing the available time for tasks.
 * <p/>
 * The bar's width will decrease as time is running out, and the color of the bar
 * will change from green to red gradually.
 */
public class TimeBar extends View {

	/**
	 * Constructor mainly for creating a new {@code TimeBar} from code.
	 *
	 * @param context Android {@link Context}
	 */
	public TimeBar(Context context) {
		this(context, null);
	}

	/**
	 * Constructor mainly for creating a new {@code TimeBar} by inflating.
	 *
	 * @param context      Android {@link Context}
	 * @param attributeSet {@link AttributeSet} representing the XML attributes of this {@code TimeBar}
	 */
	public TimeBar(Context context, AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Constructor mainly for creating a new {@code TimeBar} by inflating.
	 *
	 * @param context          Android {@link Context}
	 * @param attributeSet     {@link AttributeSet} representing the XML attributes of this {@code TimeBar}
	 * @param defaultStyleAttr the style that has been assigned to this {@code TimeBar} in the XML layout
	 */
	public TimeBar(Context context, AttributeSet attributeSet, int defaultStyleAttr) {
		super(context, attributeSet, defaultStyleAttr);
		init();
	}

	/**
	 * Refresh interval in milliseconds
	 */
	private static final long REFRESH_TIME_MS = 20;

	private long maxScoreTimeOffsetMillis = 0;
	private long noScoreTimeOffsetMillis = 0;
	private long absoluteMaxScoreTimeMillis = 0;
	private long absoluteNoScoreTimeMillis = 0;

	private Paint barPaint;

	float timeRatio;
	float barWidth;

	private boolean started;

	/**
	 * Initializes the View
	 */
	private void init() {
		barPaint = new Paint();
		barPaint.setStyle(Style.FILL_AND_STROKE);
		started = false;
	}

	/**
	 * Initializes and starts the {@code TimerBar}.
	 */
	public void start() {
		started = true;
		long actualTime = System.currentTimeMillis();

		absoluteMaxScoreTimeMillis = actualTime + maxScoreTimeOffsetMillis;
		absoluteNoScoreTimeMillis = actualTime + noScoreTimeOffsetMillis;

		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!started) {
			barPaint.setColor(Color.argb(255, 0, 255, 0));
			canvas.drawRect(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), barPaint);
			return;
		}

		long actualTime = System.currentTimeMillis();

		if (actualTime >= absoluteNoScoreTimeMillis) {
			return;
		} else if (actualTime <= absoluteMaxScoreTimeMillis) {
			barPaint.setColor(Color.argb(255, 0, 255, 0));
			canvas.drawRect(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), barPaint);
			postInvalidateDelayed(REFRESH_TIME_MS);
		} else {
			timeRatio = ((float) (absoluteNoScoreTimeMillis - actualTime)) /
					(absoluteNoScoreTimeMillis - absoluteMaxScoreTimeMillis);
			barWidth = canvas.getWidth() * timeRatio;

			barPaint.setColor(
					Color.argb(255,
							(int) (255 * (1.0f - timeRatio)),
							(int) (255 * timeRatio),
							0));
			canvas.drawRect(canvas.getWidth() - barWidth, 0.0f, canvas.getWidth(), canvas.getHeight(), barPaint);
			postInvalidateDelayed(REFRESH_TIME_MS);
		}
	}

	/**
	 * Sets the times for this {@code TimeBar} in milliseconds.
	 *
	 * @param maxScoreTimeOffset the time interval when the player still gets max score (in milliseconds)
	 * @param noScoreTimeOffset  the time interval when the player gets zero score (in milliseconds)
	 * @throws IllegalArgumentException if any of the two times are negative
	 * @throws IllegalArgumentException if {@code maxScoreTimeMillis > noScoreTimeMillis}
	 */
	public void setTimeOffset(long maxScoreTimeOffset, long noScoreTimeOffset) {
		if (maxScoreTimeOffset < 0) {
			throw new IllegalArgumentException("Max score time cannot be negative!");
		}

		if (noScoreTimeOffset < 0) {
			throw new IllegalArgumentException("No score time cannot be negative!");
		}

		if (maxScoreTimeOffset > noScoreTimeOffset) {
			throw new IllegalArgumentException("No score time cannot be earlier than max score time!");
		}

		this.maxScoreTimeOffsetMillis = maxScoreTimeOffset;
		this.noScoreTimeOffsetMillis = noScoreTimeOffset;
		postInvalidate();
	}

	/**
	 * Returns the time offset when the player still gets max score.
	 *
	 * @return the max score time offset
	 */
	public long getMaxScoreTimeOffset() {
		return this.maxScoreTimeOffsetMillis;
	}

	/**
	 * Returns the time offset when the player gets no score.
	 *
	 * @return the no score time offset
	 */
	public long getNoScoreTimeOffset() {
		return this.noScoreTimeOffsetMillis;
	}

	/**
	 * Gets the ratio of the remaining time and the full time.
	 *
	 * @return the remaining time ratio
	 */
	public float getCurrentTimeRatio() {
		long actual = System.currentTimeMillis();

		if (actual <= absoluteMaxScoreTimeMillis) {
			return 1.0f;
		}

		if (actual >= absoluteNoScoreTimeMillis) {
			return 0.0f;
		}

		if (absoluteNoScoreTimeMillis - absoluteMaxScoreTimeMillis == 0) {
			return 0.0f;
		}

		return ((float) (absoluteNoScoreTimeMillis - actual)) /
				(absoluteNoScoreTimeMillis - absoluteMaxScoreTimeMillis);
	}

}
