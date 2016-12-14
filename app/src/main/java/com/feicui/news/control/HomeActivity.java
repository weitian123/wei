package com.feicui.news.control;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.feicui.news.R;
import com.feicui.news.common.Dbhelper;
import com.feicui.news.common.GetFragView;
import com.feicui.news.common.LoadImage;
import com.feicui.news.common.LoadImage.ImageLoadListener;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.NewsView;
import com.feicui.news.control.base.MyBaseActivity;
import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.LabelSub;
import com.feicui.news.model.entity.User;
import com.slidingmenu.lib.SlidingMenu;

public class HomeActivity extends MyBaseActivity implements ImageLoadListener {

	List<LabelSub> labelSubs;

	private NewsView labelLin;
	private BaseEntity<User> user;
	private LinearLayout ah_label;
	// �����ͼ
	private LinearLayout left;
	// �໬�˵�
	private SlidingMenu slidingMenu;
	// ��ҳ�˵���ť
	private ImageView actionbar_left;
	// ��ҳ�û���ť
	private ImageView actionbar_right;
	FragmentManager fragmentManager;
	private LoadImage loadImage;
	private int islogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Intent in = getIntent();
		islogin = in.getExtras().getInt("end");
		if (islogin != -1) {
			labelSubs = (List<LabelSub>) getIntent().getSerializableExtra(
					"label");
		}
		fragmentManager = getSupportFragmentManager();
		initUI();
		initData();
		setListener();

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			user = (BaseEntity<User>) intent.getExtras()
					.getSerializable("info");
			String iv_path = user.getData().getPortrait();
			loadImage.geBitmap(iv_path, imageView1);
			tv_userlogin.setText(user.getData().getUid());
		}

	};

	@Override
	protected void onResumeFragments() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("user");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
		super.onResumeFragments();
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			int what = msg.what;
			switch (what) {

			case 1:
				LabelSub cc = (LabelSub) msg.obj;
				System.out.println(cc.getSubid() + "--->" + cc.getSubgroup());
				break;

			}
		};
	};

	private RelativeLayout rl_reading;

	private TextView tv_userlogin;

	private ImageView imageView1;

	private ImageView iv_friend;

	private ImageView iv_qq;

	private ImageView iv_friends;

	private ImageView iv_weibo;

	@Override
	public void initUI() {
		initActionBar(false, true, "��Ѷ", null);
		ah_label = (LinearLayout) findViewById(R.id.ah_label);
		getNewSoetDate();
		// ��ʼ��SlidingMenu
		slidingMenu = new SlidingMenu(this);
		// ���ò���ģʽ--left(���˵�)
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		// �趨������������
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// �趨�˵��Ŀ��--�����趨
		int width = getWindowManager().getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width / 3);
		// �趨��Ӱ
		// slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidth(5);
		// �������˵�����ʽ
		LayoutInflater inflater = LayoutInflater.from(this);
		left = (LinearLayout) inflater.inflate(R.layout.slidingmenu_left, null);
		slidingMenu.setMenu(left);
		// �����Ҳ�˵�
		slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
		// ���ù���Activity
		slidingMenu.attachToActivity(HomeActivity.this,
				SlidingMenu.SLIDING_CONTENT);
		initSlidingMenu();
		actionbar_left = (ImageView) findViewById(R.id.actionbar_left);
		actionbar_right = (ImageView) findViewById(R.id.actionbar_right);
		iv_friend.setOnClickListener(listener);
		iv_friends.setOnClickListener(listener);
		iv_qq.setOnClickListener(listener);
		iv_weibo.setOnClickListener(listener);
		super.initUI();
	}

	/**
	 * ����ť����
	 */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_friend:
				showShare(WEBCHAT);
				break;
			case R.id.iv_friends:
				showShare(WEBCHATMOMENTS);
				break;
			case R.id.iv_qq:
				showShare(QQQ);
				break;
			case R.id.iv_weibo:
				showShare(SINA);
				break;
			default:
				break;
			}
		}
	};
	public static final int WEBCHAT = 1, QQQ = 2, WEBCHATMOMENTS = 3, SINA = 4;

	private void showShare(int platforms) {
		ShareSDK.initSDK(HomeActivity.this);
		OnekeyShare oks = new OnekeyShare();
		oks.disableSSOWhenAuthorize();
		oks.setTitle(getString(R.string.ssdk_oks_share));
		oks.setTitleUrl("http://sharesdk.cn");
		oks.setText("ÿ������");
		oks.setUrl("http://sharesdk.cn");
		oks.setComment("ÿ������");
		switch (platforms) {
		case WEBCHAT:
			oks.setPlatform(Wechat.NAME);
			break;
		case WEBCHATMOMENTS:
			oks.setPlatform(WechatMoments.NAME);
			break;
		case QQQ:
			oks.setPlatform(QQ.NAME);
			break;
		case SINA:
			oks.setPlatform(SinaWeibo.NAME);
			break;
		}
		oks.show(HomeActivity.this);
	}

	private void initSlidingMenu() {
		rl_reading = (RelativeLayout) left.findViewById(R.id.rl_reading);
		tv_userlogin = (TextView) findViewById(R.id.tv_userlogin);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		iv_friend = (ImageView) findViewById(R.id.iv_friend);
		iv_qq = (ImageView) findViewById(R.id.iv_qq);
		iv_friends = (ImageView) findViewById(R.id.iv_friends);
		iv_weibo = (ImageView) findViewById(R.id.iv_weibo);
		
	}

//	@Override
//	public void initData() {
//		labelLin = new NewsView(HomeActivity.this, labelSubs, handler,
//				fragmentManager);
//		labelLin.creatLabel();
//		ah_label.addView(labelLin);
//		if (islogin == -1) {// ���ǵ���˳���¼��ת����ʱ
//			tv_userlogin.setText("���̵�¼");
//			imageView1
//					.setImageResource(R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
//		}
//		loadImage = new LoadImage(this, this);
//		super.initData();
//	}
	private void getNewSoetDate() {
		labelSubs = (List<LabelSub>) getIntent().getSerializableExtra("label");
		initNewSort(labelSubs);
	}

	private void initNewSort(List<LabelSub> sortChildCs) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		GetFragView newSortView = new GetFragView(HomeActivity.this, sortChildCs,
				fragmentManager,
				new Dbhelper(this, "new_db").getWritableDatabase());
		ah_label.addView(newSortView);
	};

	@Override
	public void setListener() {
		slidingMenulistener();
		actionbar_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// �����˵�
				slidingMenu.showMenu();
			}
		});
		actionbar_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ���Ҳ�˵�
				slidingMenu.showSecondaryMenu();
			}
		});
		tv_userlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (tv_userlogin.getText().toString().equals("���̵�½")) {
					jump(LoginActivity.class);
				} else {
					jump(UserActivity.class);
				}

			}
		});

		super.setListener();
	}

	private void slidingMenulistener() {
		rl_reading.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				jump(CollectActivity.class);

			}
		});

	}

	@Override
	public void imageLoadOk(Bitmap bitmap, String url) {
		LogUtil.d(LogUtil.TAG, "onlistener...load iamge..." + bitmap
				+ "---url=" + url);
		if (bitmap != null) {
			imageView1.setImageBitmap(bitmap);
		}
	}
}
