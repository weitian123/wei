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

	//窗体布局参数
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
     * 弹窗PopupWindow方法
     * @param 
     */
	public PopupWindow showPop(View anchor) {
		//初始一个弹窗的对象（写通知的纸）
		pop = new PopupWindow(context);
		//设定弹窗的各种属性，其中宽、高还有主视图必须设定(写通知)
		View contentView = inflater.inflate(R.layout.popuo_window, null);
		
		//内容
		TextView pop_msg_text = (TextView) contentView.findViewById(R.id.pop_text);
		if(message!=null && !message.isEmpty()){
			pop_msg_text.setText(message);

		}
		//提交按钮		
		if(submitOnClick!=null  ){
			
			pop_msg_text.setOnClickListener(submitOnClick);
		}
		
		//设定弹窗主视图
		pop.setContentView(contentView);
		//设定弹窗宽度
		pop.setWidth(width/4);
		//设定弹窗高度
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		//设定弹窗外部是否可以点击
		pop.setOutsideTouchable(true);
		//设定弹窗背景，设置null可以去掉系统默认弹窗背景,
//		设定为new BitmapDrawable()是为了弹窗外部点击让弹窗消失和实体back按钮点击，弹窗消失，注意要添加pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		//设定弹窗默认获取焦点
		pop.setFocusable(true);
//		//设定动画
//		pop.setAnimationStyle(R.style.PopAnimation);
//		setWindowAlpha(0.5f);
		//设定弹窗的消失监听
		
		pop.showAsDropDown(anchor, 0, 0);
		return pop;
	}
}
