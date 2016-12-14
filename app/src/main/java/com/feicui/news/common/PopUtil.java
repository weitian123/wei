package com.feicui.news.common;

import com.feicui.news.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;



public class PopUtil {
	private Context context;
	private LayoutInflater inflater;
	private int width;

	//���岼�ֲ���
	private WindowManager.LayoutParams lp;
	private PopupWindow pop;
	 public PopUtil(Context context, LayoutInflater inflater, int width) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.width = width;
	
		this.lp =((Activity) context).getWindow().getAttributes();
		
	 }
	 
	 private String message;
	 public void setMessage(String message){
		 this.message = message;
	 } 
	 private OnClickListener submitOnClick;
	public void setSubmitOnClick(OnClickListener submitOnClick) {
		this.submitOnClick = submitOnClick;
		
	}
	public String getMessage(){
		return message;
	}
	/**
     * ����PopupWindow����
     * @param 
     */
	public PopupWindow showPop(View anchor) {
		//��ʼһ�������Ķ���д֪ͨ��ֽ��
		pop = new PopupWindow(context);
		//�趨�����ĸ������ԣ����п��߻�������ͼ�����趨(д֪ͨ)
		View contentView = inflater.inflate(R.layout.popuo_window, null);
		
		//����
		TextView pop_msg_text = (TextView) contentView.findViewById(R.id.pop_text);
		if(message!=null && !message.isEmpty()){
			pop_msg_text.setText(message);

		}
		//�ύ��ť		
		if(submitOnClick!=null  ){
			
			pop_msg_text.setOnClickListener(submitOnClick);
		}
		
		//�趨��������ͼ
		pop.setContentView(contentView);
		//�趨�������
		pop.setWidth(width/4);
		//�趨�����߶�
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		//�趨�����ⲿ�Ƿ���Ե��
		pop.setOutsideTouchable(true);
		//�趨��������������null����ȥ��ϵͳĬ�ϵ�������,
//		�趨Ϊnew BitmapDrawable()��Ϊ�˵����ⲿ����õ�����ʧ��ʵ��back��ť�����������ʧ��ע��Ҫ���pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		//�趨����Ĭ�ϻ�ȡ����
		pop.setFocusable(true);
//		//�趨����
//		pop.setAnimationStyle(R.style.PopAnimation);
//		setWindowAlpha(0.5f);
		//�趨��������ʧ����
		
		pop.showAsDropDown(anchor, 0, 0);
		return pop;
	}
}
