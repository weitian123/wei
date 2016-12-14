package com.feicui.news.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.news.R;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.LoadImage;
import com.feicui.news.common.LoadImage.ImageLoadListener;
import com.feicui.news.common.LogUtil;
import com.feicui.news.common.ParserUser;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.SystemUtils;
import com.feicui.news.common.UserManager;
import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.Register;
import com.feicui.news.model.entity.User;
import com.feicui.news.view.adapter.LoginLogAdapter;
import com.feicui.news.view.adapter.base.MyBaseActivity;
import com.my.volley.Response.ErrorListener;
import com.my.volley.Response.Listener;
import com.my.volley.VolleyError;

/** 用户中心 **/
public class UserActivity extends MyBaseActivity implements ImageLoadListener{
	private LinearLayout layout;
	private ImageView imageView, imageView_back;
	private TextView textView, integralTextView, commentTextView;
	private ListView logListview;
	private SharedPreferences sharedPreferences;
	private PopupWindow popupWindow;
	private Bitmap bitmap, alterBitmap;
	private File file;
	private LoadImage loadImage;
	private LoginLogAdapter adapter;
	private Button btn_exit;
	private int islogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		layout = (LinearLayout) findViewById(R.id.layout);
		imageView = (ImageView) findViewById(R.id.icon);
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		textView = (TextView) findViewById(R.id.name);
		integralTextView = (TextView) findViewById(R.id.integral);
		commentTextView = (TextView) findViewById(R.id.comment_count);
		logListview = (ListView) findViewById(R.id.list);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		adapter = new LoginLogAdapter(this, new ArrayList());
		logListview.setAdapter(adapter);
		// 第一个参数：上下文，第二个参数：ImageLoadListener的接口实现对象
		// 创建图片请求LoadImage对象
		loadImage = new LoadImage(this,this);
		initData();
		sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		textView.setText(sharedPreferences.getString("uname", "�ο�"));
		String localpath = sharedPreferences.getString("localpic", null);
		if (localpath != null) {
			bitmap = BitmapFactory.decodeFile(localpath);
			imageView.setImageBitmap(bitmap);
		}
		imageView.setOnClickListener(onClickListener);
		btn_exit.setOnClickListener(onClickListener);
		imageView_back.setOnClickListener(onClickListener);
		initpopupwindow();
	}

	private void initpopupwindow() {
		View contentView = getLayoutInflater().inflate(
				R.layout.item_pop_selectpic, null);
		// 设置popupwindow视图
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true); 
		LinearLayout photo_take = (LinearLayout) contentView
				.findViewById(R.id.photo_take);
		LinearLayout photo_sel = (LinearLayout) contentView
				.findViewById(R.id.photo_sel);
		photo_take.setOnClickListener(onClickListener);
		photo_sel.setOnClickListener(onClickListener);
	}

	/**
	 * 请求用户中心数据
	 */
	private void initData() {
		String token = SharedPreferencesUtils.getToken(this);
		UserManager.getInstance(this).getUserInfo(this, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				LogUtil.d("请求用户中心返回字符串", response);
				BaseEntity<User> user = ParserUser.parserUser(response);
				if (Integer.parseInt(user.getStatus()) != 0) {
					Toast.makeText(UserActivity.this, "请求用户中心失败", 0).show();
					return;
				}

				// 保存用户数据到本地 ： 用户昵称 、 用户头像地址ַ
				SharedPreferencesUtils.saveUser(UserActivity.this, user);
				
				// 显示数据更新UI
				User userCore = user.getData();
				textView.setText(userCore.getUid());
				// 更新积分、发帖数
				integralTextView.setText("积分:" + userCore.getIntegration());
				commentTextView.setText(userCore.getComnum() + "");
				// 更新登录记录数据
				adapter.appendData(userCore.getLoginlog(), true);
				adapter.update();
				// 获取用户头像地址ַ
				String portrait = userCore.getPortrait();
				if (!TextUtils.isEmpty(portrait)) {
					// 此方法内部优先判断缓存中是否有图片，若有则返回。
					// 否则判断本地文件是否有图片存在，若有则返回，
					// 反之，则请求网络数据，最后会回调下面的imageLoadOk()
					loadImage.geBitmap(portrait, imageView);

				}
				Intent intent = new Intent();
				intent.putExtra("info", user);
				intent.setAction("user");
				sendBroadcast(intent);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

			}
		}, CommonUtil.VERSION_CODE + "", token, SystemUtils.getIMEI(this));
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.icon: // 点击用户头像，底部弹起popupwindow
				popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
				break;
			case R.id.imageView_back:
