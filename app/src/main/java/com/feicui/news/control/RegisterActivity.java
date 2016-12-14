package com.feicui.news.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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

public class RegisterActivity extends Activity {
	private EditText editTextEmail;
	private EditText editTextName;
	private EditText editTextPwd;
	private Button but_register;
	private CheckBox checkBox;
	private UserManager userManager;
	private ImageView actio_bar_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initUI();
	}
	private void initUI() {
		editTextEmail = (EditText) findViewById(R.id.editText_email);
		editTextName = (EditText) findViewById(R.id.editText_name);
		editTextPwd = (EditText) findViewById(R.id.editText_pwd);
		but_register = (Button) findViewById(R.id.bt_register);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		but_register.setOnClickListener(clickListener);
		actio_bar_left=(ImageView)findViewById(R.id.actio_bar_left);
		actio_bar_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	}
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(!checkBox.isChecked()){
				Toast.makeText(RegisterActivity.this, NewStringUtil.denglu.xieyi, Toast.LENGTH_SHORT).show();
				return;
			}
			String email=editTextEmail.getText().toString();
			String name=editTextName.getText().toString().trim();
			String pwd=editTextPwd.getText().toString().trim();
			if(!CommonUtil.verifyEmail(email)){
				Toast.makeText(RegisterActivity.this, NewStringUtil.denglu.geshi, Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(name) ){
				Toast.makeText(RegisterActivity.this, NewStringUtil.denglu.nichen, Toast.LENGTH_SHORT).show();
				return ;
			}
			if(!CommonUtil.verifyPassword(pwd)){
				Toast.makeText(RegisterActivity.this, NewStringUtil.denglu.mima, Toast.LENGTH_SHORT).show();
				return;
			}
			if (userManager == null)
				userManager = UserManager.getInstance(RegisterActivity.this);
			userManager.register(RegisterActivity.this, listener,
					errorListener, CommonUtil.VERSION_CODE + "", name, pwd,
					email);
		}
	};
	ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Toast.makeText(RegisterActivity.this,  NewStringUtil.denglu.yichang, Toast.LENGTH_SHORT)
					.show();
		}
	};
	Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			BaseEntity<Register> register = ParserUser.parserRegister(response);
			String result = null;
			String explain = null;
			Register data = register.getData();
			int status = Integer.parseInt(register.getStatus());
			if (status == 0) {
				result = data.getResult().trim();
				explain = data.getExplain();
				result =NewStringUtil.denglu.chenggong;
				if (result.equals("0")) {
					// 保存用户信息
					SharedPreferencesUtils.saveRegister(RegisterActivity.this,
							register);
					startActivity(new Intent(RegisterActivity.this,
							UserActivity.class));
					// 增加动画=======
					RegisterActivity.this.overridePendingTransition(
							R.anim.anim_activity_right_in,
							R.anim.anim_activity_bottom_out);
				}
				Toast.makeText(RegisterActivity.this, explain,
						Toast.LENGTH_SHORT).show();
			}

		}
	};
}
