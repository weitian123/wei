package com.feicui.news.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.feicui.news.model.entity.Label;
import com.feicui.news.model.entity.LabelGroup;
import com.feicui.news.model.entity.LabelSub;
import com.google.gson.Gson;
import com.my.volley.Request;
import com.my.volley.Request.Method;
import com.my.volley.RequestQueue;
import com.my.volley.Response;
import com.my.volley.VolleyError;
import com.my.volley.toolbox.JsonObjectRequest;
import com.my.volley.toolbox.Volley;
public class GetLabel {
	Context context;
	/**请求队列*/
    private RequestQueue queue;
    /**请求公共URL*/
    private final String URL = "http://118.244.212.82:9092/newsClient/";
    /**新闻类别地址*/
    private final String TYPE = "news_sort?ver=0000000&imei=000000000000000";
	private SQLiteDatabase db;
	Handler handler;
	List<LabelSub>labelSubs=new ArrayList<LabelSub>();
	public GetLabel(SQLiteDatabase db,Context context,Handler handler) {
		super();
		this.db = db;
		this.context=context;
		this.handler=handler;
	}
	class getlab extends Thread{
		@Override
		public void run() {
			labelSubs=DBUtil.queryLabel(db);
			if(labelSubs.size()>0){
				System.out.println(labelSubs.size()+"------size");
				handler.obtainMessage(0, labelSubs).sendToTarget();
			}
				
			else{
				get();
			}
			super.run();
		}
	}
	public void canGet(){
		new getlab().start();
	}
	private Object parseJson(String result, Class<?> cls) {
		Object type = new Gson().fromJson(result, cls);
		return type;
	}
	public List<LabelSub> get(){
		
		queue = Volley.newRequestQueue(context);//初始化队列
		Request request = null;
		
    request = new JsonObjectRequest(Method.GET, URL+TYPE, null, new Response.Listener<JSONObject>() {
    	//从网络获取数据
	
		
    	
		@Override
		public void onResponse(JSONObject response) {
			System.out.println(response.toString());
			Label type = (Label) parseJson(response.toString(), Label.class);//解析Json
			List<LabelGroup> labelCs = type.getData();
			for (LabelGroup labelC : labelCs) {
				List<LabelSub> labelccs = labelC.getSubgrp();
				for (LabelSub labelCC : labelccs) {
					System.out.println(labelCC.getSubgroup());
					labelSubs.add(labelCC);
					DBUtil.addLabel(db, labelCC);//存入数据库
					handler.obtainMessage(0, labelSubs).sendToTarget();//返回获取到的数据
				}
			}
		
		}
		
	}, new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			
//			System.err.println(error.getMessage());
		}
	});
    queue.add(request);//提交request到队列
	return labelSubs;
	}
}
