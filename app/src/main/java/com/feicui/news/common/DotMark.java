package com.feicui.news.common;

import com.feicui.news.R;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DotMark extends LinearLayout {
	Context context;
	int pagerNum;

	public DotMark(Context context, int pagerNum) {
		super(context);
		this.context = context;
		this.pagerNum = pagerNum;
		// TODO Auto-generated constructor stub
	}

	public void dot() {
		LayoutParams layoutParams = new LayoutParams(30, 30, 1);
		layoutParams.setMargins(5, 5, 5, 5);
		for (int i = 0; i < pagerNum; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(layoutParams);
			if (i == 0) {
				imageView.setImageResource(R.drawable.a4);

			} else {
				imageView.setImageResource(R.drawable.a4);
				imageView.setAlpha(100);
			}
			this.addView(imageView);
		}
	}

	// 根据ViewPager的切换改变圆点状态
	public void selected(int position) {
		for (int i = 0; i < pagerNum; i++) {
			ImageView imageView = (ImageView) this.getChildAt(i);
			if (i == position) {
				imageView.setImageResource(R.drawable.a4);
				imageView.setAlpha(250);
			} else {
				imageView.setImageResource(R.drawable.a4);
				imageView.setAlpha(100);
			}
		}
	}

}
