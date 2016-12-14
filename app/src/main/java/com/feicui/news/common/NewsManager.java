package com.feicui.news.common;

import java.util.ArrayList;

import android.content.Context;

import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.model.entity.News;
import com.my.volley.Response.ErrorListener;
import com.my.volley.Response.Listener;

public class NewsManager {
	public static final int MODE_NEXT = 1;
	public static final int MODE_PREVIOUS = 2;

	/**
	 * news_sort?ver=�汾��&imei=�ֻ���ʶ�� �������ŷ���
	 * 
	 * @param context
	 *            :������
	 * @param listener
	 *            :�ɹ��ص��ӿ�
	 * @param errorListener
	 *            :ʧ�ܻص��ӿ�
	 * 
	 *            http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=1
	 */
	public static void loadNewsType(Context context, Listener<String> listener,
			ErrorListener errorListener) {
		int ver = CommonUtil.VERSION_CODE;
		String imei = SystemUtils.getIMEI(context);
		VolleyHttp http = new VolleyHttp(context);
		http.getJSONObject(CommonUtil.APPURL + "/news_sort?ver=" + ver
				+ "&imei=" + imei, listener, errorListener);
	}

	/**
	 * news_list?ver=�汾��&gid=������&dir=1&nid=����id&stamp=20140321&cnt=20 ������������
	 * 
	 * @param mode
	 *            ģʽ/����
	 * @param gid
	 *            �����
	 * @param nid
	 *            ����id
	 * @param listener
	 *            �ɹ��ص��ӿ�
	 * @param erroeListener
	 *            ʧ�ܻص��ӿ�
	 */
	public static void loadNewsFromServer(Context context, int mode, int subId,
			int nid, Listener<String> listener, ErrorListener errorListener) {
		// �汾��
		int ver = CommonUtil.VERSION_CODE;
		String stamp = CommonUtil.getDate();
		VolleyHttp http = new VolleyHttp(context);
		http.getJSONObject(CommonUtil.APPURL + "/news_list?ver=" + ver
				+ "&subid=" + subId + "&dir=" + mode + "&nid=" + nid
				+ "&stamp=" + stamp + "&cnt=" + 20, listener, errorListener);
	}

	public static void loadNewsFromsLocal(int mode, int curId,
			LocalResponseHandler handler) {
		System.out.println("���ݿ����");
		if (mode == MODE_NEXT) {

		} else if (mode == MODE_PREVIOUS) {

		}
	}

	public interface LocalResponseHandler {
		public void update(ArrayList<News> data, boolean isCliearOld);
	}

}