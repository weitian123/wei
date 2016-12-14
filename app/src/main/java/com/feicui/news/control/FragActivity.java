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
				case OnScrollListener.SCROLL_STATE_FLING: // ���ٻ���ʱ
					adapter.setisOnscroll(true);
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// ����ʱ
					adapter.setisOnscroll(false);
					break;
				case OnScrollListener.SCROLL_STATE_IDLE: // ֹͣ����ʱ
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
						if (adapter == null) {// �����������null����˵���ǵ�һ�μ��أ�Ҫ��ʼ��������
							adapter = new NewsAdapter(list, context);
							xListView.setAdapter(adapter);
						} else {// ����ֻ��ˢ���������Ϳ�
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

				if (newss != null && newss.size() > 0) {// �������ҳ����ص������ݣ���˵���и������ݣ�����ԭ�����б������
					list.addAll(newss);
					if (adapter == null) {// �����������null����˵���ǵ�һ�μ��أ�Ҫ��ʼ��������
						adapter = new NewsAdapter(list, context);
						xListView.setAdapter(adapter);
					} else {// ����ֻ��ˢ���������Ϳ�
						adapter.notifyDataSetChanged();
					}
				} else {// ���û�м��ص��������ݣ���˵��û��������
				// �����б������������ظ���
					xListView.setPullLoadEnable(false);
					// �����ظ���İ�ť����
					xListView.setFooterVis(View.GONE);
					Toast.makeText(context, NewStringUtil.shoucang.bc, Toast.LENGTH_SHORT)
							.show();
				}
				// ���غ����ݾ�ֹͣˢ�»���ظ���
				xListView.stopRefresh();
				xListView.stopLoadMore();

				break;

			}

		};
	};

	private void InitUI() {
		xListView = (XListView) getView().findViewById(R.id.fn_list);
		// �����б��ˢ�¼��ؼ���
		xListView.setXListViewListener(this);
		// �����Ƿ��������ˢ��
		xListView.setPullRefreshEnable(true);
		// �����Ƿ�����������ظ���
		xListView.setPullLoadEnable(true);

	}

	@Override
	public void onRefresh() {
		if (list != null) {
			// �������������������ظ�������
			xListView.setPullLoadEnable(true);
			xListView.setFooterVis(View.VISIBLE);
			// ���ñ���ˢ�µ�ʱ��
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