//				startActivity(new Intent(UserActivity.this, HomeActivity.class));
				finish();
				break;
			case R.id.photo_take: // 拍照
				popupWindow.dismiss();
				takePhoto();
				break;
			case R.id.photo_sel: // 从相册选择
				popupWindow.dismiss();
				selectPhoto();
				break;
			case R.id.btn_exit:// 退出登录
				SharedPreferencesUtils.clearUser(UserActivity.this);
				Intent intent = new Intent(UserActivity.this,HomeActivity.class);
				islogin = -1;
				intent.putExtra("end", islogin);
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	/** 跳转到系统的拍照功能 */
	protected void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 100);
	}

	/*** 跳转到系统相片，选择照片 */
	protected void selectPhoto() {
		final Intent intent = getPhotoPickIntent();
		startActivityForResult(intent, 200);
	}

	/*** 封装请求Gallery的intent */
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");// 设置裁剪功能
		intent.putExtra("aspectX", 1); // 宽高比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80); // 宽高值
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true); // 返回裁剪结果
		return intent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				bitmap = (Bitmap) bundle.get("data");
				save(bitmap); // 缓存用户选择的图片
			}
		} else if (requestCode == 200) {
			if (resultCode == Activity.RESULT_OK) {
				bitmap = data.getParcelableExtra("data");
				save(bitmap); // 缓存用户选择的图片
			}

		}
	}

	/** 缓存用户上传的图片 **/
	private void save(Bitmap bitmap) {
		if (bitmap == null)
			return;
		// if(Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED){
		roundPic();
		File dir = new File(Environment.getExternalStorageDirectory(),
				"azynews");
		dir.mkdirs();
		file = new File(dir, "userpic.jpg");
		try {
			OutputStream stream = new FileOutputStream(file);
			if (alterBitmap.compress(CompressFormat.PNG, 100, stream)) {
				// 上传图片
				UserManager.getInstance(this).changePhoto(this,
						SharedPreferencesUtils.getToken(this), file, listener,
						errorListener);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// }else{
		// System.out.println(".....��SDCARD");
		// }
	}

	/** 裁剪图片 */
	private void roundPic() {
		Bitmap backBp = BitmapFactory.decodeResource(getResources(),
				R.drawable.userbg);
		alterBitmap = Bitmap.createBitmap(backBp.getWidth(),
				backBp.getHeight(), backBp.getConfig());
		Canvas canvas = new Canvas(alterBitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		canvas.drawBitmap(backBp, new Matrix(), paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		bitmap = Bitmap.createScaledBitmap(bitmap, backBp.getWidth(),
				backBp.getHeight(), true);
		canvas.drawBitmap(bitmap, new Matrix(), paint);
		// imageView.setImageBitmap(alterBitmap);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
			startActivity(new Intent(this, HomeActivity.class));
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			System.out.println("上传头像返回信息--->" + response);
			BaseEntity<Register> entity = ParserUser
					.parserUploadImage(response);
			if (entity.getData().getResult().equals("0")) {
				// 保存用户头像本地的路径
				SharedPreferencesUtils.saveUserLocalIcon(UserActivity.this,
						file.getAbsolutePath());
				imageView.setImageBitmap(alterBitmap);
			}
		}
	};

	private ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			System.out.println("上传头像返回信息--->" + error.getMessage());
		}
	};

	@Override
	public void imageLoadOk(Bitmap bitmap, String url) {
		LogUtil.d(LogUtil.TAG, "onlistener...load iamge..."+bitmap+"---url="+url);
		if(bitmap !=null){
			imageView.setImageBitmap(bitmap);
		}
	}

}
