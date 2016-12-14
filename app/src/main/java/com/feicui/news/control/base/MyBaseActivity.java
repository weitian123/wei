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
//		// ���Ӷ���=======
//		overridePendingTransition(R.anim.anim_activity_right_in,
//				R.anim.anim_activity_bottom_out);
	}

	/** ��ʼ������ **/
	public void initUI() {

	}

	/** ��ʼ������ **/
	public void initData() {

	}

	/** ���ü��� **/
	public void setListener() {

	}

	/***
	 * activity֮�����ת����
	 * 
	 * @param cls
	 *            Ŀ��activity��.class�ļ�
	 */
	public void jump(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
	}

	/***
	 * activity֮�����ת����
	 * 
	 * @param cls
	 *            Ŀ��activity��.class�ļ�
	 * @param flag
	 *            Я���ı��
	 */
	public void jump(Class<?> cls, String flag) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		intent.putExtra("flag", flag);
		startActivity(intent);
	}

	/**
	 * ���ñ�ͷ����
	 * 
	 * @param isBack
	 *            �Ƿ���Է��أ�trueչʾ���ɷ��أ�false����ʾ
	 * @param islogin
	 *            islogin �Ƿ���Ҫ��¼��true��ʾ��false����ʾ
	 * @param title
	 *            �����ı�
	 * @param setOnClick
	 *            ��ť�ĵ���¼�
	 */
	protected void initActionBar(boolean isBack, boolean islogin, String title,
			OnClickListener setOnClick) {

		// ����
		TextView ct_text_title = (TextView) findViewById(R.id.actionbar_text);
		// ���ذ�ť
		ImageView ct_lin_left = (ImageView) findViewById(R.id.actionbar_left);
		// �ұ߰�ť
		ImageView ct_lin_right = (ImageView) findViewById(R.id.actionbar_right);
		if (isBack) {
			// ����
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
		if (!islogin&&title.equals("�ղ�")) {
			//���ð�ťΪ����
			ct_lin_right.setVisibility(View.GONE);
			
			
		}else if(!islogin){
			// ���ð�ťΪ�ղ�
			ct_lin_right.setImageResource(R.drawable.news_menu);
			
		}else{
			
		}

		ct_text_title.setText(title);

	}
}
