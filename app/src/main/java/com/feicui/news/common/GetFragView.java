package com.feicui.news.common;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.feicui.news.R;
import com.feicui.news.common.NewStringUtil;
import com.feicui.news.control.FragActivity;
import com.feicui.news.model.entity.LabelSub;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
public class GetFragView extends LinearLayout {
	private List<Button> buttons = new ArrayList<Button>();
	Map<Integer, Fragment> map = new HashMap<Integer, Fragment>();
	private List<LabelSub> newsSubs;
	private Context context;
	private FragmentManager fragmentManager;
	private SQLiteDatabase database;

	public GetFragView(Context context, List<LabelSub> sortChildCs,
			FragmentManager fragmentManager, SQLiteDatabase database) {
		super(context);
		this.context = context;
		this.newsSubs = sortChildCs;
		this.fragmentManager = fragmentManager;
		this.database = database;
		initNewSort();
	}

	@SuppressLint("ResourceAsColor")
	private void initNewSort() {
		LayoutParams layoutParams = new LayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1f));

		for (int i = 0; i < newsSubs.size(); i++) {
			Button button = new Button(context);
			button.setLayoutParams(layoutParams);
			button.setText(newsSubs.get(i).getSubgroup());
			button.setId(i);
			button.setBackgroundColor(Color.TRANSPARENT);
			buttons.add(button);
			button.setOnClickListener(new newSortClick(button.getId(),button));
			if (i == 0) {
				// Ä¬ÈÏµã»÷
				button.performClick();
				button.setTextColor(Color.parseColor(NewStringUtil.colors.red));
			}
			
			this.addView(button);
		}

	}

	class newSortClick implements OnClickListener {
		int buttonID;
		Button button;
		public newSortClick(int buttonID,Button button) {
			super();
			this.buttonID = buttonID;
			this.button=button;
		}

		@SuppressLint("ResourceAsColor")
		public void onClick(View view) {
			Fragment fragment = map.get(buttonID);
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragment == null) {
				fragment = initFragment(buttonID);
				map.put(buttonID, fragment);
				fragmentTransaction.add(R.id.fl_frame, fragment).commit();
			} else {
				if (fragment.isHidden()) {
					fragmentTransaction.show(fragment).commit();
					fragment.onResume();
				}
			}
			fragmented(buttonID, fragmentTransaction);
//			fragmentTransaction.commit();
			
			ButtononClick(buttonID);
		}
	}

	private Fragment initFragment(Integer buttonID) {
		Fragment fragment = null;
		int subid;
		for (int i = 0; i < newsSubs.size(); i++) {
			if (i == buttonID) {
				subid = newsSubs.get(i).getSubid();
				fragment = new FragActivity(database, subid);
			}
		}
		return fragment;
	}

	private void fragmented(Integer buttonID,
			FragmentTransaction fragmentTransaction) {
		Iterator<Integer> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Integer integer = iterator.next();
			if (integer != buttonID) {
				Fragment fragment = map.get(integer);
				fragmentTransaction.hide(fragment);
				fragment.onPause();
			}
		}
	}
	
	@SuppressLint("ResourceAsColor")
	private void ButtononClick(int buttonID){
		for (int i = 0; i < buttons.size(); i++) {
			Button button=buttons.get(i);
			if(button.getId()==buttonID){
				button.setTextColor(Color.parseColor(NewStringUtil.colors.red));
			}else{
				button.setTextColor(Color.parseColor(NewStringUtil.colors.black));
			}
		} 
	}

}
