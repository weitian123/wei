package com.feicui.news.common;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.feicui.news.model.entity.LabelSub;
import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.NewsCollect;

public class DBUtil {
	/**
	 * 查询新闻列表
	 * 
	 * @param db
	 * @return List<News>
	 */
	public static List<News> queryNewsList(SQLiteDatabase db) {

		List<News> list = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from News", null);
			list = new ArrayList<News>();
			News news = null;
			while (cursor.moveToNext()) {
				news = new News();

				news.setNid(cursor.getInt(cursor.getColumnIndex("nid")));

				news.setType(cursor.getInt(cursor.getColumnIndex("type")));

				news.setStamp(cursor.getString(cursor.getColumnIndex("stamp")));

				news.setIcon(cursor.getString(cursor.getColumnIndex("icon")));

				news.setTitle(cursor.getString(cursor.getColumnIndex("title")));

				news.setSummary(cursor.getString(cursor
						.getColumnIndex("summary")));

				news.setLink(cursor.getString(cursor.getColumnIndex("link")));

				list.add(news);

				news = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	/***
	 * 添加新闻信息
	 * 
	 * @param db
	 * @return
	 */
	public static News addNews(SQLiteDatabase db, News news) {

		db.execSQL(
				" insert into News(nid,type,stamp,icon,title,summary,link) values (?,?,?,?,?,?,?)",
				new String[] { news.getNid() + "", news.getType() + "",
						news.getStamp(), news.getIcon(), news.getTitle(),
						news.getSummary(), news.getLink() });

		return news;
	}

	/***
	 * 添加标签信息
	 * 
	 * @param db
	 * @return
	 */
	public static LabelSub addLabel(SQLiteDatabase db, LabelSub labelcc) {

		db.execSQL(" insert into label(subid,subgroup) values (?,?)",
				new String[] { labelcc.getSubid() + "", labelcc.getSubgroup() });

		return labelcc;
	}

	/**
	 * 查询标签列表
	 * 
	 * @param db
	 * @return List<LabelCC>
	 */
	public static List<LabelSub> queryLabel(SQLiteDatabase db) {

		List<LabelSub> list = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from label", null);
			list = new ArrayList<LabelSub>();
			LabelSub labelcc = null;
			System.out.println("-----------分割线");
			while (cursor.moveToNext()) {
				System.out.println("-----------有数据");
				labelcc = new LabelSub();

				labelcc.setSubid(cursor.getInt(cursor.getColumnIndex("subid")));

				labelcc.setSubgroup(cursor.getString(cursor
						.getColumnIndex("subgroup")));

				list.add(labelcc);
				System.out.println(list.size() + "--listsize");
				labelcc = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	

	/**
	 * 查询收藏列表
	 * 
	 * @param db
	 * 
	 */
	public static List<NewsCollect> querycollection(SQLiteDatabase db) {
		NewsCollect collection = null;
		List<NewsCollect> list = new ArrayList<NewsCollect>();
		Cursor cursor = null;
		try {

			cursor = db.rawQuery("select * from collect", null);

			while (cursor.moveToNext()) {
				collection = new NewsCollect();
				collection.setNid(cursor.getInt(cursor.getColumnIndex("nid")));
				collection
						.setFlag(cursor.getInt(cursor.getColumnIndex("flag")));
				list.add(collection);

				collection = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	/***
	 * 添加收藏
	 * 
	 * @param db
	 * @return
	 */
	public static void addCollection(SQLiteDatabase db, NewsCollect collection) {

		db.execSQL(" insert into collect(nid,flag) values (?,?)", new String[] {
				collection.getNid() + "", collection.getFlag() + "" });

	}
	/***
	 * 修改收藏信息
	 * 
	 * @param db
	 * @return
	 */
	public static void updatecollection(SQLiteDatabase db,
			NewsCollect collection) {

		db.execSQL("update collect set flag=" + collection.getFlag()
				+ " where nid=" + collection.getNid() + "");

	}

}
