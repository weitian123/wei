package com.feicui.news;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.res.Configuration;

import cn.jpush.android.api.JPushInterface;

import com.feicui.news.common.LogUtil;

public class MyApplication extends Application {
	private static Map<String, Object> globalData = new HashMap<String, Object>();

	public static MyApplication application;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		LogUtil.d(LogUtil.TAG, "MyApplication  onCreate() ");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		LogUtil.d(LogUtil.TAG, "MyApplication  onLowMemory() ");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		LogUtil.d(LogUtil.TAG, "MyApplication  onTerminate() ");
	}

	// ��������
	public static void addCacheData(String key, Object value) {
		globalData.put(key, value);
	}

	// ɾ������
	public static void delCacheData(String key) {
		if (globalData.containsKey(key))
			globalData.remove(key);
	}

	// �������
	public static void clearCacheData() {
		globalData.clear();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Object getCacheData(String key) {
		if (globalData.containsKey(key))
			return globalData.get(key);
		return null;
	}

}
