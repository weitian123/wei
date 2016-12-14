package com.feicui.news.control;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.feicui.news.R;
import com.feicui.news.common.DBmanager;
import com.feicui.news.common.NewStringUtil;
import com.feicui.news.common.PopUtil;
import com.feicui.news.control.base.MyBaseActivity;
import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.NewsCollect;
import com.feicui.news.view.adapter.NewsAdapter;

public class CollectActivity extends MyBaseActivity {
	private ListView lv_collect;// 收藏展示ListView
	private List<NewsCollect> newsCollects;
	private int width;
	private LayoutInflater inflater;
	private PopUtil popUtil;
	private PopupWindow pop;
	private List<News> newss;
	private NewsAdapter adapter;
	SQLiteDatabase db;
	private ImageView ct_lin_left;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_collect);
		db = openOrCreateDatabase("news_db", 1, null);
		initUI();
		initData();
		setListener();
		super.onCreate(arg0);
	}

	@Override
	public void initUI() {
		initActionBar(true, false, NewStringUtil.shoucang.sc, null);
		lv_collect = (ListView) findViewById(R.id.lv_collect);
		ct_lin_left = (ImageView) findViewById(R.id.actionbar_left);
		initPop();
		super.initUI();	
	}

	private void initPop() {
		WindowManager manager = getWindowManager();
		// 获取屏幕宽度
		width = manager.getDefaultDisplay().getWidth();
		inflater = LayoutInflater.from(this);

		popUtil = new PopUtil(this, inflater, width);
		popUtil.setMessage(NewStringUtil.shoucang.qxsc);

	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case 1:
				newsCollects = (List<NewsCollect>) msg.obj;
				bmanager.selectNews(handler);
				break;

			case 2:
				List<News> newslist = (List<News>) msg.obj;
				System.out.println(newsCollects.size());
				if (newsCollects.size() > 0 && newslist.size() > 0) {
					for (News news : newslist) {

						for (NewsCollect newsCollection : newsCollects) {
							if (newsCollection.getNid() == news.getNid()
									&& newsCollection.getFlag() == 1) {
								newss.add(news);
							}
						}
					}
				}
				adapter = new NewsAdapter(newss, CollectActivity.this);
				lv_collect.setAdapter(adapter);
				break;
			}
		};
	};

	private DBmanager bmanager;

	@Override
	public void initData() {
		newss = new ArrayList<News>();
		bmanager = new DBmanager(this);
		bmanager.selectCollect(handler);
		// NewsAdapter adapter= new NewsAdapter();
		super.initData();
	}

	@Override
	public void setListener() {
		ct_lin_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		lv_collect.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				PopOnClick click = new PopOnClick(arg2);
				popUtil.setSubmitOnClick(click);
				pop = popUtil.showPop(arg1);

				return true;
			}
		});
		lv_collect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(CollectActivity.this,
						ShowActivity.class);
				intent.putExtra("link", newss.get(arg2).getLink());
				intent.putExtra("nid", newss.get(arg2).getNid());
				startActivity(intent);

			}
		});
		super.setListener();
	}

	class PopOnClick implements OnClickListener {
		int arg2;

		public PopOnClick(int arg2) {
			super();
			this.arg2 = arg2;
		}

		@Override
		public void onClick(View arg0) {// 删除收藏
			if (pop != null && pop.isShowing()) {
				pop.dismiss();
				pop = null;

			}
			NewsCollect collection = new NewsCollect();
			collection.setNid(newss.get(arg2).getNid());

			bmanager.updateCollect(collection);

			newss.remove(arg2);
			adapter.notifyDataSetChanged();

		}
	}
}
