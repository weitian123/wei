package com.feicui.news.common;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;

import com.feicui.news.R;
import com.my.volley.RequestQueue;
import com.my.volley.Response.ErrorListener;
import com.my.volley.Response.Listener;
import com.my.volley.toolbox.ImageLoader;
import com.my.volley.toolbox.ImageLoader.ImageCache;
import com.my.volley.toolbox.ImageLoader.ImageListener;
import com.my.volley.toolbox.MultiPosttRequest;
import com.my.volley.toolbox.StringRequest;
import com.my.volley.toolbox.Volley;

public class VolleyHttp {

	public static RequestQueue mQueue;
	Context context;

	public VolleyHttp(Context context) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context);
		}
		this.context = context;
	}

	public void getJSONObject(String url, Listener<String> listener,
			ErrorListener errorListener) {
		StringRequest request = new StringRequest(url, listener, errorListener);
		mQueue.add(request);
	}

	public void addImage(String url, ImageCache imageCache, ImageView iv) {
		ImageLoader mImageLoader = new ImageLoader(mQueue, imageCache);
		ImageListener listener = ImageLoader.getImageListener(iv,
				R.drawable.defaultpic, android.R.drawable.ic_delete);
		mImageLoader.get(url, listener);
	}

	

	public void upLoadImage(String url, File file, Listener<String> listener,
			ErrorListener errorListener) {
		MultiPosttRequest request = new MultiPosttRequest(url, listener,
				errorListener);
		request.buildMultipartEntity("portrait", file);
		mQueue.add(request);
	}

	public void addUserString(String url, String token, String imei,
			Listener<String> listener, ErrorListener errorListener) {
		MultiPosttRequest request = new MultiPosttRequest(url, listener,
				errorListener);
		request.buildMultipartEntity("token", token);
		request.buildMultipartEntity("imei", imei);
		request.buildMultipartEntity("ver", 1 + "");
		mQueue.add(request);
	}

}
