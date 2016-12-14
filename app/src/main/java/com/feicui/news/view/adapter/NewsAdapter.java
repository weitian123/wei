package com.feicui.news.view.adapter;

import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.news.R;
import com.feicui.news.common.BitMapUtil;
import com.feicui.news.model.entity.News;
import com.feicui.news.view.adapter.base.MyBaseAdapter;

public class NewsAdapter extends MyBaseAdapter<News> {
	LayoutInflater inflater;
	List<News> list;
	boolean flag = false;

	public NewsAdapter(List<News> list, Context context) {
		super(list, context);

		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setisOnscroll(boolean flag) {// 传入是否在快速滑动
		this.flag = flag;
	}

	class Holder {
		ImageView ni_img;
		TextView ni_title;
		TextView ni_summary;
		TextView ni_stamp;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder holder = null;
		if (arg1 == null) {
			holder = new Holder();
			arg1 = inflater.inflate(R.layout.news_item, null);
			holder.ni_img = (ImageView) arg1.findViewById(R.id.ni_img);
			holder.ni_title = (TextView) arg1.findViewById(R.id.ni_title);
			holder.ni_summary = (TextView) arg1.findViewById(R.id.ni_summary);
			holder.ni_stamp = (TextView) arg1.findViewById(R.id.ni_stamp);
			arg1.setTag(holder);

		} else {
			holder = (Holder) arg1.getTag();
		}
		News news = list.get(arg0);
		if (flag) {// 快速滑动时设置新闻图片为默认图片
			holder.ni_img.setImageResource(R.drawable.ic_launcher);

		} else {// 没有在快速滑动时设置为原有图片

			BitMapUtil bitMapUtil = BitMapUtil.getinstance(context);
			bitMapUtil.getBitMap(news.getIcon(), holder.ni_img);
		}
		holder.ni_title.setText(news.getTitle());
		holder.ni_summary.setText(news.getSummary());
		holder.ni_stamp.setText(news.getStamp() + "");
		return arg1;
	}

}
