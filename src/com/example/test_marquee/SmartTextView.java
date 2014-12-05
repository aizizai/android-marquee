package com.example.test_marquee;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * ���ִ�С��Ӧ�ı���ĸ߶ȣ��ı���Խ�ߣ�����Խ��width�����wrap_content
 * 
 */
public class SmartTextView extends TextView {

	private final String TAG = "SmartTextView";

	public SmartTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SmartTextView(Context context) {
		super(context);
	}

	public SmartTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// System.out.println("--- SmartTextView create! ---");
	}

	/*
	 * ����specMode��ֵ�������AT_MOST��specSize ����������ɻ�õĿռ䣻 <br> �����EXACTLY��specSize
	 * ������Ǿ�ȷ�ĳߴ磻 <br> �����UNSPECIFIED�����ڿؼ��ߴ���˵��û���κβο����塣
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Logcat.d(TAG, "onMeasure called");

		int mode = MeasureSpec.getMode(heightMeasureSpec);

		if (mode == MeasureSpec.AT_MOST) {
			Log.d(TAG, "--- mode: AT_MOST, ---");
		} else if (mode == MeasureSpec.EXACTLY) {
			Log.d(TAG, "--- mode: EXACTLY ---");
		} else if (mode == MeasureSpec.UNSPECIFIED) {
			Log.d(TAG, "--- mode: UNSPECIFIED ---");
		}
		Log.d(TAG,
				"widthMeasureSpec: " + MeasureSpec.getSize(widthMeasureSpec)
						+ "heightMeasureSpec: "
						+ MeasureSpec.getSize(heightMeasureSpec));

		Logcat.d(TAG, "text: " + getText());
		if (mode == MeasureSpec.EXACTLY ) {
			int height = MeasureSpec.getSize(heightMeasureSpec);
			Logcat.d(TAG, "height  :" + height);
			this.setTextSize(getFitTextSize(height));
		}

	}

	// @Override
	// protected void onLayout(boolean changed, int left, int top, int right,
	// int bottom) {
	// Logcat.d(TAG, "onLayout called");
	// // super.onLayout(changed, left, top, right, bottom);
	// // int height = Math.abs(bottom - top);
	// // this.setTextSize(getFitTextSize(height));
	// }

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);

		// Log.d(TAG, "--- onTextChanged ---");

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void setSize(int height) {
		this.setTextSize(getFitTextSize(height));
	}

	public int getFitTextSize(int height) {

		// System.out.println("height: " + height);
		int minSize = 10;
		int maxSize = 100;
		int step = 1;

		int heightOfText = height * 2 / 3;
		Paint paint = getPaint();

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

}
