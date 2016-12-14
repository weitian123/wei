package com.feicui.news.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.feicui.news.R;
import com.feicui.news.control.FragActivity;
import com.feicui.news.model.entity.Label;
import com.feicui.news.model.entity.LabelSub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class NewsView extends LinearLayout {
	Context context;
	List<LabelSub> labelSubs;
	private TextView textView;
	private Handler handler;
	private FragmentManager fragmentManager;
	Map<Integer, Fragment> map = new HashMap<Integer, Fragment>();
	SQLiteDatabase db;

	public NewsView(Context context, List<LabelSub> labelSubs, Handler handler,
			FragmentManager fragmentManager) {
		super(context);
		this.context = context;
		this.labelSubs = labelSubs;
		this.handler = handler;
		this.fragmentManager = fragmentManager;
		db = context.openOrCreateDatabase("news_db", 1, null);
	}

	public void creatLabel() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1);
		layoutParams.setMargins(15, 5, 0, 5);

		for (int i = 0; i < labelSubs.size(); i++) {
			textView = new TextView(context);
			textView.setText(labelSubs.get(i).getSubgroup());
			textView.setId(i);

			textView.setOnClickListener(new TextOnClick(labelSubs.get(i)));
			textView.setLayoutParams(layoutParams);
			if (i == 0) {
				textView.setTextColor(Color
						.parseColor(NewStringUtil.colors.red));
				textView.performClick();
			} else {

			}
			this.addView(textView);
		}
	}

	public void changecolor(int id, View v) {

	}

	class TextOnClick implements OnClickListener {
		private LabelSub labelSub;

		public TextOnClick(LabelSub labelSub) {
			super();
			this.labelSub = labelSub;
		}

		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			int subid = labelSub.getSubid();
			//tv.setTextColor(Color.parseColor(StringForNews.colors.red));
			Fragment fragment = map.get(subid);//尝试从map中取到碎片
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragment == null) {//当没取到时，新建
				fragment = initFragment(subid);
				map.put(subid, fragment);//把新建的Fragment添加到map
			
				fragmentTransaction.add(R.id.fl_frame, fragment);//把新建的Fragment添加到事物
			} else {//当从map中取到了碎片时
				if (fragment.isHidden()) {//当该fragment是隐藏的时候
					fragmentTransaction.show(fragment);//展示并激活
					fragment.onResume();
				}
			}
			hideOtherfragment(subid, fragmentTransaction);//隐藏其他碎片
			fragmentTransaction.commit();//提交
		}
	}

	private Fragment initFragment(Integer subid) {
		Fragment fragment = null;

		fragment = new FragActivity(db, subid);//根据subid不同生成碎片

		return fragment;
	}

	private void hideOtherfragment(Integer subid,
			FragmentTransaction fragmentTransaction) {
		
		Iterator<Integer> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			int key = iterator.next();
			if (key != subid) {
				Fragment fragment = map.get(key);
				fragmentTransaction.hide(fragment);
				fragment.onPause();
			}
		}
	}
}
