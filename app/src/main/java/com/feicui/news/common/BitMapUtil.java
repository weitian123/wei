package com.feicui.news.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.feicui.news.model.entity.News;

/*** 新闻图片获取帮助类 ***/
public class BitMapUtil {

	private File cacheFile;// 文件缓存路径
	/** 内存缓存 */
	private LruCache<String, Bitmap> lruCache;
	public static BitMapUtil bitMapUtil;

	public static BitMapUtil getinstance(Context context) {
		if (bitMapUtil == null) {
			synchronized (BitMapUtil.class) {
				if (bitMapUtil == null) {
					bitMapUtil = new BitMapUtil(context);
				}
			}
		}
		return bitMapUtil;
	}

	private BitMapUtil(Context context) {
		cacheFile = context.getCacheDir();
		lruCache = new LruCache<String, Bitmap>(1024 * 1024 * 3);

	}

	public Bitmap getBitMap(String url, ImageView imageView) {

		// 从内存中获取图片
		Bitmap bitmap = lruCache.get(url);
		// 如果内存中获取不到对应图片，则从磁盘中获取
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeFile(new File(cacheFile, url
					.substring(url.lastIndexOf("/") + 1, url.length()))
					.getAbsolutePath());

		}
		// 如果磁盘中也未获取到对应图片，则从网络下载
		if (bitmap == null) {
			DownloadImg downloadImg = new DownloadImg(imageView);
			downloadImg.execute(url);
		}
		imageView.setImageBitmap(bitmap);
		return bitmap;
	}

	class DownloadImg extends AsyncTask<String, Integer, Bitmap> {
		ImageView imageView;

		public DownloadImg(ImageView imageView) {
			super();
			this.imageView = imageView;
		}

		private String url;

		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			Bitmap bitmap = down(url);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				// 设置图片
				imageView.setImageBitmap(result);
				// 添加到内存缓存中
				lruCache.put(url, result);
			}
			super.onPostExecute(result);
		}

		private Bitmap down(String url) {
			Bitmap bitmap = null;
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				HttpURLConnection hc = (HttpURLConnection) new URL(url)
						.openConnection();
				hc.setConnectTimeout(6000);
				File file = new File(cacheFile, url.substring(
						url.lastIndexOf("/") + 1, url.length()));
				if (hc.getResponseCode() == 200) {
					is = hc.getInputStream();
					fos = new FileOutputStream(file, true);
					byte[] b = new byte[1024];
					int len = -1;
					while (-1 != (len = is.read(b))) {
						fos.write(b, 0, len);
					}
					fos.flush();
				}
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return bitmap;
		}
	}

}
