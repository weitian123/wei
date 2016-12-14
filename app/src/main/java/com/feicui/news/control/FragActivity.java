package com.feicui.news.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.feicui.news.R;
import com.feicui.news.common.DBUtil;
import com.feicui.news.common.GetNews;
import com.feicui.news.common.NewStringUtil;
import com.feicui.news.model.entity.News;
import com.feicui.news.view.adapter.NewsAdapter;
import com.feicui.news.view.xlistview.XListView;
import com.feicui.news.view.xlistview.XListView.IXListViewListener;

public class FragActivity extends Fragment implements IXListViewListener {

	private NewsAdapter adapter;
	Context context;
	private XListView xListView;
	List<News> newss;
	List<News> list;
	SQLiteDatabase db;
	int subid;
	int nid = 1;
	int dir = 1;

	public FragActivity(SQLiteDatabase db, int subid) {
		this.db = db;
		this.subid = subid;
	}

	@Override
	public void onAttach(Activity activity) {
		context = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_news, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		InitUI();
		InitData();
		setListener();
		super.onActivityCreated(savedInstanceState);
	}

	private void setListener() {
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(context, ShowActivity.class);
				intent.putExtra("link", list.get((int) arg3).getLink());
				intent.putExtra("nid", list.get((int) arg3).getNid());
				context.startActivity(intent);
			}

		});
		xListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				switch (arg1) {
				case OnScrollListener.SCROLL_STATE_FLING: // 快速滑动时
					adapter.setisOnscroll(true);
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滑动时
					adapter.setisOnscroll(false);
					break;
				case OnScrollListener.SCROLL_STATE_IDLE: // 停止滑动时
					adapter.setisOnscroll(false);
					adapter.notifyDataSetChanged();
					break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});

	}

	private void InitData() {
		// new MyThead1().start();
		GetNews getNews = new GetNews(context, db, subid, handler, nid, dir);
		getNews.canGet();
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			GetNews getNews = new GetNews(context, db, subid, handler, nid, dir);
			int what = msg.what;
			switch (what) {
			case 1:
				list = new ArrayList<News>();
				int count = 0;
				newss = (List<News>) msg.obj;
				if (newss.size() > 0) {
					System.out.println(newss.size() + "-----qqq");
					for (News news : newss) {
						if (news.getType() == subid) {
							list.add(news);
						} else {
							System.out.println(count++);
						}

					}
					if (count == newss.size()) {
						getNews.get();
					} else {
						if (adapter == null) {// 如果适配器是null，则说明是第一次加载，要初始化适配器
							adapter = new NewsAdapter(list, context);
							xListView.setAdapter(adapter);
						} else {// 否则，只需刷新适配器就可
							adapter.notifyDataSetChanged();
						}
					}

				} else {
					getNews.get();
				}
				break;

			case 2:
				if (list == null) {
					list = new ArrayList<News>();
				}
				newss = null;
				newss = (List<News>) msg.obj;

				if (newss != null && newss.size() > 0) {// 如果根据页码加载到了数据，则说明有更多数据，则往原本的列表中添加
					list.addAll(newss);
					if (adapter == null) {// 如果适配器是null，则说明是第一次加载，要初始化适配器
						adapter = new NewsAdapter(list, context);
						xListView.setAdapter(adapter);
					} else {// 否则，只需刷新适配器就可
						adapter.notifyDataSetChanged();
					}
				} else {// 如果没有加载到更多数据，则说明没有数据了
				// 设置列表不可以上拉加载更多
					xListView.setPullLoadEnable(false);
					// 将加载更多的按钮隐藏
					xListView.setFooterVis(View.GONE);
					Toast.makeText(context, NewStringUtil.shoucang.bc, Toast.LENGTH_SHORT)
							.show();
				}
				// 加载好数据就停止刷新或加载更多
				xListView.stopRefresh();
				xListView.stopLoadMore();

				break;

			}

		};
	};

	private void InitUI() {
		xListView = (XListView) getView().findViewById(R.id.fn_list);
		// 设置列表的刷新加载监听
		xListView.setXListViewListener(this);
		// 设置是否可以下拉刷新
		xListView.setPullRefreshEnable(true);
		// 设置是否可以上拉加载更多
		xListView.setPullLoadEnable(true);

	}

	@Override
	public void onRefresh() {
		if (list != null) {
			// 重新设置运行上拉加载更多数据
			xListView.setPullLoadEnable(true);
			xListView.setFooterVis(View.VISIBLE);
			// 设置本次刷新的时间
			xListView.setRefreshTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()));
			list.clear();
		}
		dir = 1;
		nid = 1;
		GetNews getNews = new GetNews(context, db, subid, handler, nid, dir);
		getNews.get();
	}

	@Override
	public void onLoadMore() {
		dir = 2;
		nid = list.get(list.size() - 1).getNid();
		GetNews getNews = new GetNews(context, db, subid, handler, nid, dir);
		getNews.get();

	}
}
