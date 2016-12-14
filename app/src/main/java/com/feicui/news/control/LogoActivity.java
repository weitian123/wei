package com.feicui.news.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

import com.feicui.news.R;
import com.feicui.news.common.DBUtil;
import com.feicui.news.common.GetLabel;
import com.feicui.news.control.base.MyBaseActivity;
import com.feicui.news.model.entity.Label;
import com.feicui.news.model.entity.LabelGroup;
import com.feicui.news.model.entity.LabelSub;
import com.google.gson.Gson;

public class LogoActivity extends InstrumentedActivity {
	/** 闪屏图片 *****/
	private ImageView logo_img;
	private Animation animation;
	private SharedPreferences preferences;
	private SQLiteDatabase db;
	public List<LabelSub> labelSubs = new ArrayList<LabelSub>();
	private String url = "http://118.244.212.82:9092/newsClient/news_sort?ver=0000000&imei=000000000000000";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
		int count = preferences.getInt("count", 0);

		if (count == 0) {
			count++;
			Editor editor = preferences.edit();
			// 写入配置信息
			editor.putInt("count", count);
			// 交给管理员
			editor.commit();
			Intent intent = new Intent();

			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		initUI();
		initData();
		setListener();
	}
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
	public void initUI() {
		logo_img = (ImageView) findViewById(R.id.logo_img);
	}

	public void initData() {
		animation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
		logo_img.setAnimation(animation);
	}

	Handler handler=new Handler(){
	@Override
	public void handleMessage(Message msg) {
		labelSubs=(List<LabelSub>) msg.obj;
		super.handleMessage(msg);
	}
};

	public void setListener() {
		animation.setAnimationListener(new AnimationListener() {// 动画状态监听

					@Override
					public void onAnimationStart(Animation arg0) {
						db =openOrCreateDatabase("news_db", 1, null);
					GetLabel label =new GetLabel(db,LogoActivity.this,handler);
					label.canGet();
					System.out.println(labelSubs.size()+"!!!!!!!");
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						if (labelSubs.size() > 0) {
							Intent intent = new Intent(LogoActivity.this,
									HomeActivity.class);
							intent.putExtra("label", (Serializable) labelSubs);
							startActivity(intent);
							finish();

						}

					}
				});
	}
}
