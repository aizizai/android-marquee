package com.example.test_marquee;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {

	private MarqueeView marqueeView;

	private String[] strings = { "����Ʋ���", "��Ȼ���������", "��",
			"����һ���ܳ��ܳ��ܳ��ܳ�������11233333666699999" };

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {

				marqueeView
						.setText(strings[(int) Math.round(Math.random() * 10)
								% strings.length]);
			} else if (msg.what == 1) {
				System.out.println("=================================");

				LinearLayout.LayoutParams lp = (LayoutParams) marqueeView
						.getLayoutParams();
				lp.width = 800;
				lp.height = 50;
				marqueeView.setLayoutParams(lp);
			} else if (msg.what == 2) {
				System.out.println("=================================");

				LinearLayout.LayoutParams lp = (LayoutParams) marqueeView
						.getLayoutParams();
				lp.width = 1000;
				lp.height = 200;
				marqueeView.setLayoutParams(lp);
			} else if (msg.what == 3) {
				setLayout(1);
			} else if (msg.what == 4) {
				setLayout(2);
			} else if (msg.what == 5) {
				setLayout(3);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
		marqueeView.setBackgroundColor(Color.RED);
		/*
		 * new Thread() {
		 * 
		 * @Override public void run() { super.run(); try { sleep(3000);
		 * handler.sendEmptyMessage(0); sleep(5000);
		 * handler.sendEmptyMessage(1); sleep(5000);
		 * handler.sendEmptyMessage(2); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * }.start();
		 */
	}

	public void onBtn1Click(View v) {
		handler.sendEmptyMessage(0);
	}

	public void onBtn2Click(View v) {
		handler.sendEmptyMessage(1);
	}

	public void onBtn3Click(View v) {
		handler.sendEmptyMessage(2);
	}

	public void onBtn4Click(View v) {
		handler.sendEmptyMessage(3);
	}

	public void onBtn5Click(View v) {
		handler.sendEmptyMessage(4);
	}

	public void onBtn6Click(View v) {
		handler.sendEmptyMessage(5);
	}

	public void setLayout(int i) {
		LinearLayout.LayoutParams lp = (LayoutParams) marqueeView
				.getLayoutParams();
		lp.width = 600;
		lp.height = 200;
		lp.leftMargin = 200 + i * 5;
		lp.topMargin = 10 + i * 30;
		marqueeView.setLayoutParams(lp);
	}

}
