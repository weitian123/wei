package com.feicui.news.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.feicui.news.R;
import com.feicui.news.common.CommonUtil;
import com.feicui.news.common.NewStringUtil;
import com.feicui.news.common.ParserUser;
import com.feicui.news.common.SharedPreferencesUtils;
import com.feicui.news.common.UserManager;
import com.feicui.news.model.entity.BaseEntity;
import com.feicui.news.model.entity.Register;
import com.my.volley.Response.ErrorListener;
import com.my.volley.Response.Listener;
import com.my.volley.VolleyError;

public class LoginActivity extends Activity {
	private EditText editTextPwd;
	private Button bt_register;
	private Button bt_forgetPass;
	private Button bt_login;
	private EditText editTextNickname;
	private UserManager userManager;
	private ImageView actio_bar_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initUI();
		initData();
		bt_register.setOnClickListener(clickListener);
		bt_forgetPass.setOnClickListener(clickListener);
		bt_login.setOnClickListener(clickListener);
		actio_bar_left.setOnClickListener(clickListener);
	}

	private void initUI() {
		editTextNickname = (EditText) findViewById(R.id.editText_nickname);
		editTextPwd = (EditText) findViewById(R.id.editText_pwd);
		bt_register = (Button) findViewById(R.id.bt_register);
		bt_forgetPass = (Button) findViewById(R.id.bt_forgetPass);
		bt_login = (Button) findViewById(R.id.bt_login);
		actio_bar_left=(ImageView)findViewById(R.id.actio_bar_left);
	}

	private void initData() {

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_login:
				String name = editTextNickname.getText().toString().trim();
				String pwd = editTextPwd.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					Toast.makeText(LoginActivity.this, NewStringUtil.denglu.user, 0).show();
					return;
				}
				if (TextUtils.isEmpty(pwd)) {
					Toast.makeText(LoginActivity.this, NewStringUtil.denglu.mima2,
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (pwd.length() < 6 || pwd.length() > 16) {
					Toast.makeText(LoginActivity.this, NewStringUtil.denglu.mima,
							Toast.LENGTH_SHORT).show();
					return;
				}
					userManager = UserManager.getInstance(LoginActivity.this);
					userManager.login(LoginActivity.this, listener,
							errorListener,CommonUtil.VERSION_CODE +"", name, pwd, "0");
				break;
			case R.id.bt_register:
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.actio_bar_left:
				finish();
			}
		}
	};
	public Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub

			BaseEntity<Register> register = ParserUser.parserRegister(response);
			int status = Integer.parseInt(register.getStatus());
			String result = "";
			if (status == 0) {
				result = NewStringUtil.denglu.dlcg;
				SharedPreferencesUtils.saveRegister(LoginActivity.this,
						register);
				startActivity(new Intent(LoginActivity.this, UserActivity.class));
				finish();
				LoginActivity.this.overridePendingTransition(
						R.anim.anim_activity_right_in,
						R.anim.anim_activity_bottom_out);
			} else if (status == -3) {
				result = NewStringUtil.denglu.cuowu;
			} else {
				result = NewStringUtil.denglu.shibai;
			}
			Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT)
					.show();
		}
	};

	ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this, NewStringUtil.denglu.dlyc, Toast.LENGTH_SHORT)
					.show();
		}
	};
}
