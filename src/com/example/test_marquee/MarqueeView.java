package com.example.test_marquee;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * LinearLayout��Ϊ��View��������һ����TextView
 * 
 * ���ö���ʵ��
 */
public class MarqueeView extends LinearLayout {

	private static final String TAG = MarqueeView.class.getSimpleName();
	private static final int TEXTVIEW_VIRTUAL_WIDTH = 5000;

	private Context context;
	private TextView mTextField;
	private ScrollView mScrollView;

	private Paint mPaint;

	// ������Ҫ���ַ���ȷ�������ȷ��
	private Animation mMoveText = null;

	private float widthOfMarqueeView;
	private float heightOfMarqueeView;

	/** �ַ���֮��ļ�� */
	private String interval = "     ";// ���ּ��

	private String stringOfItem = "";
	private String stringOfTextView = "";
	private String stringOfOrigin = "";
	private float widthOfItem = 0;
	private float widthOfString = 0;
	private float startXOfAnimation = 0;// ��������ʼ����
	private float endXOfAnimation = 0;
	private Runnable mAnimationStartRunnable;
	private int mSpeed = 40;
	private Interpolator mInterpolator = new LinearInterpolator();

	public MarqueeView(Context context) {
		super(context);
		init(context);
	}

	public MarqueeView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		// init helper
		this.context = context;
	}

	private Runnable startRunnable = new Runnable() {

		@Override
		public void run() {
			setText(stringOfOrigin);
		}
	};

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		Logcat.d(TAG, "onLayout called changed: " + changed);

		if (changed) {

			widthOfMarqueeView = getWidth();
			heightOfMarqueeView = getHeight();

			// Logcat.d(TAG, "widthOfMarqueeView: " + widthOfMarqueeView);
			// Logcat.d(TAG, "heightOfMarqueeView: " + heightOfMarqueeView);
			// ����ֱ�ӵ���setText("");�ǲ��еģ�Ϊʲô������
			postDelayed(startRunnable, 0);
		}
	}

	public void setText(String string) {
		Logcat.d(TAG, "setText");
		stringOfOrigin = string;
		stringOfItem = string + interval;
		initViews();
		dealChange();
	}

	public void initViews() {
		clearMarquee();
		removeAllViews();
		mScrollView = new ScrollView(context);
		mTextField = new TextView(context);
		mPaint = mTextField.getPaint();

		mTextField.setSingleLine(true);
		mTextField.setTextColor(Color.parseColor("#FF000000"));
		mPaint.setFakeBoldText(true);
		mPaint.setAntiAlias(true);

		LayoutParams sv1lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		sv1lp.gravity = Gravity.CENTER;
		LayoutParams tv1lp = new LayoutParams(TEXTVIEW_VIRTUAL_WIDTH,
				LayoutParams.MATCH_PARENT);
		tv1lp.gravity = Gravity.CENTER;

		mScrollView.addView(mTextField, tv1lp);
		addView(mScrollView, sv1lp);
	}

	public void clearMarquee() {
		stopMarquee();
	}

	public void stopMarquee() {
		Logcat.d(TAG, "stopMarquee called ");
		if (mAnimationStartRunnable == null)
			return;
		removeCallbacks(mAnimationStartRunnable);
		mTextField.clearAnimation();
		invalidate();
	}

	public void startMarquee() {
		mAnimationStartRunnable = new Runnable() {
			public void run() {
				mTextField.startAnimation(mMoveText);
			}
		};
		postDelayed(mAnimationStartRunnable, 0);
		invalidate();
	}

	/** ���ָ߶ȹ̶�ʱ�Ϳ��Ի�ȡ��Щ�����Ϣ,��ʱ��Ϊ�ַ����Ѿ��ӹ����� */
	public void dealLayoutChange() {
		Logcat.d(TAG, "dealLayoutChange called ");

		// ��Ҫ���¼���
		stringOfTextView = stringOfItem + stringOfItem;
		widthOfItem = mPaint.measureText(stringOfItem);
		widthOfString = mPaint.measureText(stringOfTextView);
		while (widthOfString <= 2 * widthOfMarqueeView) {
			stringOfTextView += stringOfItem;
			widthOfString = mPaint.measureText(stringOfTextView);
		}
		widthOfString = mPaint.measureText(stringOfTextView);
		expandTextView();
		mTextField.setText(stringOfTextView);

		Logcat.d(TAG, "widthOfString: " + widthOfString);
		Logcat.d(TAG, "widthOFmTextField: " + mTextField.getWidth());
	}

	public void dealChange() {
		Logcat.d(TAG, "dealChange called ");
		// ���������С
		setTextFitSize();
		// �������� ���ִ�С�����仯�����ֱ�����ֲ��䣬�������ֶ����ֲ��䣬
		dealLayoutChange();
		// ���ö���
		prepareAnimation();
		// ��ʼ����
		startMarquee();
	}

	/** ����TextView��С������ */
	private void expandTextView() {

		mTextField.layout(getLeft(), getTop(),
				(int) (getLeft() + widthOfString + 5), getTop() + getHeight());
		if (BuildConfig.DEBUG) {
			Logcat.d(TAG, "expandTextView called");
			Logcat.d(TAG, "getLeft(): " + getLeft() + "###");
			Logcat.d(TAG, "getTop(): " + getTop() + "###");
			Logcat.d(TAG, "(int) (getLeft() + widthOfString + 5): "
					+ (int) (getLeft() + widthOfString + 5) + "###");
			Logcat.d(TAG, " getTop() + getHeight(): " + getTop() + getHeight()
					+ "###");
		}
	}

	public void setTextFitSize() {
		mTextField.setTextSize(getFitTextSize(mPaint, getHeight()));
	}

	public int getFitTextSize(Paint paint, int height) {

		// System.out.println("height: " + height);
		int minSize = 10;
		int maxSize = 200;
		int step = 1;

		// int heightOfText = height * 2 / 3;
		int heightOfText = height;
		while (minSize < maxSize) {

			paint.setTextSize(minSize);
			FontMetrics fm = paint.getFontMetrics();

			// //System.out.println("Math.ceil(fm.descent - fm.top): "
			// + Math.ceil(fm.descent - fm.top));
			if (Math.ceil(fm.descent - fm.top) >= heightOfText) {
				break;
			}
			minSize += step;
		}
		System.out.println("--- fit size: " + minSize + " ---");
		return minSize;
	}

	/** �������ֳ��ȡ�view���ȡ�item����ȷ���������� */
	private void prepareAnimation() {

		startXOfAnimation = -(widthOfString - widthOfMarqueeView) % widthOfItem;
		endXOfAnimation = -widthOfString + widthOfMarqueeView;

		final int duration = ((int) Math.abs(startXOfAnimation
				- endXOfAnimation) * mSpeed);

		if (BuildConfig.DEBUG) {
			Logcat.d(TAG, "prepareAnimation called ");
			Logcat.d(TAG, "stringOfTextView: " + stringOfTextView + "###");
			Logcat.d(TAG, "widthOfTextView: " + widthOfString);
			Logcat.d(TAG, "widthOfMarqueeView: " + widthOfMarqueeView);

			Log.d(TAG, "(int) Math.abs(startXOfOut - endXOfOut)       : "
					+ (int) Math.abs(startXOfAnimation - endXOfAnimation));
			Log.d(TAG, "mSpeed       : " + mSpeed);
			Log.d(TAG, "startXOfAnimation       : " + startXOfAnimation);
			Log.d(TAG, "endXOfAnimation         : " + endXOfAnimation);
			Log.d(TAG, "duration  		  : " + duration);
		}

		mMoveText = new TranslateAnimation(startXOfAnimation, endXOfAnimation,
				0, 0);
		mMoveText.setDuration(duration);
		mMoveText.setInterpolator(mInterpolator);
		mMoveText.setFillAfter(true);

		mMoveText.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				startMarquee();
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});
	}

}
