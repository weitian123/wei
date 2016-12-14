package com.feicui.news.control;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.feicui.news.R;
import com.feicui.news.common.Dbhelper;
import com.feicui.news.common.DotMark;
import com.feicui.news.control.base.MyBaseActivity;
import com.feicui.news.view.adapter.MainActivityAdapter;

public class MainActivity extends MyBaseActivity {
	/** 页面容器 **/
	private ViewPager am_viewp;
	/** 图片集合 ***/
	List<ImageView> list;
	private int currentItem;
	private ImageView imageView1;
	private DotMark dotCreate;
	private LinearLayout am_dot_lin;
	public static SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new Dbhelper(this, "news_db").getReadableDatabase();
		initUI();
		initData();
		setListener();
	}

	@Override
	public void initUI() {
		am_viewp = (ViewPager) findViewById(R.id.am_viewp);
		am_dot_lin = (LinearLayout) findViewById(R.id.am_dot_lin);
		super.initUI();
	}

	@Override
	public void initData() {

		list = new ArrayList<ImageView>();
		imageView1 = new ImageView(this);
		imageView1.setImageResource(R.drawable.welcome);
		imageView1.setScaleType(ScaleType.FIT_XY);
		list.add(imageView1);
		imageView1 = new ImageView(this);
		imageView1.setImageResource(R.drawable.wy);
		imageView1.setScaleType(ScaleType.FIT_XY);
		list.add(imageView1);
		imageView1 = new ImageView(this);
		imageView1.setImageResource(R.drawable.bd);
		imageView1.setScaleType(ScaleType.FIT_XY);
		list.add(imageView1);
		imageView1 = new ImageView(this);
		imageView1.setImageResource(R.drawable.small);
		imageView1.setScaleType(ScaleType.FIT_XY);
		list.add(imageView1);
		dotCreate = new DotMark(this, list.size());
		dotCreate.dot();
		am_dot_lin.addView(dotCreate);
		MainActivityAdapter adapter = new MainActivityAdapter(list, this);
		am_viewp.setAdapter(adapter);
		super.initData();
	}

	@Override
	public void setListener() {
		am_viewp.setOnPageChangeListener(new OnPageChangeListener() {// 滑动监听

			@Override
			public void onPageSelected(int arg0) {
				dotCreate.selected(arg0);// 让圆点跟着页面滑动
				currentItem = arg0;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		am_viewp.setOnTouchListener(new OnTouchListener() {
			float startX;
			float endX;
			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 触碰时
					startX = event.getX();
					break;
				case MotionEvent.ACTION_UP:// 放手时
					endX = event.getX();
					if (currentItem == (list.size() - 1) && startX - endX >= 25) {

						jump(LogoActivity.class);
						finish();

					}
					break;
				}
				return false;
			}
		});

		super.setListener();
	}

}
