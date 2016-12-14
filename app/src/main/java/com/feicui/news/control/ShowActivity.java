package com.feicui.news.control;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feicui.news.R;
import com.feicui.news.common.DBmanager;
import com.feicui.news.common.NewStringUtil;
import com.feicui.news.common.PopUtil;
import com.feicui.news.control.base.MyBaseActivity;
import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.NewsCollect;


public class ShowActivity extends MyBaseActivity {
	private News newsitem;
	private WebView webview;
	private String url;
	private ProgressBar anw_load;
	private ImageView ct_lin_right;
	private int width;
	private LayoutInflater inflater;
	private PopUtil popUtil;
	private PopupWindow pop;
	private int nid;
	private DBmanager dBmanager;
	private TextView tv_commentCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		dBmanager = new DBmanager(this);
		Intent intent = getIntent();
		url = intent.getExtras().getString("link");// 接收新闻详情连接
		nid = intent.getExtras().getInt("nid");// 获取新闻id;
		initUI();
		initData();
		setListener();
	}

	@Override
	public void initUI() {
//		initActionBar(true, false, NewStringUtil.shoucang.zx, null);
		tv_commentCount = (TextView) findViewById(R.id.textView2);
		// 初始化WebView控件
		webview = (WebView) findViewById(R.id.anw_web);
		// 初始化进度条
		anw_load = (ProgressBar) findViewById(R.id.anw_load);

		anw_load.setVisibility(View.VISIBLE);
		ct_lin_right = (ImageView) findViewById(R.id.ct_lin_right);

		super.initUI();
	}

	@Override
	public void setListener() {
		tv_commentCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putInt("nid", nid);
				openActivity(CommentActivity.class, bundle);
			}
		});
		ct_lin_right.setOnClickListener(new OnClickListener() {// 点击弹出PopWindow

					@Override
					public void onClick(View arg0) {
						dBmanager.selectCollect(handler);
						pop = popUtil.showPop(arg0);

					}
				});
		super.setListener();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			int what = msg.what;
			switch (what) {
			case 1:
				List<NewsCollect> list = (List<NewsCollect>) msg.obj;
				if (list.size() > 0) {

					for (NewsCollect newsCollection : list) {
						if (nid == newsCollection.getNid()
								&& newsCollection.getFlag() == 1) {
							popUtil.setMessage(NewStringUtil.shoucang.qxsc);
							pop = popUtil.showPop(ct_lin_right);

						}
					}
				} else {
					popUtil.setMessage(NewStringUtil.shoucang.tjsc);
					pop = popUtil.showPop(ct_lin_right);
				}

				break;

			case 2:
				break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void initData() {

		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 设置离线缓存
		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 设置可缩放
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		// 设置自适应屏幕
		webview.getSettings().setLoadWithOverviewMode(true);
		// 设置宽视图
		webview.getSettings().setUseWideViewPort(true);

		webview.setWebViewClient(new NewsWebViewClient());
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				anw_load.setProgress(newProgress);
				if (newProgress == 100) {// 加载完成隐藏进度条

					anw_load.setVisibility(View.GONE);
				}
			}
		});
		// 设置需要加载的网页
		webview.loadUrl(url);
		WindowManager manager = getWindowManager();
		// 获取屏幕宽度
		width = manager.getDefaultDisplay().getWidth();
		inflater = LayoutInflater.from(this);

		popUtil = new PopUtil(this, inflater, width);
		popUtil.setMessage(NewStringUtil.shoucang.tjsc);
		PopOnClick click = new PopOnClick();
		popUtil.setSubmitOnClick(click);

	}

	class PopOnClick implements OnClickListener {
		@Override
		public void onClick(View arg0) {// 加入收藏
			if (pop != null && pop.isShowing()) {
				pop.dismiss();
				pop = null;

			}
			NewsCollect collect = new NewsCollect();
			collect.setNid(nid);
			if (popUtil.getMessage().equals(NewStringUtil.shoucang.tjsc)) {

				collect.setFlag(1);

				// DBmanager dBmanager = new DBmanager(ShowActivity.this);
				dBmanager.addCollect(collect);
				System.out.println(NewStringUtil.shoucang.sccg);
			} else {
				dBmanager.updateCollect(collect);
			}

		}
	}

	class NewsWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}
}
