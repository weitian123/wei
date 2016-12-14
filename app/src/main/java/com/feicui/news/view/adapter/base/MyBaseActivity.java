package com.feicui.news.view.adapter.base;

import com.feicui.news.R;
import com.feicui.news.common.LogUtil;
import com.my.volley.RequestQueue;
import com.my.volley.toolbox.Volley;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyBaseActivity extends FragmentActivity {

	// �����ġ������� Toast  ȫ��  ���  OpenActivity
	private Toast toast;
	public static int screenW, screenH;
	public Dialog dialog;
	public RequestQueue mQueue;

	/******************************** �������ڵ��� *****************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onCreate() ");
		screenW = getWindowManager().getDefaultDisplay().getWidth();
		screenH = getWindowManager().getDefaultDisplay().getHeight();
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(this);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onStart() ");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onRestart() ");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onResume() ");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onPause() ");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onStop() ");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onDestroy() ");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName()
				+ "onSaveInstanceState() ");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName()
				+ "onRestoreInstanceState() ");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LogUtil.d(LogUtil.TAG, getClass().getSimpleName()
				+ "onConfigurationChanged() ");
	}

	/************************** �������ܷ�װ****************************************/
	public void showToast(int resId) {
		showToast(getString(resId));
	}

	public void showToast(String msg) {
		if (toast == null)
			toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setText(msg);
		toast.show();
	}

	public void openActivity(Class<?> pClass) {
		openActivity(pClass, null, null);
	}

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
		// ���Ӷ���=======
		overridePendingTransition(R.anim.anim_activity_right_in,
				R.anim.anim_activity_bottom_out);
	}

	public void openActivity(String action) {
		openActivity(action, null, null);
	}

	public void openActivity(String action, Bundle bundle) {
		openActivity(action, bundle, null);
	}

	public void openActivity(String action, Bundle bundle, Uri uri) {
		Intent intent = new Intent(action);
		if (bundle != null)
			intent.putExtras(bundle);
		if (uri != null)
			intent.setData(uri);
		startActivity(intent);
		// ���Ӷ���
		overridePendingTransition(R.anim.anim_activity_right_in,
				R.anim.anim_activity_bottom_out);
	}

	public void myFinish() {
		super.finish();
		overridePendingTransition(R.anim.anim_activity_bottom_in,
				R.anim.anim_activity_right_out);
	}

	/**
	 * @Title: showLoadingDialog
	 * @Description: ��ʾһ���ȴ��Ի���
	 * @param mContext
	 *            �����Ļ���
	 * @param msg
	 *            ��Ϣ
	 * @param cancelable
	 *            �Ƿ��ȡ��
	 * @return ����Dialog�������
	 * 
	 */
	public void showLoadingDialog(Context context, String msg,
			boolean cancelable) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		//�Զ���ͼƬ
		ImageView iv_img = (ImageView) v.findViewById(R.id.iv_dialogloading_img);
		// ��ʾ����
		TextView tv_msg = (TextView) v.findViewById(R.id.tv_dialogloading_msg);
		// ���ض���
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		// ʹ��ImageView��ʾ����
		iv_img.startAnimation(animation);
		if(null != msg) {
			// ���ü�����Ϣ	
			tv_msg.setText(msg);		
		}
		// �����Զ�����ʽdialog
		dialog = new Dialog(context, R.style.loading_dialog);
		// �������á����ؼ���ȡ��
		dialog.setCancelable(cancelable);
		// ���ò���
		dialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		//��ʾdialog
		dialog.show();
	}

	/**
	 * @Title: cancelDialog
	 * @Description: ȡ��dialog��ʾ
	 * @author hj
	 */
	public void cancelDialog() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}
}
