package com.feicui.news.control.base;

import com.feicui.news.R;
import com.feicui.news.control.HomeActivity;
import com.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyBaseActivity extends FragmentActivity {
	private View actionbar_right;
	private View actionbar_left;
	public void openActivity(Class<?> pClass, Bundle bundle) {
		openActivity(pClass, bundle, null);
	}
	public void openActivity(Class<?> pClass, Bundle bundle, Uri uri) {
		Intent intent = new Intent(this, pClass);
		if (bundle != null)
			intent.putExtras(bundle);
		if (uri != null)
			intent.setData(uri);
		startActivity(intent);
//		// 增加动画=======
//		overridePendingTransition(R.anim.anim_activity_right_in,
//				R.anim.anim_activity_bottom_out);
	}

	/** 初始化界面 **/
	public void initUI() {

	}

	/** 初始化数据 **/
	public void initData() {

	}

	/** 设置监听 **/
	public void setListener() {

	}

	/***
	 * activity之间的跳转方法
	 * 
	 * @param cls
	 *            目标activity的.class文件
	 */
	public void jump(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
	}

	/***
	 * activity之间的跳转方法
	 * 
	 * @param cls
	 *            目标activity的.class文件
	 * @param flag
	 *            携带的标记
	 */
	public void jump(Class<?> cls, String flag) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		intent.putExtra("flag", flag);
		startActivity(intent);
	}

	/**
	 * 设置标头属性
	 * 
	 * @param isBack
	 *            是否可以返回，true展示并可返回，false不显示
	 * @param islogin
	 *            islogin 是否需要登录，true显示，false不显示
	 * @param title
	 *            标题文本
	 * @param setOnClick
	 *            按钮的点击事件
	 */
	protected void initActionBar(boolean isBack, boolean islogin, String title,
			OnClickListener setOnClick) {

		// 标题
		TextView ct_text_title = (TextView) findViewById(R.id.actionbar_text);
		// 返回按钮
		ImageView ct_lin_left = (ImageView) findViewById(R.id.actionbar_left);
		// 右边按钮
		ImageView ct_lin_right = (ImageView) findViewById(R.id.actionbar_right);
		if (isBack) {
			// 返回
			ct_lin_left.setImageResource(R.drawable.back);
			ct_lin_left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
//					WebView view = (WebView) findViewById(R.id.anw_web);
//					view.goBack();
					finish();
				}
			});
		}
		if (!islogin&&title.equals("收藏")) {
			//设置按钮为隐藏
			ct_lin_right.setVisibility(View.GONE);
			
			
		}else if(!islogin){
			// 设置按钮为收藏
			ct_lin_right.setImageResource(R.drawable.news_menu);
			
		}else{
			
		}

		ct_text_title.setText(title);

	}
}
