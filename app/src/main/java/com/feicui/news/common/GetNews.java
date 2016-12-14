package com.feicui.news.common;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.feicui.news.control.MainActivity;
import com.feicui.news.model.entity.News;
import com.feicui.news.model.entity.NewsP;
import com.google.gson.Gson;
import com.my.volley.Request;
import com.my.volley.Request.Method;
import com.my.volley.RequestQueue;
import com.my.volley.Response;
import com.my.volley.VolleyError;
import com.my.volley.toolbox.JsonObjectRequest;
import com.my.volley.toolbox.Volley;

public class GetNews {
	Context context;
	/** 请求队列 */
	private RequestQueue queue;
	
	private SQLiteDatabase db;
	int nid;
	int dir;
	int subid;
	List<News> newss;
	/** 请求公共URL */
	private final String URL = "http://118.244.212.82:9092/newsClient/news_list?ver=0000000&subid=";
	/** 新闻类别地址 */
	private final String TYPE = "&stamp=20140321000000&cnt=20";
	private Handler handler;

	public GetNews(Context context, SQLiteDatabase db, int subid,
			Handler handler,int nid,int dir) {
		super();
		this.context = context;
		this.db = db;
		this.subid = subid;
		this.handler = handler;
		this.nid =nid;
		this.dir=dir;
	}

	class getlab extends Thread {
		@Override
		public void run() {
		
			newss = DBUtil.queryNewsList(db);
		
					handler.obtainMessage(1, newss).sendToTarget();
			
			
			
			super.run();
		}
	}

	public void canGet() {
		new getlab().start();
	}

	private Object parseJson(String result, Class<?> cls) {
		Object type = new Gson().fromJson(result, cls);
		return type;
	}

	

	public List<News> get() {
		queue = Volley.newRequestQueue(context);
		Request request = null;

		request = new JsonObjectRequest(Method.GET, URL + subid +"&dir="+dir+"&nid="+nid+ TYPE, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						System.out.println(response.toString());
						NewsP newsP = (NewsP) parseJson(response.toString(),
								NewsP.class);

						newss = newsP.getData();
						boolean flag=false;
						if(newss!=null){
						for (News newsNews : newss) {
							List<News>lists=DBUtil.queryNewsList(db);
							if(lists.size()>0){
								for (News news : lists) {
									if(newsNews.getNid()==news.getNid()){
										flag=true;
									}
								}
							}
							if(!flag){//当数据库中没有的时候添加到数据库
//								DBUtil.addNews(MainActivity.db, newsNews);
								flag=false;
							}
							

						}
						}
						handler.obtainMessage(2, newss).sendToTarget();
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						System.err.println(error.getMessage());
					}
				});
		queue.add(request);

		return newss;

	}
}
