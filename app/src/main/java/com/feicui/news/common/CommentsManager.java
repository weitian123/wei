package com.feicui.news.common;

import android.content.Context;

import com.feicui.news.common.CommonUtil;
import com.my.volley.Response.ErrorListener;
import com.my.volley.Response.Listener;

/**
 * ���۹���
 * 
 * @author qinyq
 * 
 */
public class CommentsManager {

	// cmt_list ?ver=�汾��&nid=����id&type=1&stamp=yyyyMMdd&cid=����id&dir=0&cnt=20
	
	/**
	 * ��������
	 * 
	 * @param ver
	 *            �汾
	 * @param responseHandler
	 *            �ص��ӿ�
	 * @param args
	 *            ˳��Ҫ������ ������ nid (����id) dir ( ˢ�·��� ��1 ��ʾ������ˢ�£��������µ����� 2
	 *            ��ʾ����ˢ�£��������ظ����xx�� ) cid ����id
	 */
	public static void loadComments(Context context,String ver,
			Listener<String> listener,ErrorListener errorListener, int... args) {
		String url = CommonUtil.APPURL + "/cmt_list?ver=" + ver + "&nid="
				+ args[0] + "&dir=" + args[1] + "&cid=" + args[2] + "&type="
				+ 1 + "&stamp=" + "20140707";
		System.out.println("url---"+url);
		new VolleyHttp(context).getJSONObject(url, listener, errorListener);
	}

	/**
	 * cmt?ver=�汾��&nid=���ű��&token=�û�����&imei=�ֻ���ʶ��&ctx=�������� ��������
	 * 
	 * @param context
	 *            ������
	 * @param nid
	 *            ����id
	 * @param responseHandler
	 *            �ص��ӿ�
	 * @param args
	 *            ˳�����£����� ver:�汾 �� token :�û����� imei:�ֻ�IMEI�� �� ctx : ��������
	 */
	public static void sendCommnet(Context context, int nid,
			Listener<String> listener,ErrorListener errorListener, String... args) {
		String url = CommonUtil.APPURL + "/cmt_commit?nid=" + nid + "&ver="
				+ args[0] + "&token=" + args[1] + "&imei=" + args[2] + "&ctx="
				+ args[3];
		new VolleyHttp(context).getJSONObject(url, listener, errorListener);
	}
	
	/**
	 * cmt_num?ver=�汾��& nid=���ű��
	 * @param ver
	 *            �汾��
	 * @param nid
	 *            ����id
	 * @param responseHandlerInterface
	 *            �ص��ӿ�
	 */
	public static void commentNum(Context context,int ver, int nid,
			Listener<String> listener,ErrorListener errorListener) {
		String url = CommonUtil.APPURL + "/cmt_num?nid=" + nid + "&ver="
				+ ver;
		new VolleyHttp(context).getJSONObject(url, listener, errorListener);
//		new AsyncHttpClient().get(url, responseHandlerInterface);
	}
}
