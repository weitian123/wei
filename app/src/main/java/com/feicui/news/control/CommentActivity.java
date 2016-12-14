package com.feicui.news.control;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.feicui.news.R;
import com.feicui.news.common.CommentsManager;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.NewStringUtil;
import com.feicui.news.common.NewsManager;
import com.feicui.news.common.ParserComments;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.model.biz.NewsDBManager;
import com.feicui.news.model.entity.Comment;
import com.feicui.news.view.adapter.CommentsAdapter;
import com.feicui.news.view.adapter.base.MyBaseActivity;
import com.feicui.news.view.xlistview.XListView;
import com.feicui.news.view.xlistview.XListView.IXListViewListener;
import com.my.volley.Response.ErrorListener;
import com.my.volley.Response.Listener;
import com.my.volley.VolleyError;

/**评论界面***/
public class CommentActivity extends MyBaseActivity {
	/**新闻id*/
	private int nid;
	/**评论列表*/
	private XListView listView;
	/**评论列表适配器*/
	private CommentsAdapter adapter;
	/***/
    private  int mode;	
    /**发送评论按钮*/
    private ImageView imageView_send;
    /**返回按钮*/
    private ImageView imageView_back;
    /**评论编辑框*/
	private EditText editText_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		nid = getIntent().getIntExtra("nid", -1);
		Log.d(LogUtil.TAG, "nid------------->" + nid);
		listView = (XListView) findViewById(R.id.listview);
		imageView_send = (ImageView) findViewById(R.id.imageview);
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		editText_content = (EditText) findViewById(R.id.edittext_comment);
		adapter = new CommentsAdapter(this, listView);
		listView.setAdapter(adapter);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(listViewListener);
		loadNextComment();

		imageView_back.setOnClickListener(clickListener);
		imageView_send.setOnClickListener(clickListener);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_back:
				finish();
				break;
			case R.id.imageview:
				String ccontent = editText_content.getText().toString();
				if (ccontent == null || ccontent.equals("")) {
					Toast.makeText(CommentActivity.this, NewStringUtil.pinlun.pinlun,
							Toast.LENGTH_SHORT).show();
					return;
				}
				imageView_send.setEnabled(false);
				String imei = SystemUtils.getInstance(CommentActivity.this)
						.getIMEI();
				String token = SharedPreferencesUtils
						.getToken(CommentActivity.this);
				if (TextUtils.isEmpty(token)) {
					Toast.makeText(CommentActivity.this, "对不起，您还没有登录.", 0)
							.show();
					return;
				}
				showLoadingDialog(CommentActivity.this, "", true);

				CommentsManager.sendCommnet(CommentActivity.this, nid,
						new Listener<String>() {

							@Override
							public void onResponse(String response) {
								// TODO Auto-generated method stub
								LogUtil.d(LogUtil.TAG, "发表评论返回信息----->"
										+ response.toString());
								int status = ParserComments
										.parserSendComment(response.trim());
								if (status == 0) {
									showToast("评论成功！");
									editText_content.setText(null);
									editText_content.clearFocus();
									loadNextComment();
								} else {
									showToast("评论失败！");
								}
								imageView_send.setEnabled(true);
								dialog.cancel();
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								showToast("服务器连接异常！");
								imageView_send.setEnabled(true);
								dialog.cancel();
							}

						}, CommonUtil.VERSION_CODE + "", token, imei, ccontent);

				break;
			}
		}
	};

	private IXListViewListener listViewListener = new IXListViewListener() {
		@Override
		public void onRefresh() {
			// 加载最新数据
			loadNextComment();
			// 加载完毕
			listView.stopLoadMore();
			listView.stopRefresh();
			listView.setRefreshTime(CommonUtil.getDate());
		}

		@Override
		public void onLoadMore() {
			// 加载下面更多的数据
			int count = adapter.getCount();
			if (count > 1) { // 如果当前的ListView不存在一条item是不允许用户加载更多
				loadPreComment();
			}
			listView.stopLoadMore();
			listView.stopRefresh();
		}
	};

	/**
	 * 加载下面的XX条数据
	 */
	protected void loadPreComment() {
		Comment comment = adapter
				.getItem(listView.getLastVisiblePosition() - 2);
		mode = NewsManager.MODE_PREVIOUS;
		if (SystemUtils.getInstance(this).isNetConn()) {
			CommentsManager.loadComments(this,CommonUtil.VERSION_CODE + "",
					listener, errorListener, nid, 2, comment.getCid());
		}
	}

	/**
	 * 请求最新的评论
	 */
	protected void loadNextComment() {
		int curId = adapter.getAdapterData().size() <= 0 ? 0 : adapter.getItem(
				0).getCid();
		LogUtil.d(LogUtil.TAG, "loadnextcomment--->currentId=" + curId);
		mode = NewsManager.MODE_NEXT;
		if (SystemUtils.getInstance(this).isNetConn()) {
			CommentsManager.loadComments(
					this, 
					CommonUtil.VERSION_CODE + "",
					listener, 
					errorListener, 
					nid, // 新闻id
					1,   // 方向1下拉
					curId);// 
			// CommentsManager.loadComments(mode, nid, curId,new
			// MyJsonHttpResponseHandler());
		}
	}

	Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {

			List<Comment> comments = ParserComments.parserComment(response);
			if (comments == null || comments.size() < 1) {
				return;
			}
			boolean flag = mode == NewsManager.MODE_NEXT ? true : false;
			adapter.appendData(comments, flag);
			adapter.update();

		}
	};
	ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Toast.makeText(CommentActivity.this, "服务器连接错误", Toast.LENGTH_SHORT)
					.show();
		}
	};


}