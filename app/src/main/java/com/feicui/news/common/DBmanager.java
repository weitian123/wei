package com.feicui.news.common;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.widget.Toast;

import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.NewsCollect;

public class DBmanager {
	Context context;
	static SQLiteDatabase db;

	public DBmanager(Context context) {
		super();
		this.context = context;
		db = context.openOrCreateDatabase("news_db", 1, null);
	}

	/******
	 * ɾ���ղط���
	 */
	public void updateCollect(NewsCollect collection) {
		new updateCollect(collection).start();
	}

	/*** ɾ���ղ��߳� ***/
	class updateCollect extends Thread {
		NewsCollect collect;

		public updateCollect(NewsCollect collect) {
			super();
			this.collect = collect;
		}

		@Override
		public void run() {

			List<NewsCollect> list = DBUtil.querycollection(db);
			if (list.size() > 0) {
				for (NewsCollect newsCollection : list) {
					if (newsCollection.getNid() == collect.getNid()) {
						collect.setFlag(0);
						DBUtil.updatecollection(db, collect);
					}

				}

			}
			super.run();
		}
	}

	/******
	 * ����ղط���
	 */
	public void addCollect(NewsCollect collect) {

		new addCollect(collect).start();
	}

	/*** ����ղ��߳� ***/
	class addCollect extends Thread {
		NewsCollect collect;

		public addCollect(NewsCollect collect) {
			super();
			this.collect = collect;
		}

		@Override
		public void run() {
			int isExist = 0;

			List<NewsCollect> list = DBUtil.querycollection(db);
			if (list.size() > 0) {
				for (NewsCollect newsCollection : list) {
					if (newsCollection.getNid() == collect.getNid()) {
						isExist = 1;
						updateCollect(collect);
					}

				}
				if (isExist == 0) {
					DBUtil.addCollection(db, collect);
				}
			} else {
				DBUtil.addCollection(db, collect);
			}
			super.run();
		}
	}

	/*******
	 * ��ѯ�ղط���
	 */
	public void selectCollect(Handler handler) {
		new selectCollect(handler).start();
	}

	/*******
	 * ��ѯ�ղ��߳�
	 */
	class selectCollect extends Thread {
		Handler handler;

		public selectCollect(Handler handler) {
			super();
			this.handler = handler;
		}

		@Override
		public void run() {
			List<NewsCollect> list = DBUtil.querycollection(db);
			handler.obtainMessage(1, list).sendToTarget();
			super.run();
		}
	}

	/*******
	 * ��ѯ���ŷ���
	 */
	public void selectNews(Handler handler) {
		new selectNews(handler).start();
	}

	/*******
	 * ��ѯ�����߳�
	 */
	class selectNews extends Thread {
		Handler handler;

		public selectNews(Handler handler) {
			super();
			this.handler = handler;
		}

		@Override
		public void run() {
			List<News> list = DBUtil.queryNewsList(db);
			handler.obtainMessage(2, list).sendToTarget();
			super.run();
		}
	}
}
