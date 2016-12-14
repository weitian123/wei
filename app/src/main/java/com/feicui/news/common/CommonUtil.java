package com.feicui.news.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class CommonUtil {
	public static final String APPURL = "http://118.244.212.82:9092/newsClient";
	public static final int VERSION_CODE = 1;

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getDate() {
		Date date = new Date(System.currentTimeMillis());
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @param email
	 *            email
	 * @return
	 */
	public static boolean verifyEmail(String email) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)"
						+ "|(([a-zA-Z0-9\\-]+\\.)+))"
						+ "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/***
	 * 验证密码格式
	 * 
	 * @param password
	 * @return
	 */
	public static boolean verifyPassword(String password) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,16}$");
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * 获取当前的版本号(内部识别号)
	 * 
	 * @param context
	 *            上下文对象
	 * @return 当前版本
	 */
	public static int getVersionCode(Context context) {

		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
